package com.snail.widget.textview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.snail.widget.R
import com.snail.widget.Utils

/**
 * 圆角背景文字
 */
open class RoundTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyle: Int = android.R.attr.textViewStyle) :
        AppCompatTextView(context, attrs, defStyle) {
    var strokeWidth: Int = 0
    var cornerRadius: Int = -1
    var normalTextColor: Int = currentTextColor
        set(value) {
            field = value
            updateTextColor()
        }
    var normalStrokeWidth: Int = -1
    var normalFillColor: Int = Color.LTGRAY
    var normalStrokeColor: Int = normalFillColor
    var pressedTextColor: Int = normalTextColor
        set(value) {
            field = value
            updateTextColor()
        }
    var pressedStrokeColor: Int = normalStrokeColor
    var pressedStrokeWidth: Int = -1
    var pressedFillColor: Int = normalFillColor
    var disabledTextColor: Int = currentTextColor
        set(value) {
            field = value
            updateTextColor()
        }
    var disabledStrokeColor: Int = Color.LTGRAY
    var disabledStrokeWidth: Int = -1
    var disabledFillColor: Int = Color.LTGRAY
    var selectedTextColor: Int = normalTextColor
        set(value) {
            field = value
            updateTextColor()
        }
    var selectedStrokeColor: Int = normalStrokeColor
    var selectedStrokeWidth: Int = -1
    var selectedFillColor: Int = normalFillColor

    var rippleColor = Color.TRANSPARENT

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView)
            strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswStrokeWidth, strokeWidth)
            normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswNormalStrokeWidth, normalStrokeWidth)
            pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswPressedStrokeWidth, pressedStrokeWidth)
            disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswDisabledStrokeWidth, disabledStrokeWidth)
            selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswSelectedStrokeWidth, selectedStrokeWidth)
            normalFillColor = a.getColor(R.styleable.RoundTextView_wswNormalFillColor, normalFillColor)
            cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswCornerRadius, cornerRadius)
            normalStrokeColor = a.getColor(R.styleable.RoundTextView_wswNormalStrokeColor, normalStrokeColor)
            normalTextColor = a.getColor(R.styleable.RoundTextView_wswNormalTextColor, normalTextColor)
            pressedStrokeColor = a.getColor(R.styleable.RoundTextView_wswPressedStrokeColor, pressedStrokeColor)
            pressedTextColor = a.getColor(R.styleable.RoundTextView_wswPressedTextColor, pressedTextColor)
            pressedFillColor = a.getColor(R.styleable.RoundTextView_wswPressedFillColor, pressedFillColor)
            selectedStrokeColor = a.getColor(R.styleable.RoundTextView_wswSelectedStrokeColor, selectedStrokeColor)
            selectedTextColor = a.getColor(R.styleable.RoundTextView_wswSelectedTextColor, selectedTextColor)
            selectedFillColor = a.getColor(R.styleable.RoundTextView_wswSelectedFillColor, selectedFillColor)
            disabledStrokeColor = a.getColor(R.styleable.RoundTextView_wswDisabledStrokeColor, disabledStrokeColor)
            disabledFillColor = a.getColor(R.styleable.RoundTextView_wswDisabledFillColor, disabledFillColor)
            disabledTextColor = a.getColor(R.styleable.RoundTextView_wswDisabledTextColor, disabledTextColor)
            rippleColor = a.getColor(R.styleable.RoundTextView_wswRippleColor, rippleColor)
            a.recycle()
        }
        updateTextColor()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (cornerRadius == -1) {
            cornerRadius = height
        }
        updateBackground()
    }

    /**
     * /** 修改了参数后，调用此方法生效 */
     */
    fun updateBackground() {
        val drawable: Drawable = Utils.createStateListDrawable(
                Utils.createDrawable(normalFillColor, if (normalStrokeWidth == -1) strokeWidth else normalStrokeWidth,
                        normalStrokeColor, cornerRadius),
                Utils.createDrawable(pressedFillColor, if (pressedStrokeWidth == -1) strokeWidth else pressedStrokeWidth,
                        pressedStrokeColor, cornerRadius),
                Utils.createDrawable(selectedFillColor, if (selectedStrokeWidth == -1) strokeWidth else selectedStrokeWidth,
                        selectedStrokeColor, cornerRadius),
                Utils.createDrawable(disabledFillColor, if (disabledStrokeWidth == -1) strokeWidth else disabledStrokeWidth,
                        disabledStrokeColor, cornerRadius)
        )
        if (rippleColor != Color.TRANSPARENT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            background = RippleDrawable(ColorStateList.valueOf(rippleColor), drawable, null)
        } else {
            background = drawable
        }
    }

    fun setTextColor(normal: Int, pressed: Int, selected: Int, disabled: Int) {
        setTextColor(Utils.createColorStateList(normal, pressed, selected, disabled))
    }

    fun updateTextColor() {
        setTextColor(Utils.createColorStateList(normalTextColor, pressedTextColor, selectedTextColor, disabledTextColor))
    }    
}
