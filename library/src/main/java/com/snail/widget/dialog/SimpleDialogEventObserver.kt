package com.snail.widget.dialog

import android.os.Bundle

/**
 * BaseDialog的事件观察者
 *
 * date: 2019/5/5 20:24
 * author: zengfansheng
 */
open class SimpleDialogEventObserver {
    open fun onCreate(savedInstanceState: Bundle?) {}

    open fun onStart() {}

    open fun onStop() {}

    open fun onShow() {}

    open fun onDismiss() {}

    open fun onCancel() {}

    open fun onBackPressed() {}

    open fun onAttachedToWindow() {}

    open fun onDetachedFromWindow() {}
}