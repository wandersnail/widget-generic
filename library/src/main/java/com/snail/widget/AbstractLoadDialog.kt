package com.snail.widget

import android.app.Activity
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.view.View

/**
 * 描述: 加载对话框抽象类
 * 时间: 2018/5/17 00:31
 * 作者: zengfansheng
 */
abstract class AbstractLoadDialog @JvmOverloads constructor(activity: Activity, view: View, themeResId: Int = 0) : BaseDialog(activity, view, themeResId) {
    /**
     * 是否自动消失
     */
    var autoDismiss = true
    /**
     * 自动消失延时时间
     */
    var autoDismissDelayMillis = 1500
    protected var handler: Handler = Handler(Looper.getMainLooper())

    private var dismissRunnable: Runnable = Runnable { dismiss() }

    constructor(activity: Activity, redId: Int) : this(activity, View.inflate(activity, redId, null))

    init {
        this.setOnDismissListener(DialogInterface.OnDismissListener {
            handler.removeCallbacks(dismissRunnable)
        })
    }

    fun updateState(state: Int) {
        if (state < 0 || state > 2) {
            return
        }
        handler.removeCallbacks(dismissRunnable)
        handleUpdateState(state)
        if (state != LOADING && autoDismiss) {
            handler.postDelayed(dismissRunnable, autoDismissDelayMillis.toLong())
        }
    }

    /**
     * 处理更新状态
     * @param state 当前要更新到的状态
     */
    protected abstract fun handleUpdateState(state: Int)

    companion object {
        const val LOADING = 0
        const val LOAD_SUCCESS = 1
        const val LOAD_FAILE = 2
    }
}
