package com.snail.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * 圆角背景文字
 */
public class RoundTextView extends android.support.v7.widget.AppCompatTextView {
    private CustomRoundTextViewHandler<RoundTextView> textViewHandler;

    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);        
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        textViewHandler = new CustomRoundTextViewHandler<>(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        textViewHandler.strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvStrokeWidth, 0);
        textViewHandler.normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvNormalStrokeWidth, -1);
        textViewHandler.pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvPressedStrokeWidth, -1);
        textViewHandler.disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvDisabledStrokeWidth, -1);
        textViewHandler.selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvSelectedStrokeWidth, -1);
        textViewHandler.normalFillColor = a.getColor(R.styleable.RoundTextView_rtvNormalFillColor, Color.LTGRAY);
        textViewHandler.cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvCornerRadius, -1);
        textViewHandler.normalStrokeColor = a.getColor(R.styleable.RoundTextView_rtvNormalStrokeColor, textViewHandler.normalFillColor);
        textViewHandler.normalTextColor = a.getColor(R.styleable.RoundTextView_rtvNormalTextColor, getCurrentTextColor());
        textViewHandler.pressedStrokeColor = a.getColor(R.styleable.RoundTextView_rtvPressedStrokeColor, textViewHandler.normalStrokeColor);
        textViewHandler.pressedTextColor = a.getColor(R.styleable.RoundTextView_rtvPressedTextColor, textViewHandler.normalTextColor);
        textViewHandler.pressedFillColor = a.getColor(R.styleable.RoundTextView_rtvPressedFillColor, textViewHandler.normalFillColor);
        textViewHandler.selectedStrokeColor = a.getColor(R.styleable.RoundTextView_rtvSelectedStrokeColor, textViewHandler.normalStrokeColor);
        textViewHandler.selectedTextColor = a.getColor(R.styleable.RoundTextView_rtvSelectedTextColor, textViewHandler.normalTextColor);
        textViewHandler.selectedFillColor = a.getColor(R.styleable.RoundTextView_rtvSelectedFillColor, textViewHandler.normalFillColor);
        textViewHandler.disabledStrokeColor = a.getColor(R.styleable.RoundTextView_rtvDisabledStrokeColor, Color.LTGRAY);
        textViewHandler.disabledFillColor = a.getColor(R.styleable.RoundTextView_rtvDisabledFillColor, Color.LTGRAY);
        textViewHandler.disabledTextColor = a.getColor(R.styleable.RoundTextView_rtvDisabledTextColor, getCurrentTextColor());
        a.recycle();
        textViewHandler.updateTextColor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textViewHandler.onMeasure(heightMeasureSpec);
    }

    public RoundTextView setTextColor(int normal, int pressed, int selected, int disabled) {
        textViewHandler.setTextColor(normal, pressed, selected, disabled);
        return this;
    }

    public RoundTextView setNormalTextColor(int normalTextColor) {
        textViewHandler.setNormalTextColor(normalTextColor);
        return this;
    }

    public RoundTextView setPressedTextColor(int pressedTextColor) {
        textViewHandler.setPressedTextColor(pressedTextColor);
        return this;
    }

    public RoundTextView setDisabledTextColor(int disabledTextColor) {
        textViewHandler.setDisabledTextColor(disabledTextColor);
        return this;
    }

    public RoundTextView setSelectedTextColor(int selectedTextColor) {
        textViewHandler.setSelectedTextColor(selectedTextColor);
        return this;
    }
    
    public RoundTextView setStrokeWidth(int strokeWidth) {
        textViewHandler.strokeWidth = strokeWidth;
        return this;
    }

    public RoundTextView setCornerRadius(int cornerRadius) {
        textViewHandler.cornerRadius = cornerRadius;
        return this;
    }

    public RoundTextView setNormalStrokeColor(int normalStrokeColor) {
        textViewHandler.normalStrokeColor = normalStrokeColor;
        return this;
    }

    public RoundTextView setNormalFillColor(int normalFillColor) {
        textViewHandler.normalFillColor = normalFillColor;
        return this;
    }

    public RoundTextView setPressedStrokeColor(int pressedStrokeColor) {
        textViewHandler.pressedStrokeColor = pressedStrokeColor;
        return this;
    }

    public RoundTextView setPressedFillColor(int pressedFillColor) {
        textViewHandler.pressedFillColor = pressedFillColor;
        return this;
    }

    public RoundTextView setDisabledStrokeColor(int disabledStrokeColor) {
        textViewHandler.disabledStrokeColor = disabledStrokeColor;
        return this;
    }

    public RoundTextView setDisabledFillColor(int disabledFillColor) {
        textViewHandler.disabledFillColor = disabledFillColor;
        return this;
    }

    public RoundTextView setSelectedStrokeColor(int selectedStrokeColor) {
        textViewHandler.selectedStrokeColor = selectedStrokeColor;
        return this;
    }

    public RoundTextView setSelectedFillColor(int selectedFillColor) {
        textViewHandler.selectedFillColor = selectedFillColor;
        return this;
    }

    public int getCornerRadius() {
        return textViewHandler.cornerRadius;
    }

    public int getNormalStrokeColor() {
        return textViewHandler.normalStrokeColor;
    }

    public int getNormalFillColor() {
        return textViewHandler.normalFillColor;
    }

    public int getPressedStrokeColor() {
        return textViewHandler.pressedStrokeColor;
    }

    public int getPressedFillColor() {
        return textViewHandler.pressedFillColor;
    }

    public int getDisabledStrokeColor() {
        return textViewHandler.disabledStrokeColor;
    }

    public int getDisabledFillColor() {
        return textViewHandler.disabledFillColor;
    }

    public int getStrokeWidth() {
        return textViewHandler.strokeWidth;
    }

    public int getNormalStrokeWidth() {
        return textViewHandler.normalStrokeWidth;
    }

    public RoundTextView setNormalStrokeWidth(int normalStrokeWidth) {
        textViewHandler.normalStrokeWidth = normalStrokeWidth;
        return this;
    }

    public int getPressedStrokeWidth() {
        return textViewHandler.pressedStrokeWidth;
    }

    public RoundTextView setPressedStrokeWidth(int pressedStrokeWidth) {
        textViewHandler.pressedStrokeWidth = pressedStrokeWidth;
        return this;
    }

    public int getDisabledStrokeWidth() {
        return textViewHandler.disabledStrokeWidth;
    }

    public RoundTextView setDisabledStrokeWidth(int disabledStrokeWidth) {
        textViewHandler.disabledStrokeWidth = disabledStrokeWidth;
        return this;
    }

    public int getSelectedStrokeColor() {
        return textViewHandler.selectedStrokeColor;
    }

    public int getSelectedStrokeWidth() {
        return textViewHandler.selectedStrokeWidth;
    }

    public RoundTextView setSelectedStrokeWidth(int selectedStrokeWidth) {
        textViewHandler.selectedStrokeWidth = selectedStrokeWidth;
        return this;
    }

    public int getSelectedFillColor() {
        return textViewHandler.selectedFillColor;
    }
}
