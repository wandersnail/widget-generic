package com.snail.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

/**
 *
 *
 * date: 2018/12/30 15:22
 * author: zengfansheng
 */
internal object Utils {
    /**
     * @param normal   正常时的Drawable
     * @param pressed  按压时的Drawable
     * @param selected 被选中时的Drawable
     * @param disabled 不可用时的Drawable
     */
    fun createStateListDrawable(normal: Drawable, pressed: Drawable, selected: Drawable, disabled: Drawable): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(-android.R.attr.state_enabled), disabled)
        drawable.addState(intArrayOf(android.R.attr.state_pressed), pressed)
        drawable.addState(intArrayOf(android.R.attr.state_selected), selected)
        drawable.addState(intArrayOf(), normal) //normal一定要最后
        return drawable
    }

    /**
     * 创建带边框背景
     * @param fillColor    背景色
     * @param frameWidth   边框亮度
     * @param frameColor   边框颜色
     * @param cornerRadius 圆角
     */
    fun createDrawable(fillColor: Int, frameWidth: Int, frameColor: Int, cornerRadius: Int): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.cornerRadius = cornerRadius.toFloat()
        drawable.setColor(fillColor)
        drawable.setStroke(frameWidth, frameColor)
        return drawable
    }

    /**
     * @param color       背景色
     * @param cornerRadii 圆角大小
     * @param leftTop     左上是否圆角
     * @param rightTop    右上是否圆角
     * @param leftBottom  左下是否圆角
     * @param rightBottom 右下是否圆角
     */
    fun createDrawable(color: Int, cornerRadii: Float, leftTop: Boolean, rightTop: Boolean, leftBottom: Boolean, rightBottom: Boolean): Drawable {
        val drawable = GradientDrawable()
        drawable.cornerRadii = floatArrayOf(if (leftTop) cornerRadii else 0f, if (leftTop) cornerRadii else 0f, if (rightTop) cornerRadii else 0f,
            if (rightTop) cornerRadii else 0f, if (rightBottom) cornerRadii else 0f, if (rightBottom) cornerRadii else 0f,
            if (leftBottom) cornerRadii else 0f, if (leftBottom) cornerRadii else 0f)
        drawable.setColor(color)
        return drawable
    }

    /**
     * @param normal  正常时的颜色
     * @param pressed 按压时的颜色
     * @param selected 选中时的颜色
     * @param disabled 不可用时的颜色
     */
    fun createColorStateList(normal: Int, pressed: Int, selected: Int, disabled: Int): ColorStateList {
        //normal一定要最后
        val states = arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled), intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled), intArrayOf())
        return ColorStateList(states, intArrayOf(disabled, pressed, selected, normal))
    }

    /**
     * 获取显示屏幕宽度，不包含状态栏和导航栏
     */
    fun getDisplayScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取显示屏幕高度
     */
    fun getDisplayScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2pxF(context: Context, dpValue: Float): Float {
        return dpValue * context.resources.displayMetrics.density + 0.5f
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dpF(context: Context, pxValue: Float): Float {
        return pxValue / context.resources.displayMetrics.density + 0.5f
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        return (pxValue / context.resources.displayMetrics.density + 0.5f).toInt()
    }

    /**
     * @param cornerRadii 圆角大小，dp
     */
    fun createDrawableSelecor(normal: Int, pressed: Int, cornerRadii: Float, leftTop: Boolean, rightTop: Boolean,
                              leftBottom: Boolean, rightBottom: Boolean): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_pressed), createDrawable(pressed, cornerRadii, leftTop, rightTop, leftBottom, rightBottom))
        drawable.addState(intArrayOf(), createDrawable(normal, cornerRadii, leftTop, rightTop, leftBottom, rightBottom))
        return drawable
    }
}