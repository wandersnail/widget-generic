package com.snail.widget

import android.content.Context
import android.util.AttributeSet

/**
 * Created by zeng on 2017/1/6.
 * 圆角背景按钮
 */
class RoundButton @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = android.R.attr.buttonStyle) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {
    private val textViewHandler: CustomRoundTextViewHandler<RoundButton> = CustomRoundTextViewHandler(this)

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
            val a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton)
            textViewHandler.strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbStrokeWidth, strokeWidth)
            textViewHandler.normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbNormalStrokeWidth, normalStrokeWidth)
            textViewHandler.pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbPressedStrokeWidth, pressedStrokeWidth)
            textViewHandler.disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbDisabledStrokeWidth, disabledStrokeWidth)
            textViewHandler.selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbSelectedStrokeWidth, selectedStrokeWidth)
            textViewHandler.normalFillColor = a.getColor(R.styleable.RoundButton_rbNormalFillColor, normalFillColor)
            textViewHandler.cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundButton_rbCornerRadius, cornerRadius)
            textViewHandler.normalStrokeColor = a.getColor(R.styleable.RoundButton_rbNormalStrokeColor, normalStrokeColor)
            textViewHandler.normalTextColor = a.getColor(R.styleable.RoundButton_rbNormalTextColor, textViewHandler.normalTextColor)
            textViewHandler.pressedStrokeColor = a.getColor(R.styleable.RoundButton_rbPressedStrokeColor, pressedStrokeColor)
            textViewHandler.pressedTextColor = a.getColor(R.styleable.RoundButton_rbPressedTextColor, textViewHandler.pressedTextColor)
            textViewHandler.pressedFillColor = a.getColor(R.styleable.RoundButton_rbPressedFillColor, pressedFillColor)
            textViewHandler.selectedStrokeColor = a.getColor(R.styleable.RoundButton_rbSelectedStrokeColor, selectedStrokeColor)
            textViewHandler.selectedTextColor = a.getColor(R.styleable.RoundButton_rbSelectedTextColor, textViewHandler.selectedTextColor)
            textViewHandler.selectedFillColor = a.getColor(R.styleable.RoundButton_rbSelectedFillColor, selectedFillColor)
            textViewHandler.disabledStrokeColor = a.getColor(R.styleable.RoundButton_rbDisabledStrokeColor, disabledStrokeColor)
            textViewHandler.disabledFillColor = a.getColor(R.styleable.RoundButton_rbDisabledFillColor, disabledFillColor)
            textViewHandler.disabledTextColor = a.getColor(R.styleable.RoundButton_rbDisabledTextColor, textViewHandler.disabledTextColor)
            textViewHandler.rippleColor = a.getColor(R.styleable.RoundButton_rbRippleColor, textViewHandler.rippleColor)
            val topBottomPaddingEnable = a.getBoolean(R.styleable.RoundButton_rbTopBottomPaddingEnable, false)
            if (!topBottomPaddingEnable) {
                setPadding(paddingLeft, 0, paddingRight, 0)
            }
            a.recycle()
        }
        textViewHandler.updateTextColor()
        //去掉最小宽高限制
        minHeight = 0
        minWidth = 0
        //不让默认英文大写
        isAllCaps = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        textViewHandler.onMeasure(heightMeasureSpec)
    }

    /** 修改了参数后，调用此方法生效 */
    fun updateBackground() {
        textViewHandler.updateBackground()
    }
    
    fun setTextColor(normal: Int, pressed: Int, selected: Int, disabled: Int): RoundButton {
        textViewHandler.setTextColor(normal, pressed, selected, disabled)
        return this
    }

    fun setNormalTextColor(normalTextColor: Int): RoundButton {
        textViewHandler.normalTextColor = normalTextColor
        return this
    }

    fun setPressedTextColor(pressedTextColor: Int): RoundButton {
        textViewHandler.pressedTextColor = pressedTextColor
        return this
    }

    fun setDisabledTextColor(disabledTextColor: Int): RoundButton {
        textViewHandler.disabledTextColor = disabledTextColor
        return this
    }

    fun setSelectedTextColor(selectedTextColor: Int): RoundButton {
        textViewHandler.selectedTextColor = selectedTextColor
        return this
    }

    fun setStrokeWidth(strokeWidth: Int): RoundButton {
        textViewHandler.strokeWidth = strokeWidth
        return this
    }

    fun setCornerRadius(cornerRadius: Int): RoundButton {
        textViewHandler.cornerRadius = cornerRadius
        return this
    }

    fun setNormalStrokeColor(normalStrokeColor: Int): RoundButton {
        textViewHandler.normalStrokeColor = normalStrokeColor
        return this
    }

    fun setNormalFillColor(normalFillColor: Int): RoundButton {
        textViewHandler.normalFillColor = normalFillColor
        return this
    }

    fun setPressedStrokeColor(pressedStrokeColor: Int): RoundButton {
        textViewHandler.pressedStrokeColor = pressedStrokeColor
        return this
    }

    fun setPressedFillColor(pressedFillColor: Int): RoundButton {
        textViewHandler.pressedFillColor = pressedFillColor
        return this
    }

    fun setDisabledStrokeColor(disabledStrokeColor: Int): RoundButton {
        textViewHandler.disabledStrokeColor = disabledStrokeColor
        return this
    }

    fun setDisabledFillColor(disabledFillColor: Int): RoundButton {
        textViewHandler.disabledFillColor = disabledFillColor
        return this
    }

    fun setSelectedStrokeColor(selectedStrokeColor: Int): RoundButton {
        textViewHandler.selectedStrokeColor = selectedStrokeColor
        return this
    }

    fun setSelectedFillColor(selectedFillColor: Int): RoundButton {
        textViewHandler.selectedFillColor = selectedFillColor
        return this
    }

    fun setNormalStrokeWidth(normalStrokeWidth: Int): RoundButton {
        textViewHandler.normalStrokeWidth = normalStrokeWidth
        return this
    }

    fun setPressedStrokeWidth(pressedStrokeWidth: Int): RoundButton {
        textViewHandler.pressedStrokeWidth = pressedStrokeWidth
        return this
    }

    fun setDisabledStrokeWidth(disabledStrokeWidth: Int): RoundButton {
        textViewHandler.disabledStrokeWidth = disabledStrokeWidth
        return this
    }

    fun setSelectedStrokeWidth(selectedStrokeWidth: Int): RoundButton {
        textViewHandler.selectedStrokeWidth = selectedStrokeWidth
        return this
    }

    fun setRippleColor(rippleColor: Int): RoundButton {
        textViewHandler.rippleColor = rippleColor
        return this
    }
}
