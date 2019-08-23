package cn.wandersnail.widget.listener;

import android.util.SparseLongArray;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 防止快速点击的
 * <p>
 * date: 2019/8/22 16:43
 * author: zengfansheng
 */
public abstract class RejectFastClickListener {
    private int minInterval = 1000;
    private SparseLongArray clickTimeArr = new SparseLongArray();

    public RejectFastClickListener(int minInterval) {
        if (minInterval > 0) {
            this.minInterval = minInterval;
        }        
    }

    protected void check(@NonNull View v, Callback callback) {
        Objects.requireNonNull(v, "v can't be null");
        long lastClickTime = clickTimeArr.get(v.getId());
        if (System.currentTimeMillis() - lastClickTime >= minInterval) {
            callback.onAccept();
            clickTimeArr.put(v.getId(), System.currentTimeMillis());
        } else {
            callback.onReject();
        }
    }
    
    public interface Callback {
        /**
         * 拒绝
         */
        void onReject();

        /**
         * 通过
         */
        void onAccept();
    }
}
