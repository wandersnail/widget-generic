package com.snail.widget;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.snail.commons.utils.ImageUtils;
import com.snail.commons.utils.UiUtils;


/**
 * 描述:
 * 时间: 2018/11/7 13:27
 * 作者: zengfansheng
 */
class CustomRoundTextViewHandler<T extends TextView> {
    private TextView textView;
    int strokeWidth;
    int cornerRadius;
    int normalTextColor;
    int normalStrokeColor;
    int normalStrokeWidth;
    int normalFillColor;
    int pressedTextColor;
    int pressedStrokeColor;
    int pressedStrokeWidth;
    int pressedFillColor;
    int disabledTextColor;
    int disabledStrokeColor;
    int disabledStrokeWidth;
    int disabledFillColor;
    int selectedTextColor;
    int selectedStrokeColor;
    int selectedStrokeWidth;
    int selectedFillColor;

    CustomRoundTextViewHandler(@NonNull T textView) {
        this.textView = textView;
    }
    
    //修改了参数后，调用此方法生效
    void updateBackground() {
        textView.setBackground(ImageUtils.createStateListDrawable(ImageUtils.createDrawable(normalFillColor, normalStrokeWidth == -1 ? strokeWidth : normalStrokeWidth, normalStrokeColor, cornerRadius),
                ImageUtils.createDrawable(pressedFillColor, pressedStrokeWidth == -1 ? strokeWidth : pressedStrokeWidth, pressedStrokeColor, cornerRadius),
                ImageUtils.createDrawable(selectedFillColor, selectedStrokeWidth == -1 ? strokeWidth : selectedStrokeWidth, selectedStrokeColor, cornerRadius),
                ImageUtils.createDrawable(disabledFillColor, disabledStrokeWidth == -1 ? strokeWidth : disabledStrokeWidth, disabledStrokeColor, cornerRadius)));
    }

    void onMeasure(int heightMeasureSpec) {
        int height = View.MeasureSpec.getSize(heightMeasureSpec);
        if (cornerRadius == -1) {
            cornerRadius = height;
        }
        updateBackground();
    }
    
    void setTextColor(int normal, int pressed, int selected, int disabled) {
        textView.setTextColor(UiUtils.createColorStateList(normal, pressed, selected, disabled));
    }

    void updateTextColor() {
        textView.setTextColor(UiUtils.createColorStateList(normalTextColor, pressedTextColor, selectedTextColor, disabledTextColor));
    }

    void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        updateTextColor();
    }

    void setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
        updateTextColor();
    }

    void setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        updateTextColor();
    }

    void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        updateTextColor();
    }
}
