package cn.wandersnail.widget.listener;

import android.view.View;

/**
 * date: 2019/8/22 16:43
 * author: zengfansheng
 */
public interface RejectableViewClickCallback {
    default void onReject(View view) {}

    void onAccept(View view);
}
