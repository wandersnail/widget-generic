package cn.wandersnail.widget.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 基本的RecyclerView的ViewHolder
 * <p>
 * date: 2019/8/22 19:46
 * author: zengfansheng
 */
public abstract class BaseItemTouchViewHolder extends RecyclerView.ViewHolder implements ItemTouchViewHolder {
    public BaseItemTouchViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
