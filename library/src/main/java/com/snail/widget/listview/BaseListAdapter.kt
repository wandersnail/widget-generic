package com.snail.widget.listview

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*

abstract class BaseListAdapter<T> @JvmOverloads constructor(val context: Context, list: MutableList<T> = ArrayList()) : BaseAdapter() {
    private val tagKey = 1098237452
    var items: MutableList<T> = list
        set(value) {
            field = value
            notifyDataSetChanged()
        }
        
    fun refresh(list: List<T>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): T {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @Suppress("unchecked_cast")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: BaseViewHolder<T> = if (convertView == null) {
            val h = createViewHolder(position)
            view = h.createView()
            view.setTag(tagKey, h)
            h
        } else {
            convertView.getTag(tagKey) as BaseViewHolder<T>
        }
        viewHolder.onBind(items[position], position)
        return view!!
    }

    protected abstract fun createViewHolder(position: Int): BaseViewHolder<T>
}