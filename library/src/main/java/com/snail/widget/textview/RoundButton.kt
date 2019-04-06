package com.snail.widget.textview

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import com.snail.widget.R
import com.snail.widget.Utils

/**
 * Created by zeng on 2017/1/6.
 * 圆角背景按钮
 */
open class RoundButton @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = android.R.attr.buttonStyle) : 
        AppCompatButton(context, attrs, defStyleAttr) {
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
            val a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton)
            strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_wswStrokeWidth, strokeWidth)
            normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_wswNormalStrokeWidth, normalStrokeWidth)
            pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_wswPressedStrokeWidth, pressedStrokeWidth)
            disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_wswDisabledStrokeWidth, disabledStrokeWidth)
            selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_wswSelectedStrokeWidth, selectedStrokeWidth)
            normalFillColor = a.getColor(R.styleable.RoundButton_wswNormalFillColor, normalFillColor)
            cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundButton_wswCornerRadius, cornerRadius)
            normalStrokeColor = a.getColor(R.styleable.RoundButton_wswNormalStrokeColor, normalStrokeColor)
            normalTextColor = a.getColor(R.styleable.RoundButton_wswNormalTextColor, normalTextColor)
            pressedStrokeColor = a.getColor(R.styleable.RoundButton_wswPressedStrokeColor, pressedStrokeColor)
            pressedTextColor = a.getColor(R.styleable.RoundButton_wswPressedTextColor, pressedTextColor)
            pressedFillColor = a.getColor(R.styleable.RoundButton_wswPressedFillColor, pressedFillColor)
            selectedStrokeColor = a.getColor(R.styleable.RoundButton_wswSelectedStrokeColor, selectedStrokeColor)
            selectedTextColor = a.getColor(R.styleable.RoundButton_wswSelectedTextColor, selectedTextColor)
            selectedFillColor = a.getColor(R.styleable.RoundButton_wswSelectedFillColor, selectedFillColor)
            disabledStrokeColor = a.getColor(R.styleable.RoundButton_wswDisabledStrokeColor, disabledStrokeColor)
            disabledFillColor = a.getColor(R.styleable.RoundButton_wswDisabledFillColor, disabledFillColor)
            disabledTextColor = a.getColor(R.styleable.RoundButton_wswDisabledTextColor, disabledTextColor)
            rippleColor = a.getColor(R.styleable.RoundButton_wswRippleColor, rippleColor)
            val topBottomPaddingEnable = a.getBoolean(R.styleable.RoundButton_wswTopBottomPaddingEnabled, false)
            if (!topBottomPaddingEnable) {
                this.setPadding(paddingLeft, 0, paddingRight, 0)
            }
            a.recycle()
        }
        updateTextColor()
        //去掉最小宽高限制
        minHeight = 0
        minWidth = 0
        //不让默认英文大写
        isAllCaps = false
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
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
