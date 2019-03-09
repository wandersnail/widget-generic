package com.snail.widget.helper

import androidx.recyclerview.widget.RecyclerView

/**
 *
 *
 * date: 2019/3/9 22:44
 * author: zengfansheng
 */
interface OnStartDragListener {
    /**
     * 当开始拖拽时
     * 
     * @param viewHolder 被拖拽的view的持有者
     */
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}