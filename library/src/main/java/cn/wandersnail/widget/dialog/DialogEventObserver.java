package cn.wandersnail.widget.dialog;

import android.os.Bundle;

/**
 * date: 2019/8/22 14:10
 * author: zengfansheng
 */
public interface DialogEventObserver {
    default void onCreate(Bundle savedInstanceState) {
    }

    default void onStart() {
    }

    default void onStop() {
    }

    default void onShow() {
    }

    default void onDismiss() {
    }

    default void onCancel() {
    }

    default void onBackPressed() {
    }

    default void onAttachedToWindow() {
    }

    default void onDetachedFromWindow() {
    }

    default void onContentChanged() {
    }

    default void onWindowFocusChanged(boolean hasFocus) {
    }
}
