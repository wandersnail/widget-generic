package com.snail.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 时间: 2017/6/27 11:17
 * 作者: 曾繁盛
 * 邮箱: 43068145@qq.com
 * 功能: 可自定义长按事件的长按时长的按钮
 */

class LongPressView @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var mLastMotionX: Int = 0
    private var mLastMotionY: Int = 0
    private var longPressDuration: Long = 2000 //长按事件触发时长
    private val longPressRunnable = LongPressRunnable()
    private var isMoved: Boolean = false

    /**
     * @param duration 长按事件触发时长
     */
    fun setOnLongPressDuration(duration: Long) {
        longPressDuration = duration
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mLastMotionX = x
                    mLastMotionY = y
                    isMoved = false
                    postDelayed(longPressRunnable, longPressDuration)
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!isMoved) {
                        if (Math.abs(mLastMotionX - x) > TOUCH_SLOP || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                            //移动超过阈值，则表示移动了  
                            isMoved = true
                            removeCallbacks(longPressRunnable)
                        }
                    }
                }
                MotionEvent.ACTION_UP //释放了				  
                -> removeCallbacks(longPressRunnable)
            }
            return true
        }
        return super.dispatchTouchEvent(event)
    }

    private inner class LongPressRunnable : Runnable {
        override fun run() {
            performLongClick()
        }
    }

    companion object {
        private const val TOUCH_SLOP = 100 //移动的阈值
    }
}
