package cn.wandersnail.widget.recyclerview;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 可拖拽适配器
 * <p>
 * date: 2019/8/22 19:16
 * author: zengfansheng
 */
public interface ItemTouchAdapter {
    /**
     * 当条目被拖拽完全离开另一个条目时被调用。实现此方法最后需要调用{@link RecyclerView.Adapter#notifyItemMoved}来通知
     *
     * @param from 从哪个条目移动来。在越过一个条目后，即使未放手，在下次方法被调用时，from的值是上次的to值
     * @param to   被越过的条目位置
     */
    boolean onItemMove(int from, int to);

    /**
     * 当条目被滑动移除后被调用。实现此方法最后需要调用{@link RecyclerView.Adapter#notifyItemMoved}来通知
     *
     * @param position  被移除的条目位置
     * @param direction 从哪个方向移除的。{@link ItemTouchHelper#START}...
     */
    void onItemSwiped(int position, int direction);
}
