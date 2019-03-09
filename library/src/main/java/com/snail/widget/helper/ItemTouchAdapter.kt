package com.snail.widget.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * 可拖拽适配器
 *
 * date: 2019/3/9 22:32
 * author: zengfansheng
 */
interface ItemTouchAdapter {
    /**
     * 当条目被拖拽完全离开另一个条目时被调用。实现此方法最后需要调用[RecyclerView.Adapter.notifyItemMoved]来通知
     * 
     * @param fromPosition 从哪个条目移动来。在越过一个条目后，即使未放手，在下次方法被调用时，from的值是上次的to值
     * @param toPosition 被越过的条目位置
     */
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    /**
     * 当条目被滑动移除后被调用。实现此方法最后需要调用[RecyclerView.Adapter.notifyItemRemoved]来通知
     * 
     * @param position 被移除的条目位置
     * @param direction 从哪个方向移除的。[ItemTouchHelper.START]，[ItemTouchHelper.END]...
     */
    fun onItemSwiped(position: Int, direction: Int)
}