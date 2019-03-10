package com.snail.widget.textview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.snail.widget.R

/**
 * 可旋转的TextView
 *
 * date: 2018/12/29 14:21
 * author: zengfansheng
 */
open class RotatableTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle) :
        AppCompatTextView(context, attrs, defStyleAttr) {
    var degree: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    
    init {
        gravity = Gravity.CENTER
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.RotatableTextView)
            degree = a.getFloat(R.styleable.RotatableTextView_wswDegree, degree)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop.toFloat())
        canvas.rotate(degree, this.width / 2f, this.height / 2f)
        super.onDraw(canvas)
        canvas.restore()
    }
}