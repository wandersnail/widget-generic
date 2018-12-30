package com.snail.widget

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.AdapterView

/**
 * 描述: 下拉刷新，控制在顶部才拦截触摸事件
 * 时间: 2018/6/8 09:20
 * 作者: zengfansheng
 */
class PullRefreshLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SwipeRefreshLayout(context, attrs) {
    private var lv: AdapterView<*>? = null

    private val isListViewNotAtTop: Boolean
        get() {
            if (lv != null) {
                val child = lv!!.getChildAt(0)
                return child != null && (lv!!.firstVisiblePosition != 0 || child.top != 0)
            }
            return true
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 0) {
            val view = getChildAt(0)
            if (view is AdapterView<*>) {
                lv = view
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return isListViewNotAtTop && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return isListViewNotAtTop && super.onInterceptTouchEvent(ev)
    }
}
