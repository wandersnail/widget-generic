package com.snail.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.text.DecimalFormat

/**
 * Created by zengfs on 2016/1/13.
 * 圆形进度条，风格有环形和填充。进度条圆环支持颜色渐变
 */
class CircleProgressBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {
    //最大进度
    private var max = 100
    //当前进度
    private var progress = 0    
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)    
    //用于定义的圆弧的形状和大小的界限
    private var oval = RectF()
    //用于记录上次渐变色的参数
    private var cx = 0f
    private var cy = 0f
    //上次渐变色实例
    private var sweepGradient: SweepGradient? = null
    //是否实例化过渐变色
    private var isInstantiated: Boolean = false
    val builder = Builder(this)

    init {        
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
            builder.isTextVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbTextVisible, true)
            builder.textSize = typedArray.getDimension(R.styleable.CircleProgressBar_cpbTextSize, 24f)
            builder.backgroundCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpbBackgroundCircleWidth, 5f)
            max = typedArray.getInt(R.styleable.CircleProgressBar_cpbMax, 100)
            progress = typedArray.getInt(R.styleable.CircleProgressBar_cpbProgress, 0)
            builder.circleStyle = intToStyle(typedArray.getInt(R.styleable.CircleProgressBar_cpbCircleStyle, Paint.Style.STROKE.ordinal))
            builder.isDotVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbDotVisible, false)
            builder.isBackgroundCircleVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbBackgroundCircleVisible, true)
            builder.startColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbStartColor, -0x3a2acc)
            builder.endColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbEndColor, -0xad485e)
            builder.progressCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpbProgressCircleWidth, builder.backgroundCircleWidth)
            builder.backgroundCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbBackgroundCircleColor, -0x101011)
            builder.progressCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbProgressCircleColor, -0x6f15ed)
            builder.textColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbTextColor, -0x6f15ed)
            builder.dotRadius = typedArray.getDimension(R.styleable.CircleProgressBar_cpbDotRadius, 0f)
            builder.startAngle = typedArray.getFloat(R.styleable.CircleProgressBar_cpbStartAngle, 0f)
            builder.cap = intToCap(typedArray.getInt(R.styleable.CircleProgressBar_cpbStrokeCap, Paint.Cap.BUTT.ordinal))
            builder.colorStyle = typedArray.getInt(R.styleable.CircleProgressBar_cpbColorStyle, COLOR_STYLE_SOLID)
            typedArray.recycle()
        }
    }

    private fun intToCap(cap: Int): Paint.Cap {
        return when (cap) {
            2 -> Paint.Cap.SQUARE
            1 -> Paint.Cap.ROUND
            else -> Paint.Cap.BUTT
        }
    }

    private fun intToStyle(style: Int): Paint.Style {
        return when (style) {
            2 -> Paint.Style.FILL_AND_STROKE
            0 -> Paint.Style.FILL
            else -> Paint.Style.STROKE
        }
    }

    override fun onDraw(canvas: Canvas) {
        paint.shader = null
        paint.style = builder.circleStyle
        paint.strokeCap = builder.cap
        //画最外层的大圆环
        val centerX = (width / 2).toFloat() //获取圆心的x坐标
        val dotRadius = if (builder.dotRadius == 0f) builder.progressCircleWidth / 2 else builder.dotRadius
        val radius = centerX - dotRadius - builder.backgroundCircleWidth / 2 //圆环的半径

        if (builder.isBackgroundCircleVisible) {
            paint.color = builder.backgroundCircleColor //设置圆环的颜色
            paint.strokeWidth = builder.backgroundCircleWidth //设置圆环的宽度
            canvas.drawCircle(centerX, centerX, radius, paint) //画出圆环 
        }

        //画进度百分比
        if (builder.isTextVisible) {
            paint.strokeWidth = 0f
            paint.style = Paint.Style.FILL
            paint.color = builder.textColor
            paint.textSize = builder.textSize
            paint.typeface = Typeface.DEFAULT_BOLD //设置字体
            val text = builder.textFormat.format((progress * 100f / max).toDouble()) + "%"
            val fontMetrics = paint.fontMetricsInt
            val baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2
            paint.textAlign = Paint.Align.CENTER
            //画出进度百分比
            canvas.drawText(text, (width / 2).toFloat(), baseline.toFloat(), paint)
        }

        //画圆弧 ，画圆环的进度
        canvas.rotate(-180 + builder.startAngle, centerX, centerX)
        paint.style = builder.circleStyle
        paint.strokeWidth = builder.progressCircleWidth //设置圆环的宽度

        oval.set(centerX - radius, centerX - radius, centerX + radius, centerX + radius)
        var shader: SweepGradient? = null
        when (builder.colorStyle) {
            COLOR_STYLE_SOLID -> {
                paint.shader = null
                paint.color = builder.progressCircleColor  //设置进度的颜色
            }
            COLOR_STYLE_GRADIENT -> {
                //设置环形渐变色
                shader = getShader(centerX, centerX)
                paint.shader = shader
            }
        }
        when (builder.circleStyle) {
            Paint.Style.STROKE -> canvas.drawArc(oval, 0f, 360f * progress / max, false, paint)  //根据进度画圆弧			
            Paint.Style.FILL, Paint.Style.FILL_AND_STROKE -> if (progress != 0) {
                canvas.drawArc(oval, 0f, 360f * progress / max, true, paint)  //根据进度画圆
            }
        }

        //画圆环上小圆点
        if (builder.isDotVisible) {
            paint.style = Paint.Style.FILL
            when (builder.colorStyle) {
                COLOR_STYLE_SOLID -> paint.color = builder.progressCircleColor
                COLOR_STYLE_GRADIENT -> when (progress) {
                    0 -> {
                        paint.shader = null
                        paint.color = if (builder.isArrayColorEnabled) builder.colors!![0] else builder.startColor
                    }
                    max -> {
                        paint.shader = null
                        paint.color = if (builder.isArrayColorEnabled) builder.colors!![builder.colors!!.size - 1] else builder.endColor
                    }
                    else -> paint.shader = shader
                }
            }
            //小圆点到圆环的半径
            canvas.drawCircle((centerX + radius * Math.cos(Math.toRadians((360f * progress / max).toDouble()))).toFloat(),
                    (centerX + radius * Math.sin(Math.toRadians((360f * progress / max).toDouble()))).toFloat(), dotRadius, paint)
        }
    }

    //获取渐变色实例，避免在onDraw中多次实例化
    private fun getShader(cx: Float, cy: Float): SweepGradient? {
        if (this.cx != cx || this.cy != cy || !isInstantiated) {
            this.cx = cx
            this.cy = cy
            isInstantiated = true
            sweepGradient = if (builder.isArrayColorEnabled)
                SweepGradient(cx, cy, builder.colors!!, null)
            else
                SweepGradient(cx, cy, builder.startColor, builder.endColor)
        }
        return sweepGradient
    }

    

    @Synchronized
    fun getMax(): Int {
        return max
    }

    /**
     * 设置进度的最大值
     */
    @Synchronized
    fun setMax(max: Int) {
        if (max < 0) {
            throw IllegalArgumentException("max not less than 0")
        }
        this.max = max
        postInvalidate()
    }

    /**
     * 获取进度，需要同步
     */
    @Synchronized
    fun getProgress(): Int {
        return progress
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     */
    @Synchronized
    fun setProgress(progress: Int) {
        var p = progress
        if (p < 0) {
            p = 0
        }
        if (p > max) {
            p = max
        }
        if (p <= max) {
            this.progress = p
            postInvalidate()
        }
    }

    class Builder internal constructor(private val progressBar: CircleProgressBar) {
        //用于设置渐变色的颜色集
        internal var colors: IntArray? = null
        //用于设置渐变色的开始色
        internal var startColor: Int = -0x3a2acc
        //用于设置渐变色的结束色
        internal var endColor: Int = -0xad485e
        //背景圆环的颜色
        internal var backgroundCircleColor: Int = -0x101011
        //圆环进度的颜色
        internal var progressCircleColor: Int = -0x6f15ed
        //中间进度百分比的字符串的颜色
        internal var textColor: Int = -0x6f15ed
        //中间进度百分比的字符串的字体
        internal var textSize = 24f
        //背景圆环的宽度
        internal var backgroundCircleWidth = 5f
        //进度圆环的宽度
        internal var progressCircleWidth = backgroundCircleWidth
        //小圆点的半径
        internal var dotRadius: Float = 0f        
        //是否显示中间的进度
        internal var isTextVisible = true
        //是否画小圆点
        internal var isDotVisible = false
        //是否显示默认圆环
        internal var isBackgroundCircleVisible = true
        //渐变色是否使用颜色数组
        internal var isArrayColorEnabled = false
        //进度条的风格
        internal var circleStyle = Paint.Style.STROKE
        //进度环的颜色风格
        internal var colorStyle = COLOR_STYLE_SOLID
        //进度条帽的形状
        internal var cap = Paint.Cap.BUTT
        //进度条开始角度
        internal var startAngle = 0f
        internal var textFormat = DecimalFormat("#")        
        
        /**
         * 设置进度条开始角度，0~360
         */
        fun setStartAngle(startAngle: Float): Builder {
            var angle = startAngle
            if (angle > 360) angle = 360f
            if (angle < 0) angle = 0f
            this.startAngle = angle
            return this
        }

        /**
         * 进度条帽的形状
         */
        fun setStrokeCap(cap: Paint.Cap): Builder {
            this.cap = cap
            return this
        }

        /**
         * 设置进度圆环的颜色风格
         */
        fun setColorStyle(colorStyle: Int): Builder {
            this.colorStyle = colorStyle
            return this
        }

        /**
         * 设置背景圆环的颜色
         */
        fun setBackgroundCircleColor(backgroundCircleColor: Int): Builder {
            this.backgroundCircleColor = backgroundCircleColor
            return this
        }

        /**
         * 设置进度圆环颜色
         */
        fun setProgressCircleColor(progressCircleColor: Int): Builder {
            this.progressCircleColor = progressCircleColor
            return this
        }

        /**
         * 设置进度圆环颜色
         */
        fun setProgressCircleColor(colors: IntArray): Builder {
            this.colors = colors
            isArrayColorEnabled = true
            return this
        }

        /**
         * 设置进度圆环颜色
         */
        fun setProgressCircleColor(startColor: Int, endColor: Int): Builder {
            this.startColor = startColor
            this.endColor = endColor
            isArrayColorEnabled = false
            return this
        }

        /**
         * 设置进度文本的颜色
         */
        fun setTextColor(textColor: Int): Builder {
            this.textColor = textColor
            return this
        }

        /**
         * 设置进度文本的大小
         */
        fun setTextSize(textSize: Float): Builder {
            this.textSize = textSize
            return this
        }

        /**
         * 设置背景圆环的宽度
         */
        fun setBackgroundCircleWidth(backgroundCircleWidth: Float): Builder {
            this.backgroundCircleWidth = backgroundCircleWidth
            return this
        }

        /**
         * 设置进度圆环的宽度
         */
        fun setProgressCircleWidth(progressCircleWidth: Float): Builder {
            this.progressCircleWidth = progressCircleWidth
            return this
        }

        /**
         * 设置小圆点的半径长度
         */
        fun setDotRadius(dotRadius: Float): Builder {
            this.dotRadius = dotRadius
            return this
        }

        /**
         * 设置进度条整体风格
         */
        fun setCircleStyle(circleStyle: Paint.Style): Builder {
            this.circleStyle = circleStyle
            return this
        }

        /**
         * 设置进度文本是否显示
         */
        fun setTextVisible(textVisible: Boolean): Builder {
            isTextVisible = textVisible
            return this
        }

        /**
         * 设置小圆点是否显示
         */
        fun setDotVisible(dotVisible: Boolean): Builder {
            isDotVisible = dotVisible
            return this
        }

        /**
         * 设置默认圆环是否显示
         */
        fun setBackgroundCircleVisible(backgroundCircleVisible: Boolean): Builder {
            isBackgroundCircleVisible = backgroundCircleVisible
            return this
        }

        /**
         * 设置百分比文本显示格式
         */
        fun setTextFormat(format: DecimalFormat): Builder {
            textFormat = format
            return this
        }
        
        fun build() {
            progressBar.postInvalidate()
        }
    }
    
    companion object {
        const val COLOR_STYLE_SOLID = 0
        const val COLOR_STYLE_GRADIENT = 1
    }
}
