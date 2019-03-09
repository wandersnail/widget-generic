package com.snail.widget.helper

import android.graphics.Canvas
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 *
 *
 * date: 2019/3/9 23:45
 * author: zengfansheng
 */
open class SimpleItemTouchCallback(protected val adapter: ItemTouchAdapter) : ItemTouchHelper.Callback() {
    private var longPressDragEnabled = true
    private var itemViewSwipeEnabled = true
    /** 条目被拖拽或滑动时的阴影 */
    var elevation = 15f
    var translationZ = 6f

    /**
     * 设置长按是否可拖拽
     */
    fun setLongPressDragEnabled(enabled: Boolean) {
        longPressDragEnabled = enabled
    }

    /**
     * 设置是否可滑动删除条目
     */
    fun setItemViewSwipeEnabled(enabled: Boolean) {
        itemViewSwipeEnabled = enabled
    }
    
    override fun isLongPressDragEnabled(): Boolean {
        return longPressDragEnabled
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return itemViewSwipeEnabled
    }
    
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (recyclerView.layoutManager is GridLayoutManager) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            makeMovementFlags(dragFlags, 0)
        } else {
            makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END)
        }
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) {
            return false
        }
        return adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemSwiped(viewHolder.adapterPosition, direction)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.itemView?.elevation = elevation
        viewHolder?.itemView?.translationZ = translationZ
        if (viewHolder is ItemTouchViewHolder) {
            when (actionState) {
                ItemTouchHelper.ACTION_STATE_DRAG -> viewHolder.onDrag()
                ItemTouchHelper.ACTION_STATE_SWIPE -> viewHolder.onSwipe()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.elevation = 0f
        viewHolder.itemView.translationZ = 0f
        if (viewHolder is ItemTouchViewHolder) {
            viewHolder.onClear()
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_DRAG -> {
                viewHolder.itemView.translationY = dY
            }
            ItemTouchHelper.ACTION_STATE_SWIPE -> {
                viewHolder.itemView.translationX = dX
            }
            else -> super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}