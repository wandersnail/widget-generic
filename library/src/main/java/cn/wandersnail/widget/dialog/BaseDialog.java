package cn.wandersnail.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * date: 2019/8/22 14:12
 * author: zengfansheng
 */
public class BaseDialog<T extends BaseDialog> {
    protected Dialog dialog;
    private final List<DialogEventObserver> observers = new ArrayList<>();
    protected Window window;
    protected View view;
    
    public BaseDialog(@NonNull Activity activity, @NonNull View view) {
        this(activity, view, 0);
    }

    public BaseDialog(@NonNull Activity activity, @LayoutRes int layoutResId) {
        this(activity, layoutResId, 0);
    }
    
    public BaseDialog(@NonNull Activity activity, @LayoutRes int layoutResId, @StyleRes int themeResId) {
        this(activity, View.inflate(activity, layoutResId, null), themeResId);
    }
    
    public BaseDialog(@NonNull Activity activity, @NonNull View view, @StyleRes int themeResId) {
        this.view = view;
        dialog = new MyDialog(new DialogEventObserver() {
            @Override
            public void onCreate(Bundle savedInstanceState) {
                BaseDialog.this.onCreate(savedInstanceState);
                for (DialogEventObserver observer : getObservers()) {
                    observer.onCreate(savedInstanceState);
                }
            }

            @Override
            public void onStart() {
                BaseDialog.this.onStart();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onStart();
                }
            }

            @Override
            public void onStop() {
                BaseDialog.this.onStop();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onStop();
                }
            }

            @Override
            public void onShow() {
                BaseDialog.this.onDetachedFromWindow();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onDetachedFromWindow();
                }
            }

            @Override
            public void onDismiss() {
                BaseDialog.this.onDismiss();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onDismiss();
                }
            }

            @Override
            public void onCancel() {
                BaseDialog.this.onCancel();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onCancel();
                }
            }

            @Override
            public void onBackPressed() {
                BaseDialog.this.onBackPressed();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onBackPressed();
                }
            }

            @Override
            public void onAttachedToWindow() {
                BaseDialog.this.onAttachedToWindow();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onAttachedToWindow();
                }
            }

            @Override
            public void onDetachedFromWindow() {
                BaseDialog.this.onDetachedFromWindow();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onDetachedFromWindow();
                }
            }

            @Override
            public void onContentChanged() {
                BaseDialog.this.onContentChanged();
                for (DialogEventObserver observer : getObservers()) {
                    observer.onContentChanged();
                }
            }

            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                BaseDialog.this.onWindowFocusChanged(hasFocus);
                for (DialogEventObserver observer : getObservers()) {
                    observer.onWindowFocusChanged(hasFocus);
                }
            }
        }, activity, themeResId);
        window = dialog.getWindow();
        dialog.setOwnerActivity(activity);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
    }    
    
    public Context getContext() {
        return dialog.getContext();
    }
    
    public Activity getActivity() {
        return dialog.getOwnerActivity();
    }
    
    private boolean isActive() {
        Activity activity = dialog.getOwnerActivity();
        return activity != null && !activity.isDestroyed() && !activity.isFinishing();
    }

    private List<DialogEventObserver> getObservers() {
        synchronized(observers) {
            List<DialogEventObserver> obs = new ArrayList<>();
            Iterator<DialogEventObserver> iterator = observers.iterator();
            while (iterator.hasNext()) {
                DialogEventObserver observer = iterator.next();
                if (observer == null) {
                    iterator.remove();
                } else {
                    obs.add(observer);
                }
            }
            return obs;
        }
    }

    /**
     * 将观察者添加到注册集合里
     *
     * @param observer 需要注册的观察者
     */
    public void registerEventObserver(DialogEventObserver observer) {
        synchronized(observers) {
            for (DialogEventObserver eventObserver : observers) {
                if (eventObserver == observer) {
                    return;
                }
            }
            observers.add(observer);
        }
    }

    /**
     * 将观察者从注册集合里移除
     *
     * @param observer 需要取消注册的观察者
     */
    public void unregisterEventObserver(DialogEventObserver observer) {
        synchronized(observers) {
            Iterator<DialogEventObserver> iterator = observers.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == observer) {
                    iterator.remove();
                    return;
                }
            }
        }
    }

    /**
     * 将所有观察者从注册集合中移除
     */
    public void unregisterEventAll() {
        synchronized(observers) {
            observers.clear();
        }
    }
    
    public boolean isShowing() {
        return dialog.isShowing();
    }
    
    public WindowManager.LayoutParams getAttributes() {
        return window.getAttributes();
    }

    public T setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return (T) this;
    }
    
    public T setCanceledOnTouchOutside(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
        return (T) this;
    }
    
    @CallSuper
    public void cancel() {
        if (isActive()) {
            dialog.cancel();
        }
    }
    
    @CallSuper
    public void dismiss() {
        if (isActive()) {
            dialog.dismiss();
        }
    }
    
    @CallSuper
    public T show() {
        if (isActive() && !dialog.isShowing()) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                dialog.show();
            } else {
                view.post(() -> dialog.show());
            }
        }
        return (T) this;
    }
    
    public T setPadding(int left, int top, int right, int bottom) {
        window.getDecorView().setPadding(left, top, right, bottom);
        return (T) this;
    }

    public T setGravity(int gravity) {
        window.setGravity(gravity);
        return (T) this;
    }
    
    public T setAnimation(@StyleRes int resId) {
        window.setWindowAnimations(resId);
        return (T) this;
    }

    public void onCreate(Bundle savedInstanceState) {}

    public void onStart() {}

    public void onStop() {}

    public void onShow() {}

    public void onDismiss() {}

    public void onCancel() {}

    public void onBackPressed() {}

    public void onAttachedToWindow() {}

    public void onDetachedFromWindow() {}

    public void onContentChanged() {}

    public void onWindowFocusChanged(boolean hasFocus) {}
    
    /**
     * 设置偏移
     */
    public T setOffset(int x, int y) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = x;
        params.y = y;
        window.setAttributes(params);
        return (T) this;
    }
    
    /**
     * 对话框外区域暗度
     */
    public T setDimAmount(float amount) {
        window.setDimAmount(amount);
        return (T) this;
    }

    public T setSize(int width, int height) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        window.setAttributes(params);
        return (T) this;
    }
    
    /**
     * 旋转
     */
    public T setRotation(float rotation) {
        view.setRotation(rotation);
        return (T) this;
    }
    
    /**
     * 设置窗口类型，如[WindowManager.LayoutParams.TYPE_TOAST]设置一个window级别与toast相同的弹窗
     * @param type 窗口类型
     */
    public T setWindowType(int type) {
        WindowManager.LayoutParams params = window.getAttributes();
        params.type = type;
        window.setAttributes(params);
        return (T) this;
    }
    
    private static class MyDialog extends Dialog {
        private final DialogEventObserver observer;

        MyDialog(DialogEventObserver observer, @NonNull Context context, int themeResId) {
            super(context, themeResId);
            this.observer = observer;
            setOnCancelListener(dialog -> observer.onCancel());
            setOnDismissListener(dialog -> observer.onDismiss());
            setOnShowListener(dialog -> observer.onShow());
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            observer.onBackPressed();
        }

        @Override
        public void onAttachedToWindow() {
            observer.onAttachedToWindow();
        }

        @Override
        public void onDetachedFromWindow() {
            observer.onDetachedFromWindow();
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            observer.onCreate(savedInstanceState);
        }

        @Override
        protected void onStart() {
            super.onStart();
            observer.onStart();
        }

        @Override
        protected void onStop() {
            super.onStop();
            observer.onStop();
        }

        @Override
        public void onContentChanged() {
            observer.onContentChanged();
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            observer.onWindowFocusChanged(hasFocus);
        }
    }
}
