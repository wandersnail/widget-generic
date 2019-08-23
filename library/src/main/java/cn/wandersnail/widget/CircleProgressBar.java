package cn.wandersnail.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;

/**
 * 圆形进度条，风格有环形和填充。进度条圆环支持颜色渐变
 * <p>
 * date: 2019/8/22 23:41
 * author: zengfansheng
 */
public class CircleProgressBar extends View {
    public static final int COLOR_STYLE_SOLID = 0;
    public static final int COLOR_STYLE_GRADIENT = 1;
    //最大进度
    private int max = 100;
    //当前进度
    private int progress;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //用于定义的圆弧的形状和大小的界限
    private RectF oval = new RectF();
    //用于记录上次渐变色的参数
    private float cx;
    private float cy;
    //上次渐变色实例
    private SweepGradient sweepGradient;
    //是否实例化过渐变色
    private boolean isInstantiated;
    private Builder builder = new Builder(this);

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
            builder.isTextVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_wswTextVisible, true);
            builder.textSize = typedArray.getDimension(R.styleable.CircleProgressBar_wswTextSize, 24f);
            builder.backgroundCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_wswBackgroundCircleWidth, 5f);
            max = typedArray.getInt(R.styleable.CircleProgressBar_wswMax, 100);
            progress = typedArray.getInt(R.styleable.CircleProgressBar_wswProgress, 0);
            builder.circleStyle = intToStyle(typedArray.getInt(R.styleable.CircleProgressBar_wswCircleStyle, Paint.Style.STROKE.ordinal()));
            builder.isDotVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_wswDotVisible, false);
            builder.isBackgroundCircleVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_wswBackgroundCircleVisible, true);
            builder.startColor = typedArray.getColor(R.styleable.CircleProgressBar_wswStartColor, -0x3a2acc);
            builder.endColor = typedArray.getColor(R.styleable.CircleProgressBar_wswEndColor, -0xad485e);
            builder.progressCircleWidth = typedArray.getDimension(R.styleable.CircleProgressBar_wswProgressCircleWidth, builder.backgroundCircleWidth);
            builder.backgroundCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_wswBackgroundCircleColor, -0x101011);
            builder.progressCircleColor = typedArray.getColor(R.styleable.CircleProgressBar_wswProgressCircleColor, -0x6f15ed);
            builder.textColor = typedArray.getColor(R.styleable.CircleProgressBar_wswTextColor, -0x6f15ed);
            builder.dotRadius = typedArray.getDimension(R.styleable.CircleProgressBar_wswDotRadius, 0f);
            builder.startAngle = typedArray.getFloat(R.styleable.CircleProgressBar_wswStartAngle, 0f);
            builder.cap = intToCap(typedArray.getInt(R.styleable.CircleProgressBar_wswStrokeCap, Paint.Cap.BUTT.ordinal()));
            builder.colorStyle = typedArray.getInt(R.styleable.CircleProgressBar_wswColorStyle, COLOR_STYLE_SOLID);
            typedArray.recycle();
        }
    }

    private Paint.Cap intToCap(int cap) {
        switch (cap) {
            case 2:
                return Paint.Cap.SQUARE;
            case 1:
                return Paint.Cap.ROUND;
            default:
                return Paint.Cap.BUTT;
        }
    }

    private Paint.Style intToStyle(int style) {
        switch (style) {
            case 2:
                return Paint.Style.FILL_AND_STROKE;
            case 0:
                return Paint.Style.FILL;
            default:
                return Paint.Style.STROKE;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setShader(null);
        paint.setStyle(builder.circleStyle);
        paint.setStrokeCap(builder.cap);
        //画最外层的大圆环
        int centerX = getWidth() / 2; //获取圆心的x坐标
        float dotRadius = builder.dotRadius == 0f ? builder.progressCircleWidth / 2 : builder.dotRadius;
        float radius = centerX - dotRadius - builder.backgroundCircleWidth / 2; //圆环的半径

        if (builder.isBackgroundCircleVisible) {
            paint.setColor(builder.backgroundCircleColor); //设置圆环的颜色
            paint.setStrokeWidth(builder.backgroundCircleWidth); //设置圆环的宽度
            canvas.drawCircle(centerX, centerX, radius, paint); //画出圆环 
        }

        //画进度百分比
        if (builder.isTextVisible) {
            paint.setStrokeWidth(0);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(builder.textColor);
            paint.setTextSize(builder.textSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
            String text = builder.textFormat.format((progress * 100f / max)) + "%";
            Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
            int baseline = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
            paint.setTextAlign(Paint.Align.CENTER);
            //画出进度百分比
            canvas.drawText(text, getWidth() / 2, baseline, paint);
        }

        //画圆弧 ，画圆环的进度
        canvas.rotate(-180 + builder.startAngle, centerX, centerX);
        paint.setStyle(builder.circleStyle);
        paint.setStrokeWidth(builder.progressCircleWidth); //设置圆环的宽度

        oval.set(centerX - radius, centerX - radius, centerX + radius, centerX + radius);
        SweepGradient shader = null;
        switch (builder.colorStyle) {
            case COLOR_STYLE_SOLID:
                paint.setShader(null);
                paint.setColor(builder.progressCircleColor);//设置进度的颜色
                break;
            case COLOR_STYLE_GRADIENT:
                //设置环形渐变色
                shader = getShader(centerX, centerX);
                paint.setShader(shader);
                break;
        }
        if (builder.circleStyle == Paint.Style.STROKE) {
            canvas.drawArc(oval, 0f, 360f * progress / max, false, paint);  //根据进度画圆弧	
        } else {
            canvas.drawArc(oval, 0f, 360f * progress / max, true, paint);  //根据进度画圆
        }

        //画圆环上小圆点
        if (builder.isDotVisible) {
            paint.setStyle(Paint.Style.FILL);
            switch (builder.colorStyle) {
                case COLOR_STYLE_SOLID:
                    paint.setColor(builder.progressCircleColor);
                    break;
                case COLOR_STYLE_GRADIENT:
                    if (progress <= 0) {
                        paint.setShader(null);
                        paint.setColor(builder.isArrayColorEnabled ? builder.colors[0] : builder.startColor);
                    } else if (progress >= max) {
                        paint.setShader(null);
                        paint.setColor(builder.isArrayColorEnabled ? builder.colors[builder.colors.length - 1] : builder.endColor);
                    } else {
                        paint.setShader(shader);
                    }
                    break;
            }
            //小圆点到圆环的半径
            canvas.drawCircle((float) (centerX + radius * Math.cos(Math.toRadians(360f * progress / max))),
                    (float) (centerX + radius * Math.sin(Math.toRadians(360f * progress / max))), dotRadius, paint);
        }
    }

    //获取渐变色实例，避免在onDraw中多次实例化
    private SweepGradient getShader(float cx, float cy) {
        if (this.cx != cx || this.cy != cy || !isInstantiated) {
            this.cx = cx;
            this.cy = cy;
            isInstantiated = true;            
            if (builder.isArrayColorEnabled) {
                sweepGradient = new SweepGradient(cx, cy, builder.colors, null);
            } else {
                sweepGradient = new SweepGradient(cx, cy, builder.startColor, builder.endColor);
            }
        }
        return sweepGradient;
    }

    public int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
        postInvalidate();
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }
    }

    public static class Builder {
        CircleProgressBar progressBar;
        //用于设置渐变色的颜色集
        int[] colors;
        //用于设置渐变色的开始色
        int startColor = -0x3a2acc;
        //用于设置渐变色的结束色
        int endColor = -0xad485e;
        //背景圆环的颜色
        int backgroundCircleColor = -0x101011;
        //圆环进度的颜色
        int progressCircleColor = -0x6f15ed;
        //中间进度百分比的字符串的颜色
        int textColor = -0x6f15ed;
        //中间进度百分比的字符串的字体
        float textSize = 24f;
        //背景圆环的宽度
        float backgroundCircleWidth = 5f;
        //进度圆环的宽度
        float progressCircleWidth = backgroundCircleWidth;
        //小圆点的半径
        float dotRadius;
        //是否显示中间的进度
        boolean isTextVisible;
        //是否画小圆点
        boolean isDotVisible;
        //是否显示默认圆环
        boolean isBackgroundCircleVisible = true;
        //渐变色是否使用颜色数组
        boolean isArrayColorEnabled;
        //进度条的风格
        Paint.Style circleStyle = Paint.Style.STROKE;
        //进度环的颜色风格
        int colorStyle = COLOR_STYLE_SOLID;
        //进度条帽的形状
        Paint.Cap cap = Paint.Cap.BUTT;
        //进度条开始角度
        float startAngle;
        DecimalFormat textFormat = new DecimalFormat("#");

        Builder(CircleProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        /**
         * 设置进度条开始角度，0~360
         */
        public Builder setStartAngle(float startAngle) {
            if (startAngle > 360) {
                startAngle = 360f;
            } else if (startAngle < 0) {
                startAngle = 0f;
            }
            this.startAngle = startAngle;
            return this;
        }

        /**
         * 进度条帽的形状
         */
        public Builder setStrokeCap(@NonNull Paint.Cap cap) {
            this.cap = cap;
            return this;
        }

        /**
         * 设置进度圆环的颜色风格
         */
        public Builder setColorStyle(int colorStyle) {
            this.colorStyle = colorStyle;
            return this;
        }

        /**
         * 设置背景圆环的颜色
         */
        public Builder setBackgroundCircleColor(int backgroundCircleColor) {
            this.backgroundCircleColor = backgroundCircleColor;
            return this;
        }

        /**
         * 设置进度圆环颜色
         */
        public Builder setProgressCircleColor(int progressCircleColor) {
            this.progressCircleColor = progressCircleColor;
            return this;
        }

        /**
         * 设置进度圆环颜色
         */
        public Builder setProgressCircleColor(@NonNull int[] colors) {
            this.colors = colors;
            isArrayColorEnabled = true;
            return this;
        }

        /**
         * 设置进度圆环颜色
         */
        public Builder setProgressCircleColor(int startColor, int endColor) {
            this.startColor = startColor;
            this.endColor = endColor;
            isArrayColorEnabled = false;
            return this;
        }

        /**
         * 设置进度文本的颜色
         */
        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        /**
         * 设置进度文本的大小
         */
        public Builder setTextSize(float textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 设置背景圆环的宽度
         */
        public Builder setBackgroundCircleWidth(float backgroundCircleWidth) {
            this.backgroundCircleWidth = backgroundCircleWidth;
            return this;
        }

        /**
         * 设置进度圆环的宽度
         */
        public Builder setProgressCircleWidth(float progressCircleWidth) {
            this.progressCircleWidth = progressCircleWidth;
            return this;
        }

        /**
         * 设置小圆点的半径长度
         */
        public Builder setDotRadius(float dotRadius) {
            this.dotRadius = dotRadius;
            return this;
        }

        /**
         * 设置进度条整体风格
         */
        public Builder setCircleStyle(Paint.Style circleStyle) {
            this.circleStyle = circleStyle;
            return this;
        }

        /**
         * 设置进度文本是否显示
         */
        public Builder setTextVisible(boolean textVisible) {
            isTextVisible = textVisible;
            return this;
        }

        /**
         * 设置小圆点是否显示
         */
        public Builder setDotVisible(boolean dotVisible) {
            isDotVisible = dotVisible;
            return this;
        }

        /**
         * 设置默认圆环是否显示
         */
        public Builder setBackgroundCircleVisible(boolean backgroundCircleVisible) {
            isBackgroundCircleVisible = backgroundCircleVisible;
            return this;
        }

        /**
         * 设置百分比文本显示格式
         */
        public Builder setTextFormat(DecimalFormat format) {
            textFormat = format;
            return this;
        }

        public void build() {
            progressBar.postInvalidate();
        }
    }
}
