package com.snail.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Looper
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StyleRes
import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * Created by zeng on 2016/12/4.
 */

open class BaseDialog : DialogInterface {
    protected lateinit var dialog: MyDialog
    protected var window: Window? = null
    lateinit var view: View
        protected set

    val context: Context
        get() = dialog.context

    val activity: Activity?
        get() = dialog.ownerActivity

    private val isDestroyed: Boolean
        get() {
            val activity = dialog.ownerActivity
            return activity != null && (activity.isDestroyed || activity.isFinishing)
        }

    val isShowing: Boolean
        get() = dialog.isShowing

    val attributes: WindowManager.LayoutParams
        get() = window!!.attributes

    constructor(activity: Activity, view: View) {
        init(activity, view, 0)
    }

    constructor(activity: Activity, @LayoutRes redId: Int) {
        init(activity, View.inflate(activity, redId, null), 0)
    }

    constructor(activity: Activity, view: View, @StyleRes themeResId: Int) {
        init(activity, view, themeResId)
    }

    private fun init(activity: Activity, view: View, @StyleRes themeResId: Int) {
        this.view = view
        dialog = MyDialog(activity, themeResId)
        dialog.ownerActivity = activity
        window = dialog.window
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        window!!.decorView.setPadding(0, 0, 0, 0)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(view)
    }

    fun <T : View> findViewById(@IdRes id: Int): T {
        return view.findViewById(id)
    }

    open fun setCancelable(cancelable: Boolean): BaseDialog {
        dialog.setCancelable(cancelable)
        return this
    }

    open fun setOnCancelListener(listener: DialogInterface.OnCancelListener): BaseDialog {
        dialog.setOnCancelListener(listener)
        return this
    }

    open fun setOnDismissListener(listener: DialogInterface.OnDismissListener): BaseDialog {
        dialog.setOnDismissListener(listener)
        return this
    }

    open fun setCanceledOnTouchOutside(flag: Boolean): BaseDialog {
        dialog.setCanceledOnTouchOutside(flag)
        return this
    }

    open fun show(): BaseDialog {
        if (!isDestroyed && !dialog.isShowing) {
            if (Looper.getMainLooper() == Looper.myLooper()) {
                dialog.show()
            } else {
                view.post { dialog.show() }
            }
        }
        return this
    }

    override fun cancel() {
        if (!isDestroyed) {
            dialog.cancel()
        }
    }

    override fun dismiss() {
        if (!isDestroyed) {
            dialog.dismiss()
        }
    }

    /**
     * 设置返回键按下监听
     */
    open fun setOnBackPressedListener(listener: OnBackPressedListener): BaseDialog {
        dialog.setOnBackPressedListener(listener)
        return this
    }

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

    class MyDialog : Dialog {
        private var backPressedListener: OnBackPressedListener? = null

        internal constructor(context: Context) : super(context)

        internal constructor(context: Context, themeResId: Int) : super(context, themeResId)

        internal constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

        override fun onBackPressed() {
            super.onBackPressed()
            if (backPressedListener != null) {
                backPressedListener!!.onBackPressed()
            }
        }

        internal fun setOnBackPressedListener(backPressedListener: OnBackPressedListener) {
            this.backPressedListener = backPressedListener
        }
    }
}