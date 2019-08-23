package cn.wandersnail.widget.listview;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * date: 2019/8/22 18:57
 * author: zengfansheng
 */
public interface BaseViewHolder<T> {
    void onBind(@NonNull T item, int position);
    
    @NonNull
    View createView();
}
