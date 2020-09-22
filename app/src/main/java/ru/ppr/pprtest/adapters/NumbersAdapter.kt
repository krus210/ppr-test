package ru.ppr.pprtest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_layout.view.*
import ru.ppr.pprtest.R

class NumbersAdapter(
    val list: MutableList<NumberItem>,
    var onLastItemCallback: ((Int, NumberItem?, NumberItem?) -> Unit)? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var countForLoading = 50

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return NumbersViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val numberItem = list[position]
        if (position == list.lastIndex || position > countForLoading) {
            val preLastItem = list[position - 1]
            onLastItemCallback?.invoke(position, numberItem, preLastItem)
            countForLoading += countForLoading
        }
        (holder as NumbersViewHolder).bind(numberItem)
    }

    override fun getItemCount(): Int = list.size

    inner class NumbersViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(numberItem: NumberItem) {
            with (view) {
                tv_number.text = "${numberItem.number}"
                val color = if (numberItem.isWhiteColor) {
                    R.color.colorWhite
                } else {
                    R.color.colorWhiteGrey
                }
                setBackgroundColor(ContextCompat.getColor(context, color))
            }
        }
    }

}