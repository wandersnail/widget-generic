package com.snail.widget

import android.view.View
import android.widget.TextView
import com.snail.commons.utils.ImageUtils
import com.snail.commons.utils.UiUtils


/**
 * 描述:
 * 时间: 2018/11/7 13:27
 * 作者: zengfansheng
 */
internal class CustomRoundTextViewHandler<T : TextView>(textView: T) {
    private val textView: TextView
    var strokeWidth: Int = 0
    var cornerRadius: Int = 0
    var normalTextColor: Int = 0
        set(value) {
            field = value
            updateTextColor()
        }
    var normalStrokeColor: Int = 0
    var normalStrokeWidth: Int = 0
    var normalFillColor: Int = 0
    var pressedTextColor: Int = 0
        set(value) {
            field = value
            updateTextColor()
        }
    var pressedStrokeColor: Int = 0
    var pressedStrokeWidth: Int = 0
    var pressedFillColor: Int = 0
    var disabledTextColor: Int = 0
        set(value) {
            field = value
            updateTextColor()
        }
    var disabledStrokeColor: Int = 0
    var disabledStrokeWidth: Int = 0
    var disabledFillColor: Int = 0
    var selectedTextColor: Int = 0
        set(value) {
            field = value
            updateTextColor()
        }
    var selectedStrokeColor: Int = 0
    var selectedStrokeWidth: Int = 0
    var selectedFillColor: Int = 0

    init {
        this.textView = textView
    }

    //修改了参数后，调用此方法生效
    fun updateBackground() {
        textView.background = ImageUtils.createStateListDrawable(ImageUtils.createDrawable(normalFillColor, if (normalStrokeWidth == -1) strokeWidth else normalStrokeWidth, normalStrokeColor, cornerRadius),
                ImageUtils.createDrawable(pressedFillColor, if (pressedStrokeWidth == -1) strokeWidth else pressedStrokeWidth, pressedStrokeColor, cornerRadius),
                ImageUtils.createDrawable(selectedFillColor, if (selectedStrokeWidth == -1) strokeWidth else selectedStrokeWidth, selectedStrokeColor, cornerRadius),
                ImageUtils.createDrawable(disabledFillColor, if (disabledStrokeWidth == -1) strokeWidth else disabledStrokeWidth, disabledStrokeColor, cornerRadius))
    }

    fun onMeasure(heightMeasureSpec: Int) {
        val height = View.MeasureSpec.getSize(heightMeasureSpec)
        if (cornerRadius == -1) {
            cornerRadius = height
        }
        updateBackground()
    }

    fun setTextColor(normal: Int, pressed: Int, selected: Int, disabled: Int) {
        textView.setTextColor(UiUtils.createColorStateList(normal, pressed, selected, disabled))
    }

    fun updateTextColor() {
        textView.setTextColor(UiUtils.createColorStateList(normalTextColor, pressedTextColor, selectedTextColor, disabledTextColor))
    }
}
