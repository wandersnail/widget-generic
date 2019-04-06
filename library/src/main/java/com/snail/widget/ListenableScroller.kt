package com.snail.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.animation.Interpolator
import android.widget.OverScroller
import java.lang.ref.WeakReference

/**
 * 带滑动回调监听滚动器
 *
 * date: 2019/4/5 22:55
 * author: zengfansheng
 */
class ListenableScroller @JvmOverloads 
constructor(context: Context, interpolator: Interpolator? = null) : OverScroller(context, interpolator) {
    var scrollListener: OnScrollListener? = null    
    private val handler = ScrollEventHandler(this, Looper.getMainLooper())
    private var flingMode = false
    
    interface OnScrollListener {
        fun onScroll(scroller: ListenableScroller)
        
        fun onScrollFinish(scroller: ListenableScroller)
        
        fun onFlingFinish(scroller: ListenableScroller)
    }

    override fun fling(startX: Int, startY: Int, velocityX: Int, velocityY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int) {
        super.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
        flingMode = true
        handler.sendEmptyMessage(0)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy)
        flingMode = false
        handler.sendEmptyMessage(0)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, duration)
        flingMode = false
        handler.sendEmptyMessage(0)
    }
    
    private class ScrollEventHandler(scroller: ListenableScroller, looper: Looper) : Handler(looper) {
        private var weakRef: WeakReference<ListenableScroller>? = null
        private var lastX = 0
        private var lastY = 0
        
        init {
            weakRef = WeakReference(scroller)
        }

        override fun handleMessage(msg: Message?) {
            val scroller = weakRef?.get()
            if (scroller != null) {                
                if (scroller.computeScrollOffset()) {
                    if (lastX != scroller.currX || lastY != scroller.currY) {
                        scroller.scrollListener?.onScroll(scroller)
                        lastX = scroller.currX
                        lastY = scroller.currY
                    }                   
                }
                if (scroller.isFinished) {
                    if (scroller.flingMode) {
                        scroller.scrollListener?.onFlingFinish(scroller)
                    } else {
                        scroller.scrollListener?.onScrollFinish(scroller)
                    }
                } else {
                    sendEmptyMessageDelayed(0, 10)
                }
            }
        }
    }
}