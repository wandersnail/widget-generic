package com.snail.widget

import android.content.Context
import android.util.AttributeSet

/**
 * 圆角背景文字
 */
class RoundTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyle: Int = android.R.attr.textViewStyle) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyle) {
    private val textViewHandler: CustomRoundTextViewHandler<RoundTextView> = CustomRoundTextViewHandler(this)

    val cornerRadius: Int
        get() = textViewHandler.cornerRadius

    val normalStrokeColor: Int
        get() = textViewHandler.normalStrokeColor

    val normalFillColor: Int
        get() = textViewHandler.normalFillColor

    val pressedStrokeColor: Int
        get() = textViewHandler.pressedStrokeColor

    val pressedFillColor: Int
        get() = textViewHandler.pressedFillColor

    val disabledStrokeColor: Int
        get() = textViewHandler.disabledStrokeColor

    val disabledFillColor: Int
        get() = textViewHandler.disabledFillColor

    val strokeWidth: Int
        get() = textViewHandler.strokeWidth

    val normalStrokeWidth: Int
        get() = textViewHandler.normalStrokeWidth

    val pressedStrokeWidth: Int
        get() = textViewHandler.pressedStrokeWidth

    val disabledStrokeWidth: Int
        get() = textViewHandler.disabledStrokeWidth

    val selectedStrokeColor: Int
        get() = textViewHandler.selectedStrokeColor

    val selectedStrokeWidth: Int
        get() = textViewHandler.selectedStrokeWidth

    val selectedFillColor: Int
        get() = textViewHandler.selectedFillColor

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView)
            textViewHandler.strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvStrokeWidth, strokeWidth)
            textViewHandler.normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvNormalStrokeWidth, normalStrokeWidth)
            textViewHandler.pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvPressedStrokeWidth, pressedStrokeWidth)
            textViewHandler.disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvDisabledStrokeWidth, disabledStrokeWidth)
            textViewHandler.selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvSelectedStrokeWidth, selectedStrokeWidth)
            textViewHandler.normalFillColor = a.getColor(R.styleable.RoundTextView_rtvNormalFillColor, normalFillColor)
            textViewHandler.cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvCornerRadius, cornerRadius)
            textViewHandler.normalStrokeColor = a.getColor(R.styleable.RoundTextView_rtvNormalStrokeColor, normalStrokeColor)
            textViewHandler.normalTextColor = a.getColor(R.styleable.RoundTextView_rtvNormalTextColor, textViewHandler.normalTextColor)
            textViewHandler.pressedStrokeColor = a.getColor(R.styleable.RoundTextView_rtvPressedStrokeColor, pressedStrokeColor)
            textViewHandler.pressedTextColor = a.getColor(R.styleable.RoundTextView_rtvPressedTextColor, textViewHandler.pressedTextColor)
            textViewHandler.pressedFillColor = a.getColor(R.styleable.RoundTextView_rtvPressedFillColor, pressedFillColor)
            textViewHandler.selectedStrokeColor = a.getColor(R.styleable.RoundTextView_rtvSelectedStrokeColor, selectedStrokeColor)
            textViewHandler.selectedTextColor = a.getColor(R.styleable.RoundTextView_rtvSelectedTextColor, textViewHandler.selectedTextColor)
            textViewHandler.selectedFillColor = a.getColor(R.styleable.RoundTextView_rtvSelectedFillColor, selectedFillColor)
            textViewHandler.disabledStrokeColor = a.getColor(R.styleable.RoundTextView_rtvDisabledStrokeColor, disabledStrokeColor)
            textViewHandler.disabledFillColor = a.getColor(R.styleable.RoundTextView_rtvDisabledFillColor, disabledFillColor)
            textViewHandler.disabledTextColor = a.getColor(R.styleable.RoundTextView_rtvDisabledTextColor, textViewHandler.disabledTextColor)
            textViewHandler.rippleColor = a.getColor(R.styleable.RoundTextView_rtvRippleColor, textViewHandler.rippleColor)
            a.recycle()
        }
        textViewHandler.updateTextColor()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        textViewHandler.onMeasure(heightMeasureSpec)
    }

    /** 修改了参数后，调用此方法生效 */
    fun updateBackground() {
        textViewHandler.updateBackground()
    }

    fun setTextColor(normal: Int, pressed: Int, selected: Int, disabled: Int): RoundTextView {
        textViewHandler.setTextColor(normal, pressed, selected, disabled)
        return this
    }

    fun setNormalTextColor(normalTextColor: Int): RoundTextView {
        textViewHandler.normalTextColor = normalTextColor
        return this
    }

    fun setPressedTextColor(pressedTextColor: Int): RoundTextView {
        textViewHandler.pressedTextColor = pressedTextColor
        return this
    }

    fun setDisabledTextColor(disabledTextColor: Int): RoundTextView {
        textViewHandler.disabledTextColor = disabledTextColor
        return this
    }

    fun setSelectedTextColor(selectedTextColor: Int): RoundTextView {
        textViewHandler.selectedTextColor = selectedTextColor
        return this
    }

    fun setStrokeWidth(strokeWidth: Int): RoundTextView {
        textViewHandler.strokeWidth = strokeWidth
        return this
    }

    fun setCornerRadius(cornerRadius: Int): RoundTextView {
        textViewHandler.cornerRadius = cornerRadius
        return this
    }

    fun setNormalStrokeColor(normalStrokeColor: Int): RoundTextView {
        textViewHandler.normalStrokeColor = normalStrokeColor
        return this
    }

    fun setNormalFillColor(normalFillColor: Int): RoundTextView {
        textViewHandler.normalFillColor = normalFillColor
        return this
    }

    fun setPressedStrokeColor(pressedStrokeColor: Int): RoundTextView {
        textViewHandler.pressedStrokeColor = pressedStrokeColor
        return this
    }

    fun setPressedFillColor(pressedFillColor: Int): RoundTextView {
        textViewHandler.pressedFillColor = pressedFillColor
        return this
    }

    fun setDisabledStrokeColor(disabledStrokeColor: Int): RoundTextView {
        textViewHandler.disabledStrokeColor = disabledStrokeColor
        return this
    }

    fun setDisabledFillColor(disabledFillColor: Int): RoundTextView {
        textViewHandler.disabledFillColor = disabledFillColor
        return this
    }

    fun setSelectedStrokeColor(selectedStrokeColor: Int): RoundTextView {
        textViewHandler.selectedStrokeColor = selectedStrokeColor
        return this
    }

    fun setSelectedFillColor(selectedFillColor: Int): RoundTextView {
        textViewHandler.selectedFillColor = selectedFillColor
        return this
    }

    fun setNormalStrokeWidth(normalStrokeWidth: Int): RoundTextView {
        textViewHandler.normalStrokeWidth = normalStrokeWidth
        return this
    }

    fun setPressedStrokeWidth(pressedStrokeWidth: Int): RoundTextView {
        textViewHandler.pressedStrokeWidth = pressedStrokeWidth
        return this
    }

    fun setDisabledStrokeWidth(disabledStrokeWidth: Int): RoundTextView {
        textViewHandler.disabledStrokeWidth = disabledStrokeWidth
        return this
    }

    fun setSelectedStrokeWidth(selectedStrokeWidth: Int): RoundTextView {
        textViewHandler.selectedStrokeWidth = selectedStrokeWidth
        return this
    }
    
    fun setRippleColor(rippleColor: Int): RoundTextView {
        textViewHandler.rippleColor = rippleColor
        return this
    }
}
