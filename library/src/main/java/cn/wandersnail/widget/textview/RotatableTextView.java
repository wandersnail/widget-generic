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
        this(context, null);
    }

    public RotatableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotatableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotatableTextView);
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
