package ru.ppr.pprtest

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_numbers.*
import ru.ppr.pprtest.adapters.NumbersAdapter
import ru.ppr.pprtest.viewmodels.FibonacciViewModel
import ru.ppr.pprtest.viewmodels.PrimeNumbersViewModel
import ru.ppr.pprtest.viewmodels.ViewModelFactory


private const val TYPE_OF_FRAGMENT = "type"

class NumbersFragment : Fragment() {
    private var type: String? = null
    private var loading = true

    private val vmFactory by lazy { ViewModelFactory() }
    private val numbersViewModel by lazy {
        val viewModelClass = if (type == PRIME_NUMBERS_TYPE) PrimeNumbersViewModel::class.java
        else FibonacciViewModel::class.java
        ViewModelProvider(requireActivity(), vmFactory).get(
            viewModelClass
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
            numbersViewModel.loadNumbers()
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
                                numbersViewModel.loadNumbers( list.lastIndex, list.last(), list[list.lastIndex - 1])
                            }
                        }
                    }
                }
            })
        }
        numbersViewModel.getNumbers().observe(viewLifecycleOwner, Observer {
            with((container.adapter as NumbersAdapter)) {
                Log.d("list", it.toString())
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