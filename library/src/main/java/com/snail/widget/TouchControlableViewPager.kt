package com.snail.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.Scroller

/**
 * Created by zengfs on 2016/2/2.
 * 可控制是否能滑动
 */
class TouchControlableViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    private var isTouchEnabled = true

    /**
     * 设置ViewPager是否可滑动
     */
    fun setTouchEnabled(enable: Boolean) {
        isTouchEnabled = enable
    }

    /**
     * 是否禁用其事件，实现不能滑动
     */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isTouchEnabled && super.onTouchEvent(ev)
    }

    /**
     * 是否截触摸事件，让事件可以向内层传递
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isTouchEnabled && super.onInterceptTouchEvent(ev)
    }

    /**设置切换动画时长 */
    fun setScrollDuration(duration: Int) {
        try {
            val field = ViewPager::class.java.getDeclaredField("mScroller")
            field.isAccessible = true
            val viewPagerScroller = ViewPagerScroller(context, OvershootInterpolator(0.6f))
            field.set(this, viewPagerScroller)
            viewPagerScroller.duration = duration
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 控制ViewPager动画切换时长
     */
    inner class ViewPagerScroller @JvmOverloads constructor(context: Context, interpolator: Interpolator? = null) : Scroller(context, interpolator) {
        internal var duration: Int = 0

        fun setDuration(duration: Int) {
            this.duration = duration
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
            super.startScroll(startX, startY, dx, dy, duration)
        }

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, this.duration)
        }
    }
}
