package com.snail.widget

import android.content.Context
import android.graphics.*
import androidx.annotation.DrawableRes
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 描述: 刻度尺选值器
 * 时间: 2018/8/26 14:59
 * 作者: zengfansheng
 */
class ScaleValuePicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var labelHeight: Int = 0
    private var value: Float = 0.toFloat()
    private var scales: Int = 0
    var builder: Builder? = null
        set(builder) {
            field = builder
            value = builder!!.min
            scales = ((builder.max - builder.min) / (builder.twoBigStepDifValue / 10)).toInt()
            paint.typeface = builder.typeface
            paint.textSize = builder.labelSize.toFloat()
            val text = "0.00"
            val rect = Rect()
            paint.getTextBounds(text, 0, text.length, rect)
            labelHeight = rect.bottom - rect.top
            val bitmap = BitmapFactory.decodeResource(resources, builder.sliderResId)
            if (bitmap != null) {
                val width = bitmap.width
                val matrix = Matrix()
                val scaleWidth = builder.sliderWidth / width
                matrix.postScale(scaleWidth, scaleWidth)
                sliderBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, bitmap.height, matrix, true)
                bitmap.recycle()
            }
            currentX = builder.sliderWidth / 2
            invalidate()
        }
    private var sliderBitmap: Bitmap? = null
    private val bitmapRectF = RectF()
    private var canMove: Boolean = false
    private var currentX: Float = 0.toFloat()
    private var updateCallback: OnValueUpdateCallback? = null
    private var isInitialized: Boolean = false //是否已获得控件大小
    private var hasCacheValue: Boolean = false

    interface OnValueUpdateCallback {
        fun onValueUpdate(value: Float)
    }

    /**
     * 数值变化回调
     */
    fun setOnValueUpdateCallback(updateCallback: OnValueUpdateCallback?) {
        this.updateCallback = updateCallback
    }

    /**标签文本格式 */
    interface TextFormatter {
        fun format(value: Float): String
    }

    /**
     * 获取当前值
     */
    fun getValue(): Float {
        return value
    }

    /**
     * 设置值
     */
    fun setValue(value: Float) {
        doSetValue(value, true)
    }

    /**
     * 设置值，不触发回调
     */
    fun setValueNoEvent(value: Float) {
        doSetValue(value, false)
    }

    private fun doSetValue(value: Float, nofify: Boolean) {
        var v = value
        if (this.builder != null && v >= this.builder!!.min && v <= this.builder!!.max) {
            if (v < this.builder!!.min) {
                v = this.builder!!.min
            } else if (v > this.builder!!.max) {
                v = this.builder!!.max
            }
            if (Math.abs(this.value - v) > 0) {
                this.value = v
                if (isInitialized) {
                    currentX = (v - this.builder!!.min) * (width - this.builder!!.sliderWidth) / (this.builder!!.max - this.builder!!.min) + this.builder!!.sliderWidth / 2
                } else {
                    hasCacheValue = true
                }
                invalidate()
                if (nofify && updateCallback != null) {
                    updateCallback!!.onValueUpdate(v)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (this.builder != null) {
            isInitialized = true
            if (hasCacheValue) {
                hasCacheValue = false
                currentX = (value - this.builder!!.min) * (width - this.builder!!.sliderWidth) / (this.builder!!.max - this.builder!!.min) + this.builder!!.sliderWidth / 2
            }
            paint.strokeWidth = this.builder!!.scaleWidth.toFloat()
            //画刻度和标签
            val scaleTotalWidth = width - this.builder!!.sliderWidth //画刻度的总宽度
            //先算开始的横坐标,两边空白滑块宽度的一半
            val startX = this.builder!!.sliderWidth / 2
            //长刻度顶部纵坐标 = (控件高度-长刻度-刻度与标签间距-标签高度) / 2
            val longScaleStartY = ((height - this.builder!!.longScaleLen - this.builder!!.labelAndScaleSpace - labelHeight) / 2).toFloat()
            val shortScaleLen = this.builder!!.longScaleLen * this.builder!!.shortLongScaleRatio
            val midScaleLen = (this.builder!!.longScaleLen - shortScaleLen) / 2 + shortScaleLen
            //计算刻度间距
            val space = scaleTotalWidth / scales
            //计算刻度间代表的值，用来计算当前数值指示线位置
            val unit = (this.builder!!.max - this.builder!!.min) / scales
            //刻度基准线Y坐标
            val scaleBaseline = when (this.builder!!.scaleAlignment) {
                ALIGN_TOP -> longScaleStartY
                ALIGN_CENTER -> longScaleStartY + this.builder!!.longScaleLen / 2
                else -> longScaleStartY + this.builder!!.longScaleLen
            }
            for (i in 0..scales) {
                val scaleStartX = startX - this.builder!!.scaleWidth / 2 + i * space
                //计算刻度长度
                val scaleLen: Float
                val scaleValue = this.builder!!.min + unit * i
                when {
                    i % 10 == 0 -> {
                        scaleLen = this.builder!!.longScaleLen.toFloat()
                        //画标签
                        val label = getLabel(scaleValue)
                        val textWidth = paint.measureText(label)
                        paint.color = this.builder!!.labelColor
                        val textX = scaleStartX - textWidth / 2
                        when (i) {
                            0 -> //第一个标签
                                canvas.drawText(label, if (textX < 0) 0f else textX, longScaleStartY + this.builder!!.longScaleLen.toFloat() + this.builder!!.labelAndScaleSpace.toFloat() + labelHeight.toFloat(), paint)
                            scales -> //最后一个标签
                                canvas.drawText(label, if (textX + textWidth > width) width - textWidth else textX,
                                        longScaleStartY + this.builder!!.longScaleLen.toFloat() + this.builder!!.labelAndScaleSpace.toFloat() + labelHeight.toFloat(), paint)
                            else -> canvas.drawText(label, scaleStartX - textWidth / 2, longScaleStartY + this.builder!!.longScaleLen.toFloat() + this.builder!!.labelAndScaleSpace.toFloat() + labelHeight.toFloat(), paint)
                        }
                    }
                    i % 5 == 0 -> scaleLen = midScaleLen
                    else -> scaleLen = shortScaleLen.toInt().toFloat()
                }
                val startY = when (this.builder!!.scaleAlignment) {
                    ALIGN_TOP -> longScaleStartY
                    ALIGN_CENTER -> longScaleStartY + (this.builder!!.longScaleLen - scaleLen) / 2
                    else -> longScaleStartY + this.builder!!.longScaleLen - scaleLen
                }
                paint.color = this.builder!!.scaleColor
                canvas.drawLine(scaleStartX, startY, scaleStartX, startY + scaleLen, paint)
            }
            //画指示线           
            paint.color = this.builder!!.indicateLineColor
            if (this.builder!!.indicateLineWidth >= 0) {
                paint.strokeWidth = this.builder!!.indicateLineWidth.toFloat()
            }
            canvas.drawLine(currentX, longScaleStartY, currentX, longScaleStartY + this.builder!!.longScaleLen, paint)
            //画滑块
            if (sliderBitmap != null) {
                bitmapRectF.left = currentX - startX
                bitmapRectF.top = scaleBaseline - this.builder!!.sliderDistanceToScaleBaseLine.toFloat() - sliderBitmap!!.height.toFloat()
                bitmapRectF.right = sliderBitmap!!.width + bitmapRectF.left
                bitmapRectF.bottom = sliderBitmap!!.height + bitmapRectF.top
                canvas.drawBitmap(sliderBitmap!!, bitmapRectF.left, bitmapRectF.top, paint)
            }
        }
    }

    private fun getLabel(value: Float): String {
        return if (this.builder!!.textFormatter != null) {
            this.builder!!.textFormatter!!.format(value)
        } else value.toString()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isEnabled && this.builder != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    currentX = event.x
                    canMove = bitmapRectF.contains(event.x, event.y)
                    if (this.builder!!.clickable && event.x >= 0 && event.x <= width && event.y >= 0 && event.y <= height) { //点击在范围内
                        canMove = true //如果可点击改变值，那在按下后应该可以继续滑动
                        updateValue()
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = event.x - currentX
                    if (canMove) {
                        currentX += dx
                        updateValue()
                    }
                }
            }
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun updateValue() {
        val startX = this.builder!!.sliderWidth / 2
        if (currentX < startX) {
            currentX = startX
        } else if (currentX > width - startX) {
            currentX = width - startX
        }
        invalidate()
        var value = (currentX - startX) * (this.builder!!.max - this.builder!!.min) / (width - this.builder!!.sliderWidth) + this.builder!!.min
        if (value < this.builder!!.min) {
            value = this.builder!!.min
        } else if (value > this.builder!!.max) {
            value = this.builder!!.max
        }
        if (Math.abs(this.value - value) > 0) {
            this.value = value
            if (updateCallback != null) {
                updateCallback!!.onValueUpdate(value)
            }
        }
    }

    class Builder {
        internal var min = 0f
        internal var max = 100f
        internal var twoBigStepDifValue = 10f //两个长刻度之间值的大小
        internal var longScaleLen = 60 //长刻度尺寸
        internal var labelColor = Color.GRAY //标签颜色
        internal var labelSize = 15 //标签字体大小
        internal var typeface: Typeface? = null //标签字体
        internal var labelAndScaleSpace = 8 //标签与长刻度的间隔
        internal var scaleWidth = 2 //刻度线条宽度
        internal var scaleColor = Color.GRAY //刻度线条颜色
        internal var sliderResId: Int = 0 //滑块
        internal var sliderWidth = 40f //滑块宽度
        internal var sliderDistanceToScaleBaseLine = 6 //滑块与刻度基准线的距离，基准线取决于刻度对齐方式
        internal var shortLongScaleRatio = 0.6f //短刻度与长刻度比例，短/长        
        internal var textFormatter: TextFormatter? = null
        internal var scaleAlignment: Int = 0 //刻度对齐方式
        internal var indicateLineColor = Color.BLUE //数值指示线颜色
        internal var indicateLineWidth = -1
        internal var clickable: Boolean = false

        /** 设置取值范围  */
        fun setRange(min: Float, max: Float): Builder {
            this.min = min
            this.max = max
            return this
        }

        /** 两个长刻度之间值的大小  */
        fun setTwoBigStepDifValue(twoBigStepDifValue: Float): Builder {
            this.twoBigStepDifValue = twoBigStepDifValue
            return this
        }

        /** 长刻度尺寸  */
        fun setLongScaleLen(longScaleLen: Int): Builder {
            this.longScaleLen = longScaleLen
            return this
        }

        /** 标签颜色  */
        fun setLabelColor(labelColor: Int): Builder {
            this.labelColor = labelColor
            return this
        }

        /** 标签字体大小  */
        fun setLabelSize(labelSize: Int): Builder {
            this.labelSize = labelSize
            return this
        }

        /** 标签字体  */
        fun setLabelTypeface(typeface: Typeface): Builder {
            this.typeface = typeface
            return this
        }

        /** 刻度与标签之间间隔  */
        fun setLabelAndScaleSpace(labelAndScaleSpace: Int): Builder {
            this.labelAndScaleSpace = labelAndScaleSpace
            return this
        }

        /** 刻度线条宽度  */
        fun setScaleWidth(scaleWidth: Int): Builder {
            this.scaleWidth = scaleWidth
            return this
        }

        /** 刻度线条颜色  */
        fun setScaleColor(scaleColor: Int): Builder {
            this.scaleColor = scaleColor
            return this
        }

        /**
         * 设置滑块
         * @param distanceToScaleBaseLine 滑块底部与刻度基准线的距离，基准线取决于刻度对齐方式
         */
        fun setSlider(@DrawableRes sliderResId: Int, width: Int, distanceToScaleBaseLine: Int): Builder {
            this.sliderResId = sliderResId
            sliderWidth = width.toFloat()
            sliderDistanceToScaleBaseLine = distanceToScaleBaseLine
            return this
        }

        /** 短刻度与长刻度比例，短/长  */
        fun setShortLongScaleRatio(shortLongScaleRatio: Float): Builder {
            this.shortLongScaleRatio = shortLongScaleRatio
            return this
        }

        /**
         * 数值格式
         */
        fun setTextFormatter(textFormatter: TextFormatter): Builder {
            this.textFormatter = textFormatter
            return this
        }

        /**
         * 设置刻度对齐方式
         * @param alignment [ALIGN_BOTTOM], [ALIGN_CENTER], [ALIGN_TOP]
         */
        fun setScaleAlignment(alignment: Int): Builder {
            scaleAlignment = alignment
            return this
        }

        /**
         * 设置数值指示线颜色
         */
        fun setIndicateLineColor(color: Int): Builder {
            indicateLineColor = color
            return this
        }

        /**
         * 设置数值指示线粗细
         */
        fun setIndicateLineWidth(width: Int): Builder {
            indicateLineWidth = width
            return this
        }

        /**
         * 设置是否支持点击选中
         */
        fun setClickable(b: Boolean): Builder {
            clickable = b
            return this
        }
    }

    companion object {
        const val ALIGN_BOTTOM = 0
        const val ALIGN_CENTER = 1
        const val ALIGN_TOP = 2
    }
}
