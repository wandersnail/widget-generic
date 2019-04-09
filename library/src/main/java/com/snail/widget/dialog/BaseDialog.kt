package com.snail.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes

/**
 * Created by zeng on 2016/12/4.
 */

open class BaseDialog @JvmOverloads constructor(val activity: Activity, val view: View, @StyleRes themeResId: Int = 0) {
    private var dialog = MyDialog(activity, themeResId)
    protected var window: Window? = dialog.window
    var onCancelListener: DialogInterface.OnCancelListener? = null
    var onDismissListener: DialogInterface.OnDismissListener? = null
    /** 返回键按下监听 */
    var onBackPressedListener: OnBackPressedListener? = null

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
        dialog.setOnCancelListener {
            onCancel()
            onCancelListener?.onCancel(dialog)
        }
        dialog.setOnDismissListener { 
            onDismiss()
            onDismissListener?.onDismiss(dialog)
        }
        dialog.setOnShowListener { 
            onShow()
        }
        dialog.onBackPressedListener = object : OnBackPressedListener {
            override fun onBackPressed() {
                this@BaseDialog.onBackPressed()
                onBackPressedListener?.onBackPressed()
            }
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

    interface OnBackPressedListener {
        fun onBackPressed()
    }
    
    internal interface WindowCallback {
        fun onAttachedToWindow()

        fun onDetachedFromWindow()
    }
    
    class MyDialog : Dialog {
        internal var onBackPressedListener: OnBackPressedListener? = null
        internal var windowCallback: WindowCallback? = null

        internal constructor(context: Context) : super(context)

        internal constructor(context: Context, themeResId: Int) : super(context, themeResId)

        internal constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

        override fun onBackPressed() {
            super.onBackPressed()
            onBackPressedListener?.onBackPressed()
        }

        override fun onAttachedToWindow() {
            windowCallback?.onAttachedToWindow()
        }

        override fun onDetachedFromWindow() {
            windowCallback?.onDetachedFromWindow()
        }
    }
}
