package com.snail.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import java.lang.ref.WeakReference

/**
 * Created by zeng on 2016/12/4.
 */

open class BaseDialog @JvmOverloads constructor(val activity: Activity, val view: View, @StyleRes themeResId: Int = 0) {
    private var dialog = MyDialog(activity, themeResId)
    protected var window: Window? = dialog.window
    //注册的观察者
    private val observers = ArrayList<WeakReference<SimpleDialogEventObserver>>()

    val context: Context
        get() = dialog.context

    private val isDestroyed: Boolean
        get() {
            val activity = dialog.ownerActivity
            return activity != null && (activity.isDestroyed || activity.isFinishing)
        }

    val isShowing: Boolean
        get() = dialog.isShowing

    val attributes: WindowManager.LayoutParams
        get() = window!!.attributes

    constructor(activity: Activity, @LayoutRes resId: Int) : this(activity, View.inflate(activity, resId, null))

    init {
        dialog.ownerActivity = activity
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(view)
        dialog.observer = object : SimpleDialogEventObserver() {
            override fun onCreate(savedInstanceState: Bundle?) {
                this@BaseDialog.onCreate(savedInstanceState)
                getObservers().forEach { it.onCreate(savedInstanceState) }
            }

            override fun onStart() {
                this@BaseDialog.onStart()
                getObservers().forEach { it.onStart() }
            }

            override fun onShow() {
                this@BaseDialog.onShow()
                getObservers().forEach { it.onShow() }
            }

            override fun onAttachedToWindow() {
                this@BaseDialog.onAttachedToWindow()
                getObservers().forEach { it.onAttachedToWindow() }
            }

            override fun onStop() {
                this@BaseDialog.onStop()
                getObservers().forEach { it.onStop() }
            }

            override fun onBackPressed() {
                this@BaseDialog.onBackPressed()
                getObservers().forEach { it.onBackPressed() }
            }

            override fun onCancel() {
                this@BaseDialog.onCancel()
                getObservers().forEach { it.onCancel() }
            }

            override fun onDismiss() {
                this@BaseDialog.onDismiss()
                getObservers().forEach { it.onDismiss() }
            }

            override fun onDetachedFromWindow() {
                this@BaseDialog.onDetachedFromWindow()
                getObservers().forEach { it.onDetachedFromWindow() }
            }
        }
    }

    private fun getObservers(): Array<SimpleDialogEventObserver> {
        synchronized(observers) {
            val obs = java.util.ArrayList<SimpleDialogEventObserver>()
            val iterator = observers.iterator()
            while (iterator.hasNext()) {
                val observer = iterator.next().get()
                if (observer == null) {
                    iterator.remove()
                } else {
                    obs.add(observer)
                }
            }
            return obs.toTypedArray()
        }
    }

    /**
     * 将观察者添加到注册集合里
     *
     * @param observer 需要注册的观察者
     */
    fun registerEventObserver(observer: SimpleDialogEventObserver) {
        synchronized(observers) {
            observers.forEach {
                if (it.get() == observer) {
                    return
                }
            }
            observers.add(WeakReference(observer))
        }
    }

    /**
     * 将观察者从注册集合里移除
     *
     * @param observer 需要取消注册的观察者
     */
    fun unregisterEventObserver(observer: SimpleDialogEventObserver) {
        synchronized(observers) {
            val iterator = observers.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().get() == observer) {
                    iterator.remove()
                    return
                }
            }
        }
    }

    /**
     * 将所有观察者从注册集合中移除
     */
    fun unregisterEventAll() {
        synchronized(observers) {
            observers.clear()
        }
    }
    
    fun <T : View> findViewById(@IdRes id: Int): T? {
        return view.findViewById(id)
    }

    open fun setCancelable(cancelable: Boolean): BaseDialog {
        dialog.setCancelable(cancelable)
        return this
    }

    open fun setCanceledOnTouchOutside(flag: Boolean): BaseDialog {
        dialog.setCanceledOnTouchOutside(flag)
        return this
    }

    fun show(): BaseDialog {
        if (!isDestroyed && !dialog.isShowing) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                dialog.show()
            } else {
                view.post { dialog.show() }
            }
        }
        return this
    }

    fun cancel() {
        if (!isDestroyed) {
            dialog.cancel()
        }
    }

    fun dismiss() {
        if (!isDestroyed) {
            dialog.dismiss()
        }
    }

    open fun onCreate(savedInstanceState: Bundle?) {}

    open fun onStart() {}

    open fun onStop() {}

    open fun onShow() {}

    open fun onDismiss() {}

    open fun onCancel() {}

    open fun onBackPressed() {}

    open fun onAttachedToWindow() {}

    open fun onDetachedFromWindow() {}

    open fun setPadding(left: Int, top: Int, right: Int, bottom: Int): BaseDialog {
        window!!.decorView.setPadding(left, top, right, bottom)
        return this
    }

    open fun setGravity(g: Int): BaseDialog {
        window!!.setGravity(g)
        return this
    }

    open fun setAnimation(rid: Int): BaseDialog {
        window!!.setWindowAnimations(rid)
        return this
    }

    /**
     * 设置偏移
     */
    open fun setOffset(x: Int, y: Int): BaseDialog {
        val lp = window!!.attributes
        lp.x = x
        lp.y = y
        window!!.attributes = lp
        return this
    }

    /**
     * 对话框外区域暗度
     */
    open fun setDimAmount(amount: Float): BaseDialog {
        window!!.setDimAmount(amount)
        return this
    }

    open fun setSize(width: Int, height: Int): BaseDialog {
        val lp = window!!.attributes
        lp.width = width
        lp.height = height
        window!!.attributes = lp
        return this
    }

    /**
     * 旋转
     */
    open fun setRotation(rotation: Float): BaseDialog {
        view.rotation = rotation
        return this
    }

    /**
     * 设置窗口类型，如[WindowManager.LayoutParams.TYPE_TOAST]设置一个window级别与toast相同的弹窗
     * @param type 窗口类型
     */
    open fun setWindowType(type: Int): BaseDialog {
        val lp = window!!.attributes
        lp.type = type
        window!!.attributes = lp
        return this
    }
        
    private class MyDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {
        var observer: SimpleDialogEventObserver? = null

        init {
            setOnCancelListener { observer?.onCancel() }
            setOnDismissListener { observer?.onDismiss() }
            setOnShowListener { observer?.onShow() }
        }
        
        override fun onBackPressed() {
            super.onBackPressed()
            observer?.onBackPressed()
        }

        override fun onAttachedToWindow() {
            observer?.onAttachedToWindow()
        }

        override fun onDetachedFromWindow() {
            observer?.onDetachedFromWindow()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            observer?.onCreate(savedInstanceState)
        }

        override fun onStart() {
            super.onStart()
            observer?.onStart()
        }

        override fun onStop() {
            super.onStop()
            observer?.onStop()
        }
    }
}
