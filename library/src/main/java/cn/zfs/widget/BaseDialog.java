package cn.zfs.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by zeng on 2016/12/4.
 */

public class BaseDialog implements DialogInterface {
    protected MyDialog dialog;
    protected Window window;
    protected View contentView;

    public BaseDialog(@NonNull Activity activity, @NonNull View view) {
        init(activity, view, 0);
    }

    public BaseDialog(@NonNull Activity activity, @LayoutRes int redId) {
        init(activity, View.inflate(activity, redId, null), 0);
    }

    public BaseDialog(@NonNull Activity context, @NonNull View view, @StyleRes int themeResId) {
        init(context, view, themeResId);
    }

    private void init(@NonNull Activity activity, @NonNull View view, @StyleRes int themeResId) {
        contentView = view;        
        dialog = new MyDialog(activity, themeResId);
        dialog.setOwnerActivity(activity);
        window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
    }

    public View getContentView() {
        return contentView;
    }
    
    public Context getContext() {
        return dialog.getContext();
    }

    public Activity getActivity() {
        return dialog.getOwnerActivity();
    }

    public <T extends View> T findViewById(@IdRes int id) {
        return contentView.findViewById(id);
    }

    public BaseDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    public BaseDialog setOnCancelListener(OnCancelListener listener) {
        dialog.setOnCancelListener(listener);
        return this;
    }

    public BaseDialog setOnDismissListener(OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
        return this;
    }

    public BaseDialog setCanceledOnTouchOutside(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
        return this;
    }

    public BaseDialog show() {
        if (!isDestroyed() && !dialog.isShowing()) {
            if (Looper.getMainLooper().equals(Looper.myLooper())) {
                dialog.show();
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.show();
                    }
                });
            }
        }
        return this;
    }

    @Override
    public void cancel() {
        if (!isDestroyed()) {
            dialog.cancel();
        }
    }
    
    @Override
    public void dismiss() {
        if (!isDestroyed()) {
            dialog.dismiss();
        }        
    }

    /**
     * 设置返回键按下监听
     */
    public void setOnBackPressedListener(OnBackPressedListener listener) {
        dialog.setOnBackPressedListener(listener);
    }
    
    private boolean isDestroyed() {
        Activity activity = dialog.getOwnerActivity();
        return activity != null && (activity.isDestroyed() || activity.isFinishing());
    }
            
    public boolean isShowing() {
        return dialog.isShowing();
    }

    public BaseDialog setPadding(int left, int top, int right, int bottom) {
        window.getDecorView().setPadding(left, top, right, bottom);
        return this;
    }

    public BaseDialog setGravity(int g) {
        window.setGravity(g);
        return this;
    }

    public BaseDialog setAnimation(int rid) {
        window.setWindowAnimations(rid);
        return this;
    }

    /**
     * 设置偏移
     */
    public BaseDialog setOffset(int x, int y) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = x;
        lp.y = y;
        window.setAttributes(lp);
        return this;
    }

    /**
     * 对话框外区域暗度
     */
    public BaseDialog setDimAmount(float amount) {
        window.setDimAmount(amount);
        return this;
    }
        
    public BaseDialog setSize(int width, int height) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = width;
        lp.height = height;
        window.setAttributes(lp);
        return this;
    }

    /**
     * 旋转
     */
    public void setRotation(float rotation) {
        if (contentView != null) {
            contentView.setRotation(rotation);
        }
    }
    
    /**
     * 设置窗口类型，如{@link WindowManager.LayoutParams#TYPE_TOAST}设置一个window级别与toast相同的弹窗
     * @param type 窗口类型
     */
    public BaseDialog setWindowType(int type) {
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.type = type;
        window.setAttributes(lp);
        return this;
    }
    
    public WindowManager.LayoutParams getAttributes() {
        return window.getAttributes();
    }
    
    public interface OnBackPressedListener {
        void onBackPressed();
    }
    
    public static class MyDialog extends Dialog {
        private OnBackPressedListener backPressedListener;
        
        MyDialog(@NonNull Context context) {
            super(context);
        }

        MyDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        MyDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            if (backPressedListener != null) {
                backPressedListener.onBackPressed();
            }
        }

        void setOnBackPressedListener(OnBackPressedListener backPressedListener) {
            this.backPressedListener = backPressedListener;
        }
    }
}
