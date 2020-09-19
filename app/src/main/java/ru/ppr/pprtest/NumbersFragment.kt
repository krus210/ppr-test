package ru.ppr.pprtest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_numbers.*
import kotlinx.android.synthetic.main.fragment_numbers.view.*
import ru.ppr.pprtest.adapters.NumberItem
import ru.ppr.pprtest.adapters.NumbersAdapter


private const val TYPE_OF_FRAGMENT = "type"

class NumbersFragment : Fragment() {
    private var type: String? = null
    private var loading = true

    private val vmFactory by lazy { ViewModelFactory(type ?: PRIME_NUMBERS_TYPE) }
    private val numbersViewModel by lazy {
        ViewModelProvider(requireActivity(), vmFactory).get(
            NumbersViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE_OF_FRAGMENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_numbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated", type!!)
        with(container) {
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            adapter = NumbersAdapter(mutableListOf())
            numbersViewModel.loadNumbers(null, null)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        if (loading) {
                            val visibleItemCount = (layoutManager as GridLayoutManager).childCount
                            val totalItemCount = (layoutManager as GridLayoutManager).itemCount
                            val pastVisibleItems =
                                (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false
                                val list = (adapter as NumbersAdapter).list
                                numbersViewModel.loadNumbers(list.last(), list[list.lastIndex - 1])
                            }
                        }
                    }
                }
            })
        }
        numbersViewModel.getNumbers().observe(viewLifecycleOwner, Observer {
            with((container.adapter as NumbersAdapter)) {
//                val lastIndex = list.lastIndex
                list.addAll(it)
                notifyDataSetChanged()
                loading = true
            }
        })
    }


    companion object {
        const val PRIME_NUMBERS_TYPE = "prime_numbers_type"
        const val FIBONACCI_NUMBERS_TYPE = "fibonacci_numbers_type"

        @JvmStatic
        fun newInstance(type: String) =
            NumbersFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_OF_FRAGMENT, type)
                }
            }
    }
}