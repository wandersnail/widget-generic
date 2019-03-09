package com.snail.widget.helper

import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * 基本的RecyclerView的适配器
 *
 * date: 2019/3/9 23:14
 * author: zengfansheng
 */
abstract class BaseItemTouchAdapter<T, VH : BaseItemTouchViewHolder> @JvmOverloads constructor(list: MutableList<T> = ArrayList()) : RecyclerView.Adapter<VH>(), ItemTouchAdapter {
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

    override fun getItemCount(): Int {
        return items.size
    }
    
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemSwiped(position: Int, direction: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, items[position], position)
    }
    
    abstract fun onBindViewHolder(holder: VH, item: T, position: Int)
}