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
    //用于设置渐变色的颜色集
    private var colors: IntArray? = null
    //用于设置渐变色的开始色
    private var startColor: Int = 0
    //用于设置渐变色的结束色
    private var endColor: Int = 0
    //背景圆环的颜色
    private var backgroundCircleColor: Int = 0
    //圆环进度的颜色
    private var progressCircleColor: Int = 0
    //中间进度百分比的字符串的颜色
    private var textColor: Int = 0
    //中间进度百分比的字符串的字体
    private var textSize: Float = 0.toFloat()
    //背景圆环的宽度
    private var backgroundCircleWidth: Float = 0.toFloat()
    //进度圆环的宽度
    private var progressCircleWidth: Float = 0.toFloat()
    //小圆点的半径
    private var dotRadius: Float = 0.toFloat()
    //最大进度
    private var max: Int = 0
    //当前进度
    private var progress: Int = 0
    //是否显示中间的进度
    private var isTextVisible: Boolean = false
    //是否画小圆点
    private var isDotVisible: Boolean = false
    //是否显示默认圆环
    private var isBackgroundCircleVisible: Boolean = false
    //渐变色是否使用颜色数组
    private var isArrayColorEnabled: Boolean = false
    //进度条的风格
    private var circleStyle: Paint.Style? = null
    //进度环的颜色风格
    private var colorStyle: Int = 0
    private var paint: Paint? = null
    //用于定义的圆弧的形状和大小的界限
    private var oval: RectF? = null
    //用于记录上次渐变色的参数
    private var cx: Float = 0.toFloat()
    private var cy: Float = 0.toFloat()
    //上次渐变色实例
    private var sweepGradient: SweepGradient? = null
    //是否实例化过渐变色
    private var isInstantiated: Boolean = false
    //进度条帽的形状
    private var cap: Paint.Cap? = null
    //进度条开始角度
    private var startAngle: Float = 0.toFloat()
    private var textFormat = DecimalFormat("#")

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar)
        isTextVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbTextVisible, true)
        textSize = typedArray.getDimension(R.styleable.CircleProgressBar_cpbTextSize, 24f)
        backgroundCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpbBackgroundCircleWidth, 5f)
        max = typedArray.getInt(R.styleable.CircleProgressBar_cpbMax, 100)
        progress = typedArray.getInt(R.styleable.CircleProgressBar_cpbProgress, 0)
        circleStyle = intToStyle(typedArray.getInt(R.styleable.CircleProgressBar_cpbCircleStyle, 1))
        isDotVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbDotVisible, false)
        isBackgroundCircleVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_cpbBackgroundCircleVisible, true)
        startColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbStartColor, -0x3a2acc)
        endColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbEndColor, -0xad485e)
        progressCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_cpbProgressCircleWidth, backgroundCircleWidth)
        backgroundCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbBackgroundCircleColor, -0x101011)
        progressCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbProgressCircleColor, -0x6f15ed)
        textColor = typedArray.getColor(R.styleable.CircleProgressBar_cpbTextColor, -0x6f15ed)
        dotRadius = typedArray.getDimension(R.styleable.CircleProgressBar_cpbDotRadius, 0f)
        startAngle = typedArray.getFloat(R.styleable.CircleProgressBar_cpbStartAngle, 0f)
        cap = intToCap(typedArray.getInt(R.styleable.CircleProgressBar_cpbStrokeCap, 0))
        colorStyle = typedArray.getInt(R.styleable.CircleProgressBar_cpbColorStyle, COLOR_STYLE_SOLID)
        typedArray.recycle()
        init()
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

    private fun init() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        oval = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        paint!!.shader = null
        paint!!.style = circleStyle
        paint!!.strokeCap = cap
        //画最外层的大圆环
        val centerX = (width / 2).toFloat() //获取圆心的x坐标
        dotRadius = if (dotRadius == 0f) progressCircleWidth / 2 else dotRadius
        val radius = centerX - dotRadius - backgroundCircleWidth / 2 //圆环的半径

        if (isBackgroundCircleVisible) {
            paint!!.color = backgroundCircleColor //设置圆环的颜色
            paint!!.strokeWidth = backgroundCircleWidth //设置圆环的宽度
            canvas.drawCircle(centerX, centerX, radius, paint!!) //画出圆环 
        }

        //画进度百分比
        if (isTextVisible) {
            paint!!.strokeWidth = 0f
            paint!!.style = Paint.Style.FILL
            paint!!.color = textColor
            paint!!.textSize = textSize
            paint!!.typeface = Typeface.DEFAULT_BOLD //设置字体
            val text = textFormat.format((progress * 100f / max).toDouble()) + "%"
            val fontMetrics = paint!!.fontMetricsInt
            val baseline = (height - fontMetrics.bottom - fontMetrics.top) / 2
            paint!!.textAlign = Paint.Align.CENTER
            //画出进度百分比
            canvas.drawText(text, (width / 2).toFloat(), baseline.toFloat(), paint!!)
        }

        //画圆弧 ，画圆环的进度
        canvas.rotate(-180 + startAngle, centerX, centerX)
        paint!!.style = circleStyle
        paint!!.strokeWidth = progressCircleWidth //设置圆环的宽度

        oval!!.set(centerX - radius, centerX - radius, centerX + radius, centerX + radius)
        var shader: SweepGradient? = null
        when (colorStyle) {
            COLOR_STYLE_SOLID -> {
                paint!!.shader = null
                paint!!.color = progressCircleColor  //设置进度的颜色
            }
            COLOR_STYLE_GRADIENT -> {
                //设置环形渐变色
                shader = getShader(centerX, centerX)
                paint!!.shader = shader
            }
        }
        when (circleStyle) {
            Paint.Style.STROKE -> canvas.drawArc(oval!!, 0f, 360f * progress / max, false, paint!!)  //根据进度画圆弧			
            Paint.Style.FILL, Paint.Style.FILL_AND_STROKE -> if (progress != 0)
                canvas.drawArc(oval!!, 0f, 360f * progress / max, true, paint!!)  //根据进度画圆
        }

        //画圆环上小圆点
        if (isDotVisible) {
            paint!!.style = Paint.Style.FILL
            when (colorStyle) {
                COLOR_STYLE_SOLID -> paint!!.color = progressCircleColor
                COLOR_STYLE_GRADIENT -> when (progress) {
                    0 -> {
                        paint!!.shader = null
                        paint!!.color = if (isArrayColorEnabled) colors!![0] else startColor
                    }
                    max -> {
                        paint!!.shader = null
                        paint!!.color = if (isArrayColorEnabled) colors!![colors!!.size - 1] else endColor
                    }
                    else -> paint!!.shader = shader
                }
            }
            //小圆点到圆环的半径
            canvas.drawCircle((centerX + radius * Math.cos(Math.toRadians((360f * progress / max).toDouble()))).toFloat(),
                    (centerX + radius * Math.sin(Math.toRadians((360f * progress / max).toDouble()))).toFloat(), dotRadius, paint!!)
        }
    }

    //获取渐变色实例，避免在onDraw中多次实例化
    private fun getShader(cx: Float, cy: Float): SweepGradient? {
        if (this.cx != cx || this.cy != cy || !isInstantiated) {
            this.cx = cx
            this.cy = cy
            isInstantiated = true
            sweepGradient = if (isArrayColorEnabled)
                SweepGradient(cx, cy, colors!!, null)
            else
                SweepGradient(cx, cy, startColor, endColor)
        }
        return sweepGradient
    }

    /**
     * 设置百分比文本显示格式
     */
    fun setTextFormat(format: DecimalFormat) {
        textFormat = format
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

    /**
     * 设置进度条开始角度，0~360
     */
    fun setStartAngle(startAngle: Float) {
        var angle = startAngle
        if (angle > 360) angle = 360f
        if (angle < 0) angle = 0f
        this.startAngle = angle
    }

    /**
     * 进度条帽的形状
     */
    fun setStrokeCap(cap: Paint.Cap) {
        this.cap = cap
    }

    /**
     * 设置进度圆环的颜色风格
     */
    fun setColorStyle(colorStyle: Int) {
        this.colorStyle = colorStyle
    }

    /**
     * 设置背景圆环的颜色
     */
    fun setBackgroundCircleColor(backgroundCircleColor: Int) {
        this.backgroundCircleColor = backgroundCircleColor
    }

    /**
     * 设置进度圆环颜色
     */
    fun setProgressCircleColor(progressCircleColor: Int) {
        this.progressCircleColor = progressCircleColor
    }

    /**
     * 设置进度圆环颜色
     */
    fun setProgressCircleColor(colors: IntArray) {
        this.colors = colors
        isArrayColorEnabled = true
    }

    /**
     * 设置进度圆环颜色
     */
    fun setProgressCircleColor(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
        isArrayColorEnabled = false
    }

    /**
     * 设置进度文本的颜色
     */
    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    /**
     * 设置进度文本的大小
     */
    fun setTextSize(textSize: Float) {
        this.textSize = textSize
    }

    /**
     * 设置背景圆环的宽度
     */
    fun setBackgroundCircleWidth(backgroundCircleWidth: Float) {
        this.backgroundCircleWidth = backgroundCircleWidth
    }

    /**
     * 设置进度圆环的宽度
     */
    fun setProgressCircleWidth(progressCircleWidth: Float) {
        this.progressCircleWidth = progressCircleWidth
    }

    /**
     * 设置小圆点的半径长度
     */
    fun setDotRadius(dotRadius: Float) {
        this.dotRadius = dotRadius
    }

    /**
     * 设置进度条整体风格
     */
    fun setCircleStyle(circleStyle: Paint.Style) {
        this.circleStyle = circleStyle
    }

    /**
     * 设置进度文本是否显示
     */
    fun setTextVisible(textVisible: Boolean) {
        isTextVisible = textVisible
    }

    /**
     * 设置小圆点是否显示
     */
    fun setDotVisible(dotVisible: Boolean) {
        isDotVisible = dotVisible
    }

    /**
     * 设置默认圆环是否显示
     */
    fun setBackgroundCircleVisible(backgroundCircleVisible: Boolean) {
        isBackgroundCircleVisible = backgroundCircleVisible
    }

    companion object {
        const val COLOR_STYLE_SOLID = 0
        const val COLOR_STYLE_GRADIENT = 1
    }
}
