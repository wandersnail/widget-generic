package com.snail.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller


/**
 * 描述: 横向滑动文本选择器
 * 时间: 2018/8/25 21:01
 * 作者: zengfansheng
 */
class ScrollCheckTab(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 获取当前选择tab位置
     */
    var checkedPosition: Int = 0
        private set
    private val tabInfoArray = SparseArray<TabInfo>()
    var builder: Builder? = null
        private set
    private var downX: Float = 0.toFloat()
    private var click: Boolean = false
    private var scroll: Boolean = false
    private val scroller= Scroller(context)
    private var isInitialized: Boolean = false //是否已绘制完成一次
    private var hasCacheCheck: Boolean = false //是否有缓存的选中任务
    private var listener: OnCheckChangeListener? = null
    private var lastEventTriggerTime: Long = 0 //事件上次触发时间

    interface ILabel {
        val label: String
    }
    
    /**
     * 获取选中的tab
     */
    val checkedTab: ILabel?
        get() = if (builder == null || builder!!.tabList == null || checkedPosition <= builder!!.tabList!!.size ||
                checkedPosition >= builder!!.tabList!!.size)
            null
        else
            builder!!.tabList!![checkedPosition]

    fun setBuidler(buidler: Builder) {
        this.builder = buidler
        tabInfoArray.clear()
        paint.textSize = builder!!.textSize.toFloat()
        paint.typeface = builder!!.typeface
        var i = 0
        val tabListSize = buidler.tabList!!.size
        while (i < tabListSize) {
            tabInfoArray.put(i, TabInfo(0f, 0f))
            i++
        }
        invalidate()
    }

    fun setOnCheckChangeListener(listener: OnCheckChangeListener) {
        this.listener = listener
    }

    /**
     * 选中指定tab
     */
    fun check(position: Int) {
        doCheck(position, false, true)
    }

    /**
     * 选中指定tab，不触发回调
     */
    fun checkNoEvent(position: Int) {
        doCheck(position, false, false)
    }

    /**
     * 选中指定tab，无动画
     */
    fun checkImmediately(position: Int) {
        doCheck(position, true, true)
    }

    /**
     * 选中指定tab，无动画，不触发回调
     */
    fun checkImmediatelyNoEvent(position: Int) {
        doCheck(position, true, false)
    }

    private fun doCheck(position: Int, immediately: Boolean, notify: Boolean) {
        if (builder != null && builder!!.tabList != null && !builder!!.tabList!!.isEmpty() && position >= 0 &&
                position < builder!!.tabList!!.size && this.checkedPosition != position) {
            val startScrollX = tabInfoArray.get(this.checkedPosition).scrollX
            val targetScrollX = tabInfoArray.get(position).scrollX
            this.checkedPosition = position
            if (isInitialized) {
                if (immediately) {
                    scrollTo(targetScrollX, 0)
                } else {
                    scroller.startScroll(startScrollX, 0, targetScrollX - startScrollX, 0, 300)
                }
                invalidate()
            } else {
                hasCacheCheck = true
            }
            if (notify && listener != null) {
                listener!!.onCheck(position, builder!!.tabList!![position])
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (builder!!.tabList != null && !builder!!.tabList!!.isEmpty()) {
            var startX = 0f
            //画文本
            var i = 0
            val tabListSize = builder!!.tabList!!.size
            while (i < tabListSize) {
                paint.color = if (i == checkedPosition) builder!!.checkColor else builder!!.normalColor
                val s = builder!!.tabList!![i].label
                val w = paint.measureText(s)
                val fontMetrics = paint.fontMetricsInt
                val baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2
                startX = if (i == 0) (width - w) / 2 else startX + builder!!.tabSpace
                val info = tabInfoArray.get(i)
                info.centerX = startX + w / 2
                info.textW = w
                if (info.scrollX == Integer.MAX_VALUE) {
                    info.scrollX = (info.centerX - width / 2).toInt()
                }
                canvas.drawText(s, startX, baseline.toFloat(), paint)
                startX += w
                i++
            }
            isInitialized = true
            if (hasCacheCheck) {
                hasCacheCheck = false
                scrollTo(tabInfoArray.get(checkedPosition).scrollX, 0)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled && builder != null && builder!!.tabList != null && !builder!!.tabList!!.isEmpty()) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.x
                    scroll = false
                    click = true
                }
                MotionEvent.ACTION_MOVE -> {
                    val distance = event.x - downX
                    //限制单次触发
                    if (Math.abs(distance) > 5 && !scroll) {
                        click = false
                        if (Math.abs(distance) > 30) {
                            scroll = true
                            if (System.currentTimeMillis() - lastEventTriggerTime >= builder!!.eventTriggerLimitMillis) {
                                lastEventTriggerTime = System.currentTimeMillis()
                                if (distance > 0) { //向右滑
                                    check(checkedPosition - 1)
                                } else { //向左滑
                                    check(checkedPosition + 1)
                                }
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> if (click) {
                    click = false
                    var position = -1
                    var i = 0
                    val arrSize = tabInfoArray.size()
                    while (i < arrSize) {
                        val info = tabInfoArray.valueAt(i)
                        if (info.contains(event.x + scrollX)) { //判断点击是否在tab范围内
                            position = i
                            break
                        }
                        i++
                    }
                    if (position != -1 && System.currentTimeMillis() - lastEventTriggerTime >= builder!!.eventTriggerLimitMillis) {
                        lastEventTriggerTime = System.currentTimeMillis()
                        check(position)
                    }
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, 0)
            invalidate()
        }
    }

    //centerX: 文本中心X坐标
    //textW: 文本宽度
    private inner class TabInfo internal constructor(internal var centerX: Float, internal var textW: Float) {
        internal var scrollX = Integer.MAX_VALUE //scrollX多少滑到这个tab

        internal operator fun contains(x: Float): Boolean {
            return x >= centerX - textW / 2 && x <= centerX + textW / 2
        }
    }

    interface OnCheckChangeListener {
        fun onCheck(position: Int, tab: ILabel)
    }

    class Builder {
        internal var tabList: List<ILabel>? = null
        internal var checkColor = -0xaf8e1a
        internal var normalColor = Color.GRAY
        internal var textSize = 30
        internal var tabSpace = 15
        internal var typeface: Typeface? = null
        internal var eventTriggerLimitMillis: Int = 0

        /**
         * 文本大小
         * @param textSize 像素
         */
        fun setTextSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        fun setTypeface(typeface: Typeface): Builder {
            this.typeface = typeface
            return this
        }

        /**
         * 设置文本颜色
         * @param normalColor 正常文本颜色
         * @param checkColor 选中文本颜色
         */
        fun setTextColor(normalColor: Int, checkColor: Int): Builder {
            this.normalColor = normalColor
            this.checkColor = checkColor
            return this
        }

        /**
         * 文本之间间距
         * @param tabSpace 单位：像素
         */
        fun setTabSpace(tabSpace: Int): Builder {
            this.tabSpace = tabSpace
            return this
        }

        fun setTabs(list: List<ILabel>): Builder {
            tabList = list
            return this
        }

        fun setEventTriggerLimitTime(millis: Int): Builder {
            eventTriggerLimitMillis = millis
            return this
        }

        fun getTabs(): List<ILabel>? {
            return tabList
        }
    }
}
