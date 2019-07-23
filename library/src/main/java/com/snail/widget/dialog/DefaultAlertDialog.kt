package com.snail.widget.dialog

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.snail.widget.R
import com.snail.widget.Utils
import kotlin.math.min


/**
 * 描述: 默认提示对话框
 * 时间: 2018/5/15 08:43
 * 作者: zengfansheng
 */
open class DefaultAlertDialog(activity: Activity) : BaseDialog(activity, R.layout.dialog_default_alert), View.OnClickListener {
    private var tvTitle: TextView = view.findViewById(R.id.tvTitle)
    private var tvMsg: TextView = view.findViewById(R.id.tvMsg)
    private var tvSubMsg: TextView = view.findViewById(R.id.tvSubMsg)
    private var tvNegative: TextView = view.findViewById(R.id.tvNegative)
    private var verticalDivider: View = view.findViewById(R.id.verticalDivider)
    private var horizontalDivider: View = view.findViewById(R.id.horizontalDivider)
    private var titleDivider: View = view.findViewById(R.id.titleDivider)
    private var layoutText: LinearLayout = view.findViewById(R.id.layoutText)
    private var layoutContent: FrameLayout = view.findViewById(R.id.layoutContent)
    private var tvPositive: TextView = view.findViewById(R.id.tvPositive)
    private var negativeListener: View.OnClickListener? = null
    private var positiveListener: View.OnClickListener? = null
    private var autoDismiss: Boolean = false
    private var autoDismissDelayMillis = 1500
    private var handler = Handler(Looper.getMainLooper())
    private var negativeVisible: Boolean = false //消极按钮，左
    private var positiveVisible: Boolean = false //积极按钮，右
    private var cornerRadii: Int = 0
    private var backColor = Color.WHITE //对话框背景色
    private var titleBackColor = -0x22070708 //标题栏背景色
    private var clickDismiss = true

    private val dismissRunnable = Runnable { dismiss() }

    private fun initViews() {
        tvNegative.setOnClickListener(this)
        tvPositive.setOnClickListener(this)
        cornerRadii = Utils.dp2pxF(context, 8f).toInt()
        setBackgroundColor(backColor)
        setTitleBackgroundColor(titleBackColor)
        updateButtonVisible()
    }

    init {
        initViews()
        val width = (min(Utils.getDisplayScreenWidth(context), Utils.getDisplayScreenHeight(context)) * 0.8).toInt()
        this.setSize(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onShow() {
        if (autoDismiss) {
            handler.removeCallbacks(dismissRunnable)
            handler.postDelayed(dismissRunnable, autoDismissDelayMillis.toLong())
        }
    }

    override fun onDismiss() {
        handler.removeCallbacks(dismissRunnable)
    }
    
    override fun onClick(v: View) {
        if (clickDismiss) {
            dismiss()
        }
        if (tvNegative == v) {
            negativeListener?.onClick(v)
        } else if (tvPositive == v) {
            positiveListener?.onClick(v)
        }
    }

    private fun updateButtonVisible() {
        tvPositive.visibility = if (positiveVisible) View.VISIBLE else View.GONE
        tvNegative.visibility = if (negativeVisible) View.VISIBLE else View.GONE
        horizontalDivider.visibility = if (negativeVisible || positiveVisible) View.VISIBLE else View.GONE
        verticalDivider.visibility = if (negativeVisible && positiveVisible) View.VISIBLE else View.GONE
        updateButtonBackground()
    }

    private fun updateButtonBackground() {
        if (negativeVisible && positiveVisible) {
            tvNegative.background = Utils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii.toFloat(), 
                    leftTop = false, rightTop = false, leftBottom = true, rightBottom = false)
            tvPositive.background = Utils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii.toFloat(), 
                    leftTop = false, rightTop = false, leftBottom = false, rightBottom = true)
        } else if (negativeVisible) {
            tvNegative.background = Utils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii.toFloat(),
                    leftTop = false, rightTop = false, leftBottom = true, rightBottom = true)
        } else if (positiveVisible) {
            tvPositive.background = Utils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii.toFloat(),
                    leftTop = false, rightTop = false, leftBottom = true, rightBottom = true)
        }
    }

    override fun setCancelable(cancelable: Boolean): DefaultAlertDialog {
        super.setCancelable(cancelable)
        return this
    }

    override fun setCanceledOnTouchOutside(flag: Boolean): DefaultAlertDialog {
        super.setCanceledOnTouchOutside(flag)
        return this
    }

    /**
     * 设置对话框的圆角大小
     * @param cornerRadii 圆角大小
     */
    fun setCornerRadii(cornerRadii: Int): DefaultAlertDialog {
        this.cornerRadii = cornerRadii
        setBackgroundColor(backColor)
        setTitleBackgroundColor(titleBackColor)
        updateButtonBackground()
        return this
    }

    /**
     * 设置点击按键后自动dismiss对话框
     */
    fun setClickDismiss(clickDismiss: Boolean): DefaultAlertDialog {
        this.clickDismiss = clickDismiss
        return this
    }

    fun setBackgroundColor(@ColorInt color: Int): DefaultAlertDialog {
        backColor = color
        view.background = Utils.createDrawable(color, cornerRadii.toFloat(),
                leftTop = true, rightTop = true, leftBottom = true, rightBottom = true)
        return this
    }

    /**
     * 是否自动消失
     */
    fun setAutoDismiss(autoDismiss: Boolean): DefaultAlertDialog {
        this.autoDismiss = autoDismiss
        return this
    }

    /**
     * 自动消失延时时间
     * @param autoDismissDelayMillis 毫秒
     */
    fun setAutoDismissDelayMillis(autoDismissDelayMillis: Int): DefaultAlertDialog {
        this.autoDismissDelayMillis = autoDismissDelayMillis
        return this
    }

    fun setTitle(text: CharSequence?): DefaultAlertDialog {
        tvTitle.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        titleDivider.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        tvTitle.text = text
        return this
    }

    fun setTitle(resId: Int): DefaultAlertDialog {
        tvTitle.visibility = if (resId > 0) View.VISIBLE else View.GONE
        titleDivider.visibility = if (resId > 0) View.VISIBLE else View.GONE
        tvTitle.setText(resId)
        return this
    }

    fun setTitleTextColor(@ColorInt color: Int): DefaultAlertDialog {
        tvTitle.setTextColor(color)
        return this
    }

    fun setTitleTypeface(tf: Typeface?): DefaultAlertDialog {
        tvTitle.typeface = tf
        return this
    }

    fun setTitleTextSize(unit: Int, size: Float): DefaultAlertDialog {
        tvTitle.setTextSize(unit, size)
        return this
    }

    fun setTitleBackgroundColor(@ColorInt color: Int): DefaultAlertDialog {
        titleBackColor = color
        tvTitle.background = Utils.createDrawable(color, cornerRadii.toFloat(),
                    leftTop = true, rightTop = true, leftBottom = false, rightBottom = false)
        return this
    }

    fun setTitleDividerColor(@ColorInt color: Int): DefaultAlertDialog {
        titleDivider.setBackgroundColor(color)
        return this
    }

    /**
     * 设置标题分割线高度
     * @param height 高度，像素
     */
    fun setTitleDividerHeight(height: Int): DefaultAlertDialog {
        val params = titleDivider.layoutParams
        params.height = height
        titleDivider.layoutParams = params
        return this
    }

    fun setContentViewPadding(left: Int, top: Int, right: Int, bottom: Int): DefaultAlertDialog {
        layoutContent.setPadding(left, top, right, bottom)
        return this
    }

    fun setContentView(view: View): DefaultAlertDialog {
        layoutContent.removeAllViews()
        layoutContent.addView(view)
        return this
    }

    fun setContentView(view: View, params: ViewGroup.LayoutParams): DefaultAlertDialog {
        layoutContent.removeAllViews()
        layoutContent.addView(view, params)
        return this
    }

    fun setMessage(text: CharSequence?): DefaultAlertDialog {
        tvMsg.text = text
        return this
    }

    fun setMessage(@StringRes resId: Int): DefaultAlertDialog {
        tvMsg.setText(resId)
        return this
    }

    fun setMessageTextColor(@ColorInt color: Int): DefaultAlertDialog {
        tvMsg.setTextColor(color)
        return this
    }

    fun setMessageTypeface(tf: Typeface?): DefaultAlertDialog {
        tvMsg.typeface = tf
        return this
    }

    fun setMessageTextSize(unit: Int, size: Float): DefaultAlertDialog {
        tvMsg.setTextSize(unit, size)
        return this
    }

    fun setSubMessage(text: CharSequence?): DefaultAlertDialog {
        tvSubMsg.visibility = if (TextUtils.isEmpty(text)) View.GONE else View.VISIBLE
        tvSubMsg.text = text
        return this
    }

    fun setSubMessage(resId: Int): DefaultAlertDialog {
        tvSubMsg.visibility = if (resId > 0) View.VISIBLE else View.GONE
        tvSubMsg.setText(resId)
        return this
    }

    fun setSubMessageTextColor(@ColorInt color: Int): DefaultAlertDialog {
        tvSubMsg.setTextColor(color)
        return this
    }

    fun setSubMessageTypeface(tf: Typeface?): DefaultAlertDialog {
        tvSubMsg.typeface = tf
        return this
    }

    fun setSubMessageTextSize(unit: Int, size: Float): DefaultAlertDialog {
        tvSubMsg.setTextSize(unit, size)
        return this
    }

    fun setTextGravity(gravity: Int): DefaultAlertDialog {
        layoutText.gravity = gravity
        return this
    }

    fun setNegativeButtonTypeface(tf: Typeface?): DefaultAlertDialog {
        tvNegative.typeface = tf
        return this
    }

    fun setNegativeButton(@StringRes resId: Int, listener: View.OnClickListener?): DefaultAlertDialog {
        negativeVisible = true
        tvNegative.setText(resId)
        negativeListener = listener
        updateButtonVisible()
        return this
    }

    fun setNegativeButton(text: CharSequence?, listener: View.OnClickListener?): DefaultAlertDialog {
        negativeVisible = true
        tvNegative.text = text
        negativeListener = listener
        updateButtonVisible()
        return this
    }

    fun setNegativeButtonTextColor(@ColorInt color: Int): DefaultAlertDialog {
        tvNegative.setTextColor(color)
        return this
    }

    fun setNegativeButtonTextColor(normal: Int, pressed: Int): DefaultAlertDialog {
        tvNegative.setTextColor(getColorStateList(normal, pressed))
        return this
    }

    fun setNegativeButtonTextColor(normal: Int, pressed: Int, disabled: Int): DefaultAlertDialog {
        tvNegative.setTextColor(getColorStateList(normal, pressed, disabled))
        return this
    }

    fun setNegativeButtonTextSize(unit: Int, size: Float): DefaultAlertDialog {
        tvNegative.setTextSize(unit, size)
        return this
    }

    fun setPositiveButton(text: CharSequence?, listener: View.OnClickListener?): DefaultAlertDialog {
        positiveVisible = true
        tvPositive.text = text
        positiveListener = listener
        updateButtonVisible()
        return this
    }

    fun setPositiveButton(@StringRes resId: Int, listener: View.OnClickListener?): DefaultAlertDialog {
        positiveVisible = true
        tvPositive.setText(resId)
        positiveListener = listener
        updateButtonVisible()
        return this
    }

    fun setPositiveButtonTextColor(@ColorInt color: Int): DefaultAlertDialog {
        tvPositive.setTextColor(color)
        return this
    }

    fun setPositiveButtonTextColor(normal: Int, pressed: Int): DefaultAlertDialog {
        tvPositive.setTextColor(getColorStateList(normal, pressed))
        return this
    }

    fun setsetPositiveButtonTextColor(normal: Int, pressed: Int, disabled: Int): DefaultAlertDialog {
        tvPositive.setTextColor(getColorStateList(normal, pressed, disabled))
        return this
    }

    fun setPositiveButtonTextSize(unit: Int, size: Float): DefaultAlertDialog {
        tvPositive.setTextSize(unit, size)
        return this
    }

    fun setPositiveButtonTypeface(tf: Typeface?): DefaultAlertDialog {
        tvPositive.typeface = tf
        return this
    }

    private fun getColorStateList(normal: Int, pressed: Int): ColorStateList {
        val states = arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf())
        return ColorStateList(states, intArrayOf(pressed, normal))
    }

    private fun getColorStateList(normal: Int, pressed: Int, disabled: Int): ColorStateList {
        val states = arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf(-android.R.attr.state_enabled), intArrayOf())
        return ColorStateList(states, intArrayOf(pressed, disabled, normal))
    }
}
