package com.snail.widget.dialog

import android.os.Bundle

/**
 * BaseDialog的事件观察者
 *
 * date: 2019/5/5 20:24
 * author: zengfansheng
 */
open class SimpleDialogEventObserver : DialogEventObserver {
    override fun onContentChanged() {}

    override fun onWindowFocusChanged(hasFocus: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {}

    override fun onStart() {}

    override fun onStop() {}

    override fun onShow() {}

    override fun onDismiss() {}

    override fun onCancel() {}

    override fun onBackPressed() {}

    override fun onAttachedToWindow() {}

    override fun onDetachedFromWindow() {}
}