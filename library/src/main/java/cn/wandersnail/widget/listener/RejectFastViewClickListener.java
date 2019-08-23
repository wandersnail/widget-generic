package cn.wandersnail.widget.listener;

import android.view.View;

import androidx.annotation.NonNull;

/**
 * date: 2019/8/22 18:16
 * author: zengfansheng
 */
public class RejectFastViewClickListener extends RejectFastClickListener implements View.OnClickListener {
    private RejectableViewClickCallback callback;

    public RejectFastViewClickListener(int minInterval, @NonNull RejectableViewClickCallback callback) {
        super(minInterval);
        this.callback = callback;
    }

    public RejectFastViewClickListener(@NonNull RejectableViewClickCallback callback) {
        super(1000);
        this.callback = callback;
    }

    @Override
    public void onClick(View view) {
        check(view, new Callback() {
            @Override
            public void onReject() {
                callback.onReject(view);
            }

            @Override
            public void onAccept() {
                callback.onAccept(view);
            }
        });
    }
}
