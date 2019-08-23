package cn.wandersnail.widget.listener;

import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;

/**
 * date: 2019/8/22 16:56
 * author: zengfansheng
 */
public class RejectFastItemClickListener extends RejectFastClickListener implements AdapterView.OnItemClickListener {
    private RejectableItemClickCallback callback;
    
    public RejectFastItemClickListener(int minInterval, @NonNull RejectableItemClickCallback callback) {
        super(minInterval);
        this.callback = callback;
    }

    public RejectFastItemClickListener(@NonNull RejectableItemClickCallback callback) {
        super(1000);
        this.callback = callback;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        check(view, new Callback() {
            @Override
            public void onReject() {
                callback.onReject(parent, view, position, id);
            }

            @Override
            public void onAccept() {
                callback.onAccept(parent, view, position, id);
            }
        });
    }
}
