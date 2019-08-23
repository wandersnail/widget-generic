package cn.wandersnail.widget.recyclerview;

/**
 * 可拖拽ViewHolder
 * <p>
 * date: 2019/8/22 19:20
 * author: zengfansheng
 */
public interface ItemTouchViewHolder {
    /**
     * 条目被滑动，此时可改变view的状态，和未被滑动的条目区分
     */
    void onSwipe();

    /**
     * 条目被拖拽，此时可改变view的状态，和未被拖拽的条目区分
     */
    void onDrag();

    /**
     * 条目被放开，此时应还原view的状态
     */
    void onClear();
}
