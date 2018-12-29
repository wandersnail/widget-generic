package com.snail.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by zeng on 2017/1/6.
 * 圆角背景按钮
 */
public class RoundButton extends android.support.v7.widget.AppCompatButton {
    private CustomRoundTextViewHandler<RoundButton> textViewHandler;
    
    public RoundButton(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.buttonStyle);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        textViewHandler = new CustomRoundTextViewHandler<>(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton);
        textViewHandler.setStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundButton_rbStrokeWidth, 0));
        textViewHandler.setNormalStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundButton_rbNormalStrokeWidth, -1));
        textViewHandler.setPressedStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundButton_rbPressedStrokeWidth, -1));
        textViewHandler.setDisabledStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundButton_rbDisabledStrokeWidth, -1));
        textViewHandler.setSelectedStrokeWidth(a.getDimensionPixelOffset(R.styleable.RoundButton_rbSelectedStrokeWidth, -1));
        textViewHandler.setNormalFillColor(a.getColor(R.styleable.RoundButton_rbNormalFillColor, Color.LTGRAY));
        textViewHandler.setCornerRadius(a.getDimensionPixelOffset(R.styleable.RoundButton_rbCornerRadius, -1));
        textViewHandler.setNormalStrokeColor(a.getColor(R.styleable.RoundButton_rbNormalStrokeColor, textViewHandler.getNormalFillColor()));
        textViewHandler.setNormalTextColor(a.getColor(R.styleable.RoundButton_rbNormalTextColor, getCurrentTextColor()));
        textViewHandler.setPressedStrokeColor(a.getColor(R.styleable.RoundButton_rbPressedStrokeColor, textViewHandler.getNormalStrokeColor()));
        textViewHandler.setPressedTextColor(a.getColor(R.styleable.RoundButton_rbPressedTextColor, textViewHandler.getNormalTextColor()));
        textViewHandler.setPressedFillColor(a.getColor(R.styleable.RoundButton_rbPressedFillColor, textViewHandler.getNormalFillColor()));
        textViewHandler.setSelectedStrokeColor(a.getColor(R.styleable.RoundButton_rbSelectedStrokeColor, textViewHandler.getNormalStrokeColor()));
        textViewHandler.setSelectedTextColor(a.getColor(R.styleable.RoundButton_rbSelectedTextColor, textViewHandler.getNormalTextColor()));
        textViewHandler.setSelectedFillColor(a.getColor(R.styleable.RoundButton_rbSelectedFillColor, textViewHandler.getNormalFillColor()));
        textViewHandler.setDisabledStrokeColor(a.getColor(R.styleable.RoundButton_rbDisabledStrokeColor, Color.LTGRAY));
        textViewHandler.setDisabledFillColor(a.getColor(R.styleable.RoundButton_rbDisabledFillColor, Color.LTGRAY));
        textViewHandler.setDisabledTextColor(a.getColor(R.styleable.RoundButton_rbDisabledTextColor, getCurrentTextColor()));
		boolean topBottomPaddingEnable = a.getBoolean(R.styleable.RoundButton_rbTopBottomPaddingEnable, false);
        if (!topBottomPaddingEnable) {
            setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);
        }        
        a.recycle();
        textViewHandler.updateTextColor();
        //去掉最小宽高限制
        setMinHeight(0);
        setMinWidth(0);
        //不让默认英文大写
        setAllCaps(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        textViewHandler.onMeasure(heightMeasureSpec);
    }

    public RoundButton setTextColor(int normal, int pressed, int selected, int disabled) {
        textViewHandler.setTextColor(normal, pressed, selected, disabled);
        return this;
    }

    public RoundButton setNormalTextColor(int normalTextColor) {
        textViewHandler.setNormalTextColor(normalTextColor);
        return this;
    }

    public RoundButton setPressedTextColor(int pressedTextColor) {
        textViewHandler.setPressedTextColor(pressedTextColor);
        return this;
    }

    public RoundButton setDisabledTextColor(int disabledTextColor) {
        textViewHandler.setDisabledTextColor(disabledTextColor);
        return this;
    }

    public RoundButton setSelectedTextColor(int selectedTextColor) {
        textViewHandler.setSelectedTextColor(selectedTextColor);
        return this;
    }
    
    public RoundButton setStrokeWidth(int strokeWidth) {
        textViewHandler.setStrokeWidth(strokeWidth);
        return this;
    }

    public RoundButton setCornerRadius(int cornerRadius) {
        textViewHandler.setCornerRadius(cornerRadius);
        return this;
    }

    public RoundButton setNormalStrokeColor(int normalStrokeColor) {
        textViewHandler.setNormalStrokeColor(normalStrokeColor);
        return this;
    }

    public RoundButton setNormalFillColor(int normalFillColor) {
        textViewHandler.setNormalFillColor(normalFillColor);
        return this;
    }

    public RoundButton setPressedStrokeColor(int pressedStrokeColor) {
        textViewHandler.setPressedStrokeColor(pressedStrokeColor);
        return this;
    }

    public RoundButton setPressedFillColor(int pressedFillColor) {
        textViewHandler.setPressedFillColor(pressedFillColor);
        return this;
    }

    public RoundButton setDisabledStrokeColor(int disabledStrokeColor) {
        textViewHandler.setDisabledStrokeColor(disabledStrokeColor);
        return this;
    }

    public RoundButton setDisabledFillColor(int disabledFillColor) {
        textViewHandler.setDisabledFillColor(disabledFillColor);
        return this;
    }

    public RoundButton setSelectedStrokeColor(int selectedStrokeColor) {
        textViewHandler.setSelectedStrokeColor(selectedStrokeColor);
        return this;
    }

    public RoundButton setSelectedFillColor(int selectedFillColor) {
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

    public RoundButton setNormalStrokeWidth(int normalStrokeWidth) {
        textViewHandler.setNormalStrokeWidth(normalStrokeWidth);
        return this;
    }

    public int getPressedStrokeWidth() {
        return textViewHandler.getPressedStrokeWidth();
    }

    public RoundButton setPressedStrokeWidth(int pressedStrokeWidth) {
        textViewHandler.setPressedStrokeWidth(pressedStrokeWidth);
        return this;
    }

    public int getDisabledStrokeWidth() {
        return textViewHandler.getDisabledStrokeWidth();
    }

    public RoundButton setDisabledStrokeWidth(int disabledStrokeWidth) {
        textViewHandler.setDisabledStrokeWidth(disabledStrokeWidth);
        return this;
    }

    public int getSelectedStrokeColor() {
        return textViewHandler.getSelectedStrokeColor();
    }

    public int getSelectedStrokeWidth() {
        return textViewHandler.getSelectedStrokeWidth();
    }

    public RoundButton setSelectedStrokeWidth(int selectedStrokeWidth) {
        textViewHandler.setSelectedStrokeWidth(selectedStrokeWidth);
        return this;
    }

    public int getSelectedFillColor() {
        return textViewHandler.getSelectedFillColor();
    }
}
