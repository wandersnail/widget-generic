package com.snail.widget.helper

/**
 * 可拖拽ViewHolder
 *
 * date: 2019/3/9 22:33
 * author: zengfansheng
 */
interface ItemTouchViewHolder {
    /**
     * 条目被滑动，此时可改变view的状态，和未被滑动的条目区分
     */
    fun onSwipe()

    /**
     * 条目被拖拽，此时可改变view的状态，和未被拖拽的条目区分
     */
    fun onDrag()

    /**
     * 条目被放开，此时应还原view的状态
     */
    fun onClear()
}