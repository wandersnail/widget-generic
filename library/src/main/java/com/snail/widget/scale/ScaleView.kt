package com.snail.widget.scale

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.snail.widget.ListenableScroller
import com.snail.widget.R
import com.snail.widget.Utils

/**
 * Created by zeng on 2016/8/21
 */
class ScaleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr), ListenableScroller.OnScrollListener {
    private var min = 0
    private var max = 0
    private var bigStepScaleNum = 0//隔多少个短刻度一个长刻度
    private var twoBigStepDifValue = 0f//两个长刻度之间值的大小
    private var labelColor = 0
    private var indicatorColor = 0
    private var indicatorPostion = 0
    private var labelSize = 0f//标签字体大小
    private var scaleSpace = 0//刻度之间间隔
    private var labelAndScaleSpace = 0//标签与长刻度的间隔
    private var longScaleLen = 0
    private var scaleWidth = 0//刻度线条宽度
    private var indicatorWidth = 0//指示器宽
    private var shortLongtScaleRatio = 0f//短刻度与长刻度比例，短/长
    private var orientation = 0
    private var isEdgeDim = true//两端边缘模糊
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var updateCallback: OnValueUpdateCallback? = null
    private var textFormatterCallback: TextFormatterCallback? = null
    private var labelHeight = 0
    private var value = 0f
    private var start = 0
    private var contentLen = 0
    private var scales = 0
    private var totalValue = 0f
    private var gestureDetector: GestureDetector? = null
    private var scroller = ListenableScroller(context)
    private var onFling = false

    private val scrollOffset: Int
        get() = if (orientation == HORIZONTAL) scrollX else scrollY

    override fun onScroll(scroller: ListenableScroller) {
        doMove(null)
    }

    override fun onScrollFinish(scroller: ListenableScroller) {
        
    }

    override fun onFlingFinish(scroller: ListenableScroller) {
        autoScroll()
    }

    interface OnValueUpdateCallback {
        fun onValueUpdate(value: Float)
    }

    /**标签文本格式 */
    interface TextFormatterCallback {
        fun format(value: Float): String
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView)
        min = typedArray.getInt(R.styleable.ScaleView_wswMin, 0)
        max = typedArray.getInt(R.styleable.ScaleView_wswMax, 100)
        bigStepScaleNum = typedArray.getInt(R.styleable.ScaleView_wswBigStepScaleNum, 5)
        twoBigStepDifValue = typedArray.getFloat(R.styleable.ScaleView_wswTwoBigStepDifValue, 5f)
        labelColor = typedArray.getColor(R.styleable.ScaleView_wswLabelColor, -0x676768)
        indicatorColor = typedArray.getColor(R.styleable.ScaleView_wswIndicatorColor, -0xe6bb)
        indicatorPostion = typedArray.getInt(R.styleable.ScaleView_wswIndicatorPostion, 50)
        labelSize = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswLabelSize, Utils.dp2px(context, 14f)).toFloat()
        scaleSpace = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswScaleSpace, Utils.dp2px(context, 8f))
        labelAndScaleSpace = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswLabelAndScaleSpace, Utils.dp2px(context, 20f))
        longScaleLen = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswLongScaleLen, Utils.dp2px(context, 30f))
        scaleWidth = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswScaleWidth, Utils.dp2px(context, 1f))
        indicatorWidth = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswIndicatorWidth, Utils.dp2px(context, 3f))
        shortLongtScaleRatio = typedArray.getFloat(R.styleable.ScaleView_wswShortLongScaleRatio, 2f / 3)
        isEdgeDim = typedArray.getBoolean(R.styleable.ScaleView_wswEdgeDim, true)
        value = typedArray.getFloat(R.styleable.ScaleView_wswValue, min.toFloat())
        orientation = typedArray.getInt(R.styleable.ScaleView_wswOrientation, HORIZONTAL)
        typedArray.recycle()
        init()
        updateParams()
        setValue(value)
    }

    private fun init() {
        paint.textSize = labelSize
        val text = "0.00"
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        labelHeight = rect.bottom - rect.top
        scroller = ListenableScroller(context)
        scroller.scrollListener = this
    }

    private fun updateParams() {
        scales = ((max - min) / (twoBigStepDifValue / bigStepScaleNum)).toInt()
        //刻度部分长度
        contentLen = scales * scaleSpace
        totalValue = (max - min).toFloat()
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                scroller.fling(scrollX, scrollY, (-velocityX).toInt(), (-velocityY).toInt(), -contentLen, contentLen, -contentLen, contentLen)
                onFling = true
                return true
            }
        })
        updateScroll()
    }

    /** 设置取值范围  */
    fun setScope(min: Int, max: Int) {
        this.min = min
        this.max = max
        updateParams()
        invalidate()
    }

    /** 隔多少个短刻度一个长刻度  */
    fun setBigStepScaleNum(bigStepScaleNum: Int) {
        this.bigStepScaleNum = bigStepScaleNum
        updateParams()
        invalidate()
    }

    /** 两个长刻度之间值的大小  */
    fun setTwoBigStepDifValue(twoBigStepDifValue: Float) {
        this.twoBigStepDifValue = twoBigStepDifValue
        updateParams()
        invalidate()
    }

    /** 方向  */
    fun setOrientation(orientation: Int) {
        this.orientation = orientation
        updateParams()
        invalidate()
    }

    /** 标签颜色  */
    fun setLabelColor(labelColor: Int) {
        this.labelColor = labelColor
    }

    /** 指示器颜色  */
    fun setIndicatorColor(indicatorColor: Int) {
        this.indicatorColor = indicatorColor
    }

    /** 标签字体大小  */
    fun setLabelSize(labelSize: Int) {
        this.labelSize = labelSize.toFloat()
    }

    /** 刻度之间间隔  */
    fun setScaleSpace(scaleSpace: Int) {
        this.scaleSpace = scaleSpace
        updateParams()
        invalidate()
    }

    /** 刻度与标签之间间隔  */
    fun setLabelAndScaleSpace(labelAndScaleSpace: Int) {
        this.labelAndScaleSpace = labelAndScaleSpace
    }

    /** 长刻度尺寸  */
    fun setLongScaleLen(longScaleLen: Int) {
        this.longScaleLen = longScaleLen
    }

    /** 刻度线条宽度  */
    fun setScaleWidth(scaleWidth: Int) {
        this.scaleWidth = scaleWidth
    }

    /** 指示器宽  */
    fun setIndicatorWidth(indicatorWidth: Int) {
        this.indicatorWidth = indicatorWidth
    }

    /** 短刻度与长刻度比例，短/长  */
    fun setShortLongtScaleRatio(shortLongtScaleRatio: Float) {
        this.shortLongtScaleRatio = shortLongtScaleRatio
    }

    /** 两端是否边缘模糊  */
    fun setEdgeDim(isEdgeDim: Boolean) {
        this.isEdgeDim = isEdgeDim
    }

    fun setValue(value: Float) {
        var v = value
        if (v < min) {
            v = min.toFloat()
        } else if (v > max) {
            v = max.toFloat()
        }
        this.value = v
        updateCallback?.onValueUpdate(value)
        updateScroll()
    }

    /**
     * 设置指示器位置，即取哪个位置的值
     * @param indicatorPostion 取值位置占View的百分比。水平方向从左到右：0~100；垂直方向从上到下0~100
     */
    fun setIndicatorPosition(indicatorPostion: Int) {
        var postion = indicatorPostion
        if (postion < 0) {
            postion = 0
        } else if (postion > 100) {
            postion = 100
        }
        this.indicatorPostion = postion
        updateParams()
        invalidate()
    }

    fun setOnValueUpdateCallback(callback: OnValueUpdateCallback) {
        updateCallback = callback
    }

    fun setLabelFormatter(callback: TextFormatterCallback) {
        textFormatterCallback = callback
    }

    fun getValue(): Float {
        return value
    }

    private fun getLabel(value: Float): String {
        return if (textFormatterCallback != null) {
            textFormatterCallback!!.format(value)
        } else value.toString()
    }
    
    private fun autoScroll() {
        //算最小单位
        val unit = totalValue / scales
        //求余
        val remainder = value % unit
        val targetValue = if (Math.abs(remainder) > unit / 2) {//过半了，滚动到下一个
            value + unit - remainder
        } else {
            value - remainder
        }        
        val dx = if (orientation == HORIZONTAL) getScrollByValue(targetValue) - scrollX else 0
        val dy = if (orientation == HORIZONTAL) 0 else getScrollByValue(targetValue) - scrollY
        scroller.startScroll(scrollX, scrollY, dx, dy)
    }
    
    private fun getScrollByValue(value: Float): Int {
        return if (orientation == HORIZONTAL) {
            ((value - min) / totalValue * contentLen).toInt()
        } else {
            (contentLen - (value - min) / totalValue * contentLen).toInt()
        }
    }

    //数值改变会影响滚动的位置，需要更新
    private fun updateScroll() {
        scrollToTarget(getScrollByValue(value))
    }

    override fun onDraw(canvas: Canvas) {
        //不画标签部分长度
        val blank: Float
        if (orientation == HORIZONTAL) {
            blank = width * indicatorPostion / 100f
            val extraScales = (blank / scaleSpace).toInt()
            val startX = blank - extraScales * scaleSpace
            val extra = extraScales % bigStepScaleNum
            //刻度
            paint.strokeWidth = scaleWidth.toFloat()
            paint.color = labelColor
            for (i in 0..scales + extraScales * 2) {
                val scaleX = startX + i * scaleSpace
                if (isEdgeDim) {
                    val alpha = (parabola(width * 5 / 12f, width / 2f + scrollX - scaleX) * 255).toInt()
                    paint.alpha = alpha
                }
                if ((i - extra) % bigStepScaleNum == 0) {
                    canvas.drawLine(scaleX, 0f, scaleX, longScaleLen.toFloat(), paint)
                    if ((i - extraScales) / bigStepScaleNum % 2 == 0 && i >= extraScales && i <= scales + extraScales) {
                        //标签
                        val label = getLabel(min + (i - extraScales) / bigStepScaleNum * twoBigStepDifValue)
                        val textWidth = paint.measureText(label)
                        canvas.drawText(label, scaleX - textWidth / 2, (longScaleLen + labelAndScaleSpace + labelHeight).toFloat(), paint)
                    }
                } else {
                    canvas.drawLine(scaleX, 0f, scaleX, longScaleLen * shortLongtScaleRatio, paint)
                }
            }
            //指示线
            paint.alpha = 255
            paint.strokeWidth = indicatorWidth.toFloat()
            paint.color = indicatorColor
            canvas.drawLine(scrollX + blank, 0f, scrollX + blank, (labelAndScaleSpace / 2 + longScaleLen).toFloat(), paint)
        } else {
            blank = height * indicatorPostion / 100f
            val extraScales = (blank / scaleSpace).toInt()
            val startY = blank - extraScales * scaleSpace
            val extra = extraScales % bigStepScaleNum
            //刻度
            paint.strokeWidth = scaleWidth.toFloat()
            paint.color = labelColor
            for (i in 0..scales + extraScales * 2) {
                val scaleY = contentLen + blank * 2 - (startY + i * scaleSpace)
                if (isEdgeDim) {
                    val alpha = (parabola(height * 5 / 12f, height / 2f + scrollY - scaleY) * 255).toInt()
                    paint.alpha = alpha
                }
                if ((i - extra) % bigStepScaleNum == 0) {
                    canvas.drawLine((width - longScaleLen).toFloat(), scaleY, width.toFloat(), scaleY, paint)
                    if ((i - extraScales) / bigStepScaleNum % 2 == 0 && i >= extraScales && i <= scales + extraScales) {
                        //标签
                        val label = getLabel(min + (i - extraScales) / bigStepScaleNum * twoBigStepDifValue)
                        val textWidth = paint.measureText(label)
                        canvas.drawText(label, width.toFloat() - labelAndScaleSpace.toFloat() - longScaleLen.toFloat() - textWidth, scaleY + labelHeight / 2, paint)
                    }
                } else {
                    canvas.drawLine(width - longScaleLen * shortLongtScaleRatio, scaleY, width.toFloat(), scaleY, paint)
                }
            }
            //指示线
            paint.alpha = 255
            paint.strokeWidth = indicatorWidth.toFloat()
            paint.color = indicatorColor
            canvas.drawLine((width - labelAndScaleSpace / 2 - longScaleLen).toFloat(), scrollY + blank, width.toFloat(), scrollY + blank, paint)
        }
    }

    /**
     * 抛物线
     * @param zero 零点坐标
     * @param x 偏移量
     */
    private fun parabola(zero: Float, x: Float): Float {
        val f = (1 - Math.pow((x / zero).toDouble(), 2.0)).toFloat()
        return if (f < 0) 0f else f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {        
        if (event.action == MotionEvent.ACTION_DOWN) {
            onFling = false
            if (!scroller.isFinished) {
                scroller.abortAnimation()
            }
            start = getPosition(event)
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            doMove(event)
        }
        gestureDetector!!.onTouchEvent(event)
        if (!onFling && event.action == MotionEvent.ACTION_UP) {//抬起
            //如果没对齐刻度，自动滚过去
            autoScroll()
        }
        return true
    }

    private fun doMove(event: MotionEvent?) {
        var scroll: Int
        if (event == null) {
            scroll = if (orientation == HORIZONTAL) {
                scroller.currX
            } else {
                scroller.currY
            }
        } else {
            val move = getPosition(event)
            scroll = scrollOffset + start - move
            start = move
        }

        if (scroll < 0) {
            scroll = 0
        } else if (scroll > contentLen) {
            scroll = contentLen
        }
        scrollToTarget(scroll)
        invalidate()
        //计算当前指向的值
        value = if (orientation == HORIZONTAL) {
            min + scroll * totalValue / contentLen
        } else {
            max - scroll * totalValue / contentLen
        }
        updateCallback?.onValueUpdate(value)
    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            doMove(null)
        }
    }

    private fun scrollToTarget(scroll: Int) {
        if (orientation == HORIZONTAL) {
            scrollTo(scroll, 0)
        } else {
            scrollTo(0, scroll)
        }
    }

    private fun getPosition(event: MotionEvent): Int {
        return if (orientation == HORIZONTAL) event.x.toInt() else event.y.toInt()
    }

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }
}
