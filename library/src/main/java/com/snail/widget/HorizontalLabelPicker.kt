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
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.OverScroller


/**
 * 描述: 横向滑动文本选择器
 * 时间: 2018/8/25 21:01
 * 作者: zengfansheng
 */
class HorizontalLabelPicker(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    /**
     * 获取当前选择label位置
     */
    var checkedPosition: Int = 0
        private set
    private val labelInfoArray = SparseArray<LabelInfo>()
    private var builder: Builder? = null
    private var downX: Float = 0.toFloat()
    private var click: Boolean = false
    private var scroll: Boolean = false
    private var scroller: OverScroller? = null
    private var isInitialized: Boolean = false //是否已绘制完成一次
    private var hasCacheCheck: Boolean = false //是否有缓存的选中任务
    private var listener: OnCheckChangeListener? = null
    private var lastEventTriggerTime: Long = 0 //事件上次触发时间

    interface ILabel {
        fun getText(): String
    }
    
    /**
     * 获取选中的label
     */
    val checkedLabel: ILabel?
        get() = if (builder == null || builder!!.labels == null || checkedPosition <= builder!!.labels!!.size ||
                checkedPosition >= builder!!.labels!!.size) {
            null
        } else {
            builder!!.labels!![checkedPosition]
        }

    fun setBuidler(builder: Builder) {
        this.builder = builder
        labelInfoArray.clear()
        paint.textSize = builder.textSize.toFloat()
        paint.typeface = builder.typeface
        var i = 0
        val labelListSize = builder.labels?.size ?: 0
        while (i < labelListSize) {
            labelInfoArray.put(i, LabelInfo(0f, 0f))
            i++
        }
        scroller = OverScroller(context, builder.interpolator)
        invalidate()
    }
    
    fun getLabels(): List<ILabel>? {
        return builder?.getLabels()
    }

    fun setOnCheckChangeListener(listener: OnCheckChangeListener?) {
        this.listener = listener
    }

    /**
     * 选中指定label
     */
    fun check(position: Int) {
        doCheck(position, immediately = false, notify = true)
    }

    /**
     * 选中指定label，不触发回调
     */
    fun checkNoEvent(position: Int) {
        doCheck(position, immediately = false, notify = false)
    }

    /**
     * 选中指定label，无动画
     */
    fun checkImmediately(position: Int) {
        doCheck(position, immediately = true, notify = true)
    }

    /**
     * 选中指定label，无动画，不触发回调
     */
    fun checkImmediatelyNoEvent(position: Int) {
        doCheck(position, immediately = true, notify = false)
    }

    /**
     * @return 返回第一个和给定的标签文本对上的条目位置，未找到时返回-1
     */
    fun firstPositionOf(label: String): Int {
        return if (builder == null || builder!!.labels == null || builder!!.labels!!.isEmpty()) {
            -1
        } else {
            builder!!.labels!!.indexOfFirst { it.getText() == label }
        }
    }
    
    /**
     * 选中指定label
     */
    fun check(label: String) {
        val position = firstPositionOf(label)
        if (position != -1) {
            doCheck(position, immediately = false, notify = true)
        }
    }

    /**
     * 选中指定label，不触发回调
     */
    fun checkNoEvent(label: String) {
        val position = firstPositionOf(label)
        if (position != -1) {
            doCheck(position, immediately = false, notify = false)
        }
    }

    /**
     * 选中指定label，无动画
     */
    fun checkImmediately(label: String) {
        val position = firstPositionOf(label)
        if (position != -1) {
            doCheck(position, immediately = true, notify = true)
        }
    }

    /**
     * 选中指定label，无动画，不触发回调
     */
    fun checkImmediatelyNoEvent(label: String) {
        val position = firstPositionOf(label)
        if (position != -1) {
            doCheck(position, immediately = true, notify = false)
        }
    }

    private fun doCheck(position: Int, immediately: Boolean, notify: Boolean) {
        if (builder != null && builder!!.labels != null && !builder!!.labels!!.isEmpty() && position >= 0 &&
                position < builder!!.labels!!.size && this.checkedPosition != position) {
            val startScrollX = labelInfoArray.get(this.checkedPosition).scrollX
            val targetScrollX = labelInfoArray.get(position).scrollX
            this.checkedPosition = position
            if (isInitialized) {
                if (immediately) {
                    scrollTo(targetScrollX, 0)
                } else {
                    scroller?.startScroll(startScrollX, 0, targetScrollX - startScrollX, 0, 300)
                }
                invalidate()
            } else {
                hasCacheCheck = true
            }
            if (notify && listener != null) {
                listener!!.onCheck(position, builder!!.labels!![position])
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (builder!!.labels != null && !builder!!.labels!!.isEmpty()) {
            var startX = 0f
            //画文本
            val labelListSize = builder!!.labels!!.size
            for (i in 0 until labelListSize) {
                paint.color = if (i == checkedPosition) builder!!.checkColor else builder!!.normalColor
                val s = builder!!.labels!![i].getText()
                val w = paint.measureText(s)
                val fontMetrics = paint.fontMetricsInt
                val baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2
                startX = if (i == 0) (width - w) / 2 else startX + builder!!.labelSpace
                val info = labelInfoArray.get(i)
                info.centerX = startX + w / 2
                info.textW = w
                if (info.scrollX == Integer.MAX_VALUE) {
                    info.scrollX = (info.centerX - width / 2).toInt()
                }
                canvas.drawText(s, startX, baseline.toFloat(), paint)
                startX += w
            }
            isInitialized = true
            if (hasCacheCheck) {
                hasCacheCheck = false
                scrollTo(labelInfoArray.get(checkedPosition).scrollX, 0)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled && builder != null && builder!!.labels != null && !builder!!.labels!!.isEmpty()) {
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
                    val arrSize = labelInfoArray.size()
                    while (i < arrSize) {
                        val info = labelInfoArray.valueAt(i)
                        if (info.contains(event.x + scrollX)) { //判断点击是否在label范围内
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
        if (scroller?.computeScrollOffset() == true) {
            scrollTo(scroller!!.currX, 0)
            invalidate()
        }
    }

    //centerX: 文本中心X坐标
    //textW: 文本宽度
    private inner class LabelInfo internal constructor(internal var centerX: Float, internal var textW: Float) {
        internal var scrollX = Integer.MAX_VALUE //scrollX多少滑到这个label

        internal operator fun contains(x: Float): Boolean {
            return x >= centerX - textW / 2 && x <= centerX + textW / 2
        }
    }

    interface OnCheckChangeListener {
        fun onCheck(position: Int, label: ILabel)
    }

    class Builder {
        internal var labels: List<ILabel>? = null
        internal var checkColor = -0xaf8e1a
        internal var normalColor = Color.GRAY
        internal var textSize = 30
        internal var labelSpace = 15
        internal var typeface: Typeface? = null
        internal var eventTriggerLimitMillis: Int = 0
        internal var interpolator: Interpolator = AccelerateDecelerateInterpolator()
        
        /**
         * 设置滑动动画插值器
         */
        fun setInterpolator(interpolator: Interpolator): Builder {
            this.interpolator = interpolator
            return this
        }
        
        /**
         * 文本大小
         * @param textSize 像素
         */
        fun setTextSize(textSize: Int): Builder {
            this.textSize = textSize
            return this
        }

        /**
         * 设置字体
         */
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
         * @param labelSpace 单位：像素
         */
        fun setLabelSpace(labelSpace: Int): Builder {
            this.labelSpace = labelSpace
            return this
        }

        /**
         * 设置标签
         */
        fun setLabels(list: List<ILabel>): Builder {
            labels = list
            return this
        }

        /**
         * 设置事件触发时间间隔限制
         */
        fun setEventTriggerLimitTime(millis: Int): Builder {
            eventTriggerLimitMillis = millis
            return this
        }

        fun getLabels(): List<ILabel>? {
            return labels
        }
    }
}
