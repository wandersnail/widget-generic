package cn.wandersnail.widget.recyclerview;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 拖拽监听器
 * <p>
 * date: 2019/8/22 19:20
 * author: zengfansheng
 */
public interface OnStartDragListener {
    /**
     * 当开始拖拽时
     *
     * @param viewHolder 被拖拽的view的持有者
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
