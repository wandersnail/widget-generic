package cn.wandersnail.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatTextView;

import cn.wandersnail.widget.R;

/**
 * 可旋转的TextView
 * <p>
 * date: 2019/8/22 23:04
 * author: zengfansheng
 */
public class RotatableTextView extends AppCompatTextView {
    private float degree;

    public RotatableTextView(Context context) {
        super(context);
        init(null);
    }

    public RotatableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context.obtainStyledAttributes(attrs, R.styleable.RotatableTextView));
    }

    public RotatableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context.obtainStyledAttributes(attrs, R.styleable.RotatableTextView, defStyleAttr, 0));
    }

    private void init(TypedArray a) {
        setGravity(Gravity.CENTER);
        if (a != null) {
            degree = a.getFloat(R.styleable.RotatableTextView_wswDegree, degree);
            a.recycle();
        }
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        canvas.rotate(degree, getWidth() / 2f, getHeight() / 2f);
        super.onDraw(canvas);
        canvas.restore();
    }
}
