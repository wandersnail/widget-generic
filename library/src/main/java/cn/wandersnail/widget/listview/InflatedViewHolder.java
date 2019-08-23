package cn.wandersnail.widget.listview;

import android.content.Context;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * 根据布局ID自动填充
 * <p>
 * date: 2019/8/22 19:08
 * author: zengfansheng
 */
public abstract class InflatedViewHolder<T> implements BaseViewHolder<T> {
    private final View rootView;

    public InflatedViewHolder(@NonNull Context context, @LayoutRes int resId) {
        rootView = View.inflate(context, resId, null);
    }

    @NonNull
    @Override
    public View createView() {
        return rootView;
    }

    public <V extends View> V getView(@IdRes int resId) {
        return rootView.findViewById(resId);
    }
}
