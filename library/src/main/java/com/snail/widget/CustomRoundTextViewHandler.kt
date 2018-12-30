package com.snail.widget

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View
import android.widget.TextView


/**
 * 描述:
 * 时间: 2018/11/7 13:27
 * 作者: zengfansheng
 */
internal class CustomRoundTextViewHandler<T : TextView>(private val textView: T) {
    var strokeWidth: Int = 0
    var cornerRadius: Int = -1
    var normalTextColor: Int = textView.currentTextColor
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
    var disabledTextColor: Int = textView.currentTextColor
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
    
    //修改了参数后，调用此方法生效
    fun updateBackground() {
        textView.background = Utils.createStateListDrawable(Utils.createDrawable(normalFillColor, if (normalStrokeWidth == -1) strokeWidth else normalStrokeWidth, normalStrokeColor, cornerRadius),
                Utils.createDrawable(pressedFillColor, if (pressedStrokeWidth == -1) strokeWidth else pressedStrokeWidth, pressedStrokeColor, cornerRadius),
                Utils.createDrawable(selectedFillColor, if (selectedStrokeWidth == -1) strokeWidth else selectedStrokeWidth, selectedStrokeColor, cornerRadius),
                Utils.createDrawable(disabledFillColor, if (disabledStrokeWidth == -1) strokeWidth else disabledStrokeWidth, disabledStrokeColor, cornerRadius))
        if (rippleColor != Color.TRANSPARENT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textView.background = RippleDrawable(ColorStateList.valueOf(rippleColor), textView.background, textView.background)
        }
    }

    fun onMeasure(heightMeasureSpec: Int) {
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (cornerRadius == -1) {
            cornerRadius = height
        }
        updateBackground()
    }

    fun setTextColor(normal: Int, pressed: Int, selected: Int, disabled: Int) {
        textView.setTextColor(Utils.createColorStateList(normal, pressed, selected, disabled))
    }

    fun updateTextColor() {
        textView.setTextColor(Utils.createColorStateList(normalTextColor, pressedTextColor, selectedTextColor, disabledTextColor))
    }
}
