package com.snail.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * 描述: 加载对话框抽象类
 * 时间: 2018/5/17 00:31
 * 作者: zengfansheng
 */
public abstract class AbstractLoadDialog extends BaseDialog {
    public static final int LOADING = 0;
    public static final int LOAD_SUCCESS = 1;
    public static final int LOAD_FAILE = 2;
    
    protected boolean autoDismiss = true;
    protected int autoDismissDelayMillis = 1500;
    protected Handler handler;

    public AbstractLoadDialog(@NonNull Activity activity, int redId) {
        this(activity, View.inflate(activity, redId, null));
    }

    public AbstractLoadDialog(@NonNull Activity activity, @NonNull View view) {
        this(activity, view, 0);
    }

    public AbstractLoadDialog(@NonNull Activity activity, @NonNull View view, int themeResId) {
        super(activity, view, themeResId);
        handler = new Handler(Looper.getMainLooper());
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(dismissRunnable);
            }
        });
    }
    
    protected Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };
    
    public void updateState(int state) {
        if (state < 0 || state > 2) {
            return;
        }
        handler.removeCallbacks(dismissRunnable);
        handleUpdateState(state);
        if (state != LOADING && autoDismiss) {
            handler.postDelayed(dismissRunnable, autoDismissDelayMillis);
        }
    }

    /**
     * 处理更新状态
     * @param state 当前要更新到的状态
     */
    protected abstract void handleUpdateState(int state);

    /**
     * 是否自动消失
     */
    public void setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }

    /**
     * 自动消失延时时间
     * @param autoDismissDelayMillis 毫秒
     */
    public void setAutoDismissDelayMillis(int autoDismissDelayMillis) {
        this.autoDismissDelayMillis = autoDismissDelayMillis;
    }
}
