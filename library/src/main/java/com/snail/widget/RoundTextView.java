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
        textViewHandler.setStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvStrokeWidth, 0));
        textViewHandler.setNormalStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvNormalStrokeWidth, -1));
        textViewHandler.setPressedStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvPressedStrokeWidth, -1));
        textViewHandler.setDisabledStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvDisabledStrokeWidth, -1));
        textViewHandler.setSelectedStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvSelectedStrokeWidth, -1));
        textViewHandler.setNormalFillColor(a.getColor(R.styleable.RoundTextView_rtvNormalFillColor, Color.LTGRAY));
        textViewHandler.setCornerRadius(a.getDimensionPixelOffset(R.styleable.RoundTextView_rtvCornerRadius, -1));
        textViewHandler.setNormalStrokeColor(a.getColor(R.styleable.RoundTextView_rtvNormalStrokeColor, textViewHandler.getNormalFillColor()));
        textViewHandler.setNormalTextColor(a.getColor(R.styleable.RoundTextView_rtvNormalTextColor, getCurrentTextColor()));
        textViewHandler.setPressedStrokeColor(a.getColor(R.styleable.RoundTextView_rtvPressedStrokeColor, textViewHandler.getNormalStrokeColor()));
        textViewHandler.setPressedTextColor(a.getColor(R.styleable.RoundTextView_rtvPressedTextColor, textViewHandler.getNormalTextColor()));
        textViewHandler.setPressedFillColor(a.getColor(R.styleable.RoundTextView_rtvPressedFillColor, textViewHandler.getNormalFillColor()));
        textViewHandler.setSelectedStrokeColor(a.getColor(R.styleable.RoundTextView_rtvSelectedStrokeColor, textViewHandler.getNormalStrokeColor()));
        textViewHandler.setSelectedTextColor(a.getColor(R.styleable.RoundTextView_rtvSelectedTextColor, textViewHandler.getNormalTextColor()));
        textViewHandler.setSelectedFillColor(a.getColor(R.styleable.RoundTextView_rtvSelectedFillColor, textViewHandler.getNormalFillColor()));
        textViewHandler.setDisabledStrokeColor(a.getColor(R.styleable.RoundTextView_rtvDisabledStrokeColor, Color.LTGRAY));
        textViewHandler.setDisabledFillColor(a.getColor(R.styleable.RoundTextView_rtvDisabledFillColor, Color.LTGRAY));
        textViewHandler.setDisabledTextColor(a.getColor(R.styleable.RoundTextView_rtvDisabledTextColor, getCurrentTextColor()));
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
        textViewHandler.setStrokeWidth(strokeWidth);
        return this;
    }

    public RoundTextView setCornerRadius(int cornerRadius) {
        textViewHandler.setCornerRadius(cornerRadius);
        return this;
    }

    public RoundTextView setNormalStrokeColor(int normalStrokeColor) {
        textViewHandler.setNormalStrokeColor(normalStrokeColor);
        return this;
    }

    public RoundTextView setNormalFillColor(int normalFillColor) {
        textViewHandler.setNormalFillColor(normalFillColor);
        return this;
    }

    public RoundTextView setPressedStrokeColor(int pressedStrokeColor) {
        textViewHandler.setPressedStrokeColor(pressedStrokeColor);
        return this;
    }

    public RoundTextView setPressedFillColor(int pressedFillColor) {
        textViewHandler.setPressedFillColor(pressedFillColor);
        return this;
    }

    public RoundTextView setDisabledStrokeColor(int disabledStrokeColor) {
        textViewHandler.setDisabledStrokeColor(disabledStrokeColor);
        return this;
    }

    public RoundTextView setDisabledFillColor(int disabledFillColor) {
        textViewHandler.setDisabledFillColor(disabledFillColor);
        return this;
    }

    public RoundTextView setSelectedStrokeColor(int selectedStrokeColor) {
        textViewHandler.setSelectedStrokeColor(selectedStrokeColor);
        return this;
    }

    public RoundTextView setSelectedFillColor(int selectedFillColor) {
        textViewHandler.setSelectedFillColor(selectedFillColor);
        return this;
    }

    public int getCornerRadius() {
        return textViewHandler.getCornerRadius();
    }

    public int getNormalStrokeColor() {
        return textViewHandler.getNormalStrokeColor();
    }

    public int getNormalFillColor() {
        return textViewHandler.getNormalFillColor();
    }

    public int getPressedStrokeColor() {
        return textViewHandler.getPressedStrokeColor();
    }

    public int getPressedFillColor() {
        return textViewHandler.getPressedFillColor();
    }

    public int getDisabledStrokeColor() {
        return textViewHandler.getDisabledStrokeColor();
    }

    public int getDisabledFillColor() {
        return textViewHandler.getDisabledFillColor();
    }

    public int getStrokeWidth() {
        return textViewHandler.getStrokeWidth();
    }

    public int getNormalStrokeWidth() {
        return textViewHandler.getNormalStrokeWidth();
    }

    public RoundTextView setNormalStrokeWidth(int normalStrokeWidth) {
        textViewHandler.setNormalStrokeWidth(normalStrokeWidth);
        return this;
    }

    public int getPressedStrokeWidth() {
        return textViewHandler.getPressedStrokeWidth();
    }

    public RoundTextView setPressedStrokeWidth(int pressedStrokeWidth) {
        textViewHandler.setPressedStrokeWidth(pressedStrokeWidth);
        return this;
    }

    public int getDisabledStrokeWidth() {
        return textViewHandler.getDisabledStrokeWidth();
    }

    public RoundTextView setDisabledStrokeWidth(int disabledStrokeWidth) {
        textViewHandler.setDisabledStrokeWidth(disabledStrokeWidth);
        return this;
    }

    public int getSelectedStrokeColor() {
        return textViewHandler.getSelectedStrokeColor();
    }

    public int getSelectedStrokeWidth() {
        return textViewHandler.getSelectedStrokeWidth();
    }

    public RoundTextView setSelectedStrokeWidth(int selectedStrokeWidth) {
        textViewHandler.setSelectedStrokeWidth(selectedStrokeWidth);
        return this;
    }

    public int getSelectedFillColor() {
        return textViewHandler.getSelectedFillColor();
    }
}
