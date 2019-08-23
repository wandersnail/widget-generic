package cn.wandersnail.widget.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ListView的数据适配器
 * <p>
 * date: 2019/8/22 18:49
 * author: zengfansheng
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    private static final int TAG_KEY = 1098237452;
    protected Context context;
    private final List<T> items;

    public BaseListAdapter(@NonNull Context context) {
        Objects.requireNonNull(context, "context can't be null");
        this.context = context;
        items = new ArrayList<>();
    }

    public BaseListAdapter(@NonNull Context context, @NonNull List<T> items) {
        Objects.requireNonNull(context, "context can't be null");
        Objects.requireNonNull(items, "items can't be null");
        this.context = context;
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
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        BaseViewHolder<T> holder;
        if (view == null) {
            holder = createViewHolder(position);
            view = holder.createView();
            view.setTag(TAG_KEY);
        } else {
            holder = (BaseViewHolder<T>) view.getTag(TAG_KEY);
        }
        holder.onBind(getItem(position), position);
        return view;
    }

    protected abstract BaseViewHolder<T> createViewHolder(int position);    
}
