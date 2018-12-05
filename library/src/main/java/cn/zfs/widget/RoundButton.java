package cn.zfs.widget;

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
        textViewHandler.strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbStrokeWidth, 0);
        textViewHandler.normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbNormalStrokeWidth, -1);
        textViewHandler.pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbPressedStrokeWidth, -1);
        textViewHandler.disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbDisabledStrokeWidth, -1);
        textViewHandler.selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_rbSelectedStrokeWidth, -1);
        textViewHandler.normalFillColor = a.getColor(R.styleable.RoundButton_rbNormalFillColor, Color.LTGRAY);
        textViewHandler.cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundButton_rbCornerRadius, -1);
        textViewHandler.normalStrokeColor = a.getColor(R.styleable.RoundButton_rbNormalStrokeColor, textViewHandler.normalFillColor);
        textViewHandler.normalTextColor = a.getColor(R.styleable.RoundButton_rbNormalTextColor, getCurrentTextColor());
        textViewHandler.pressedStrokeColor = a.getColor(R.styleable.RoundButton_rbPressedStrokeColor, textViewHandler.normalStrokeColor);
        textViewHandler.pressedTextColor = a.getColor(R.styleable.RoundButton_rbPressedTextColor, textViewHandler.normalTextColor);
        textViewHandler.pressedFillColor = a.getColor(R.styleable.RoundButton_rbPressedFillColor, textViewHandler.normalFillColor);
        textViewHandler.selectedStrokeColor = a.getColor(R.styleable.RoundButton_rbSelectedStrokeColor, textViewHandler.normalStrokeColor);
        textViewHandler.selectedTextColor = a.getColor(R.styleable.RoundButton_rbSelectedTextColor, textViewHandler.normalTextColor);
        textViewHandler.selectedFillColor = a.getColor(R.styleable.RoundButton_rbSelectedFillColor, textViewHandler.normalFillColor);
        textViewHandler.disabledStrokeColor = a.getColor(R.styleable.RoundButton_rbDisabledStrokeColor, Color.LTGRAY);
        textViewHandler.disabledFillColor = a.getColor(R.styleable.RoundButton_rbDisabledFillColor, Color.LTGRAY);
        textViewHandler.disabledTextColor = a.getColor(R.styleable.RoundButton_rbDisabledTextColor, getCurrentTextColor());
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
        textViewHandler.strokeWidth = strokeWidth;
        return this;
    }

    public RoundButton setCornerRadius(int cornerRadius) {
        textViewHandler.cornerRadius = cornerRadius;
        return this;
    }

    public RoundButton setNormalStrokeColor(int normalStrokeColor) {
        textViewHandler.normalStrokeColor = normalStrokeColor;
        return this;
    }

    public RoundButton setNormalFillColor(int normalFillColor) {
        textViewHandler.normalFillColor = normalFillColor;
        return this;
    }

    public RoundButton setPressedStrokeColor(int pressedStrokeColor) {
        textViewHandler.pressedStrokeColor = pressedStrokeColor;
        return this;
    }

    public RoundButton setPressedFillColor(int pressedFillColor) {
        textViewHandler.pressedFillColor = pressedFillColor;
        return this;
    }

    public RoundButton setDisabledStrokeColor(int disabledStrokeColor) {
        textViewHandler.disabledStrokeColor = disabledStrokeColor;
        return this;
    }

    public RoundButton setDisabledFillColor(int disabledFillColor) {
        textViewHandler.disabledFillColor = disabledFillColor;
        return this;
    }

    public RoundButton setSelectedStrokeColor(int selectedStrokeColor) {
        textViewHandler.selectedStrokeColor = selectedStrokeColor;
        return this;
    }

    public RoundButton setSelectedFillColor(int selectedFillColor) {
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

    public RoundButton setNormalStrokeWidth(int normalStrokeWidth) {
        textViewHandler.normalStrokeWidth = normalStrokeWidth;
        return this;
    }

    public int getPressedStrokeWidth() {
        return textViewHandler.pressedStrokeWidth;
    }

    public RoundButton setPressedStrokeWidth(int pressedStrokeWidth) {
        textViewHandler.pressedStrokeWidth = pressedStrokeWidth;
        return this;
    }

    public int getDisabledStrokeWidth() {
        return textViewHandler.disabledStrokeWidth;
    }

    public RoundButton setDisabledStrokeWidth(int disabledStrokeWidth) {
        textViewHandler.disabledStrokeWidth = disabledStrokeWidth;
        return this;
    }

    public int getSelectedStrokeColor() {
        return textViewHandler.selectedStrokeColor;
    }

    public int getSelectedStrokeWidth() {
        return textViewHandler.selectedStrokeWidth;
    }

    public RoundButton setSelectedStrokeWidth(int selectedStrokeWidth) {
        textViewHandler.selectedStrokeWidth = selectedStrokeWidth;
        return this;
    }

    public int getSelectedFillColor() {
        return textViewHandler.selectedFillColor;
    }
}
