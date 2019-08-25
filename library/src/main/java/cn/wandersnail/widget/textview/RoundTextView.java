package cn.wandersnail.widget.textview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import cn.wandersnail.widget.R;
import cn.wandersnail.widget.WidgetUtils;

/**
 * date: 2019/8/22 23:08
 * author: zengfansheng
 */
public class RoundTextView extends AppCompatTextView {
    private int strokeWidth;
    private int cornerRadius = -1;
    private int normalTextColor;
    private int normalStrokeWidth = -1;
    private int normalFillColor = Color.LTGRAY;
    private int normalStrokeColor = normalFillColor;
    private int pressedTextColor;
    private int pressedStrokeColor = normalStrokeColor;
    private int pressedStrokeWidth = -1;
    private int pressedFillColor = normalFillColor;
    private int disabledTextColor;
    private int disabledStrokeColor = Color.LTGRAY;
    private int disabledStrokeWidth = -1;
    private int disabledFillColor = Color.LTGRAY;
    private int selectedTextColor;
    private int selectedStrokeColor = normalStrokeColor;
    private int selectedStrokeWidth = -1;
    private int selectedFillColor = normalFillColor;

    private int rippleColor = Color.TRANSPARENT;

    public RoundTextView(Context context) {
        super(context);
        init(null);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context.obtainStyledAttributes(attrs, R.styleable.RoundTextView));
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context.obtainStyledAttributes(attrs, R.styleable.RoundTextView, defStyleAttr, 0));
    }

    private void init(TypedArray a) {
        normalTextColor = getCurrentTextColor();
        pressedTextColor = normalTextColor;
        disabledTextColor = normalTextColor;
        selectedTextColor = normalTextColor;
        if (a != null) {
            strokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswStrokeWidth, strokeWidth);
            normalStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswNormalStrokeWidth, normalStrokeWidth);
            pressedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswPressedStrokeWidth, pressedStrokeWidth);
            disabledStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswDisabledStrokeWidth, disabledStrokeWidth);
            selectedStrokeWidth = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswSelectedStrokeWidth, selectedStrokeWidth);
            normalFillColor = a.getColor(R.styleable.RoundTextView_wswNormalFillColor, normalFillColor);
            cornerRadius = a.getDimensionPixelOffset(R.styleable.RoundTextView_wswCornerRadius, cornerRadius);
            normalStrokeColor = a.getColor(R.styleable.RoundTextView_wswNormalStrokeColor, normalStrokeColor);
            normalTextColor = a.getColor(R.styleable.RoundTextView_wswNormalTextColor, normalTextColor);
            pressedStrokeColor = a.getColor(R.styleable.RoundTextView_wswPressedStrokeColor, pressedStrokeColor);
            pressedTextColor = a.getColor(R.styleable.RoundTextView_wswPressedTextColor, pressedTextColor);
            pressedFillColor = a.getColor(R.styleable.RoundTextView_wswPressedFillColor, pressedFillColor);
            selectedStrokeColor = a.getColor(R.styleable.RoundTextView_wswSelectedStrokeColor, selectedStrokeColor);
            selectedTextColor = a.getColor(R.styleable.RoundTextView_wswSelectedTextColor, selectedTextColor);
            selectedFillColor = a.getColor(R.styleable.RoundTextView_wswSelectedFillColor, selectedFillColor);
            disabledStrokeColor = a.getColor(R.styleable.RoundTextView_wswDisabledStrokeColor, disabledStrokeColor);
            disabledFillColor = a.getColor(R.styleable.RoundTextView_wswDisabledFillColor, disabledFillColor);
            disabledTextColor = a.getColor(R.styleable.RoundTextView_wswDisabledTextColor, disabledTextColor);
            rippleColor = a.getColor(R.styleable.RoundTextView_wswRippleColor, rippleColor);
            a.recycle();
        }
        updateTextColor();
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public int getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
        updateTextColor();
    }

    public int getNormalStrokeWidth() {
        return normalStrokeWidth;
    }

    public void setNormalStrokeWidth(int normalStrokeWidth) {
        this.normalStrokeWidth = normalStrokeWidth;
    }

    public int getNormalFillColor() {
        return normalFillColor;
    }

    public void setNormalFillColor(int normalFillColor) {
        this.normalFillColor = normalFillColor;
    }

    public int getNormalStrokeColor() {
        return normalStrokeColor;
    }

    public void setNormalStrokeColor(int normalStrokeColor) {
        this.normalStrokeColor = normalStrokeColor;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public void setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
        updateTextColor();
    }

    public int getPressedStrokeColor() {
        return pressedStrokeColor;
    }

    public void setPressedStrokeColor(int pressedStrokeColor) {
        this.pressedStrokeColor = pressedStrokeColor;
    }

    public int getPressedStrokeWidth() {
        return pressedStrokeWidth;
    }

    public void setPressedStrokeWidth(int pressedStrokeWidth) {
        this.pressedStrokeWidth = pressedStrokeWidth;
    }

    public int getPressedFillColor() {
        return pressedFillColor;
    }

    public void setPressedFillColor(int pressedFillColor) {
        this.pressedFillColor = pressedFillColor;
    }

    public int getDisabledTextColor() {
        return disabledTextColor;
    }

    public void setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        updateTextColor();
    }

    public int getDisabledStrokeColor() {
        return disabledStrokeColor;
    }

    public void setDisabledStrokeColor(int disabledStrokeColor) {
        this.disabledStrokeColor = disabledStrokeColor;
    }

    public int getDisabledStrokeWidth() {
        return disabledStrokeWidth;
    }

    public void setDisabledStrokeWidth(int disabledStrokeWidth) {
        this.disabledStrokeWidth = disabledStrokeWidth;
    }

    public int getDisabledFillColor() {
        return disabledFillColor;
    }

    public void setDisabledFillColor(int disabledFillColor) {
        this.disabledFillColor = disabledFillColor;
    }

    public int getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        updateTextColor();
    }

    public int getSelectedStrokeColor() {
        return selectedStrokeColor;
    }

    public void setSelectedStrokeColor(int selectedStrokeColor) {
        this.selectedStrokeColor = selectedStrokeColor;
    }

    public int getSelectedStrokeWidth() {
        return selectedStrokeWidth;
    }

    public void setSelectedStrokeWidth(int selectedStrokeWidth) {
        this.selectedStrokeWidth = selectedStrokeWidth;
    }

    public int getSelectedFillColor() {
        return selectedFillColor;
    }

    public void setSelectedFillColor(int selectedFillColor) {
        this.selectedFillColor = selectedFillColor;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (cornerRadius == -1) {
            cornerRadius = height;
        }
        updateBackground();
    }

    /**
     * 修改了参数后，调用此方法生效
     */
    public void updateBackground() {
        Drawable drawable = WidgetUtils.createStateListDrawable(
                WidgetUtils.createDrawable(normalFillColor, normalStrokeWidth == -1 ? strokeWidth : normalStrokeWidth,
                        normalStrokeColor, cornerRadius),
                WidgetUtils.createDrawable(pressedFillColor, pressedStrokeWidth == -1 ? strokeWidth : pressedStrokeWidth,
                        pressedStrokeColor, cornerRadius),
                WidgetUtils.createDrawable(selectedFillColor, selectedStrokeWidth == -1 ? strokeWidth : selectedStrokeWidth,
                        selectedStrokeColor, cornerRadius),
                WidgetUtils.createDrawable(disabledFillColor, disabledStrokeWidth == -1 ? strokeWidth : disabledStrokeWidth,
                        disabledStrokeColor, cornerRadius)
        );
        if (rippleColor != Color.TRANSPARENT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setBackground(new RippleDrawable(ColorStateList.valueOf(rippleColor), drawable, null));
        } else {
            setBackground(drawable);
        }
    }
    
    public void setTextColor(int normal, int pressed, int selected, int disabled) {
        setTextColor(WidgetUtils.createColorStateList(normal, pressed, selected, disabled));
    }
    
    private void updateTextColor() {
        setTextColor(WidgetUtils.createColorStateList(normalTextColor, pressedTextColor, selectedTextColor, disabledTextColor));
    }
}
