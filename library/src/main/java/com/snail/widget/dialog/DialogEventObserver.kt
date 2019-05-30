package com.snail.widget.dialog

import android.os.Bundle

/**
 *
 *
 * date: 2019/5/6 14:26
 * author: zengfansheng
 */
interface DialogEventObserver {
    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onStop()

    fun onShow()

    fun onDismiss()

    fun onCancel()

    fun onBackPressed()

    fun onAttachedToWindow()

    fun onDetachedFromWindow()

    fun onContentChanged()

    fun onWindowFocusChanged(hasFocus: Boolean)
}