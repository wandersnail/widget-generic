package cn.wandersnail.widget.recyclerview;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * date: 2019/8/22 19:22
 * author: zengfansheng
 */
public abstract class BaseItemTouchAdapter<T, VH extends BaseItemTouchViewHolder> extends RecyclerView.Adapter<VH> implements ItemTouchAdapter {
    private final List<T> items;

    public BaseItemTouchAdapter() {
        items = new ArrayList<>();
    }

    public BaseItemTouchAdapter(@NonNull List<T> items) {
        Objects.requireNonNull(items, "items can't be null");
        this.items = items;
    }

    /**
     * 获取适配器数据
     */
    @NonNull
    public List<T> getItems() {
        return items;
    }

    /**
     * 设置适配器数据，不会更新界面
     */
    public void setItems(@NonNull List<T> items) {
        Objects.requireNonNull(items, "items can't be null");
        this.items.clear();
        this.items.addAll(items);
    }

    /**
     * 刷新适配器数据，同时更新界面
     */
    public void refresh(@NonNull List<T> items) {
        setItems(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public boolean onItemMove(int from, int to) {
        Collections.swap(items, from, to);
        notifyItemMoved(from, to);
        return true;
    }

    @Override
    public void onItemSwiped(int position, int direction) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindViewHolder(holder, items.get(position), position);
    }

    protected abstract void onBindViewHolder(@NonNull VH holder, @NonNull T item, int position);
}
