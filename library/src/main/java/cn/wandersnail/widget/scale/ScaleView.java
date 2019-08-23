package cn.wandersnail.widget.scale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import cn.wandersnail.widget.ListenableScroller;
import cn.wandersnail.widget.R;
import cn.wandersnail.widget.WidgetUtils;

/**
 * date: 2019/8/22 20:09
 * author: zengfansheng
 */
public class ScaleView extends View implements ListenableScroller.OnScrollListener {
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 0;
    private int min = 0;
    private int max = 100;
    private int bigStepScaleNum = 5;//隔多少个短刻度一个长刻度
    private float twoBigStepDifValue = 5f;//两个长刻度之间值的大小
    private int labelColor = -0x676768;
    private int indicatorColor = -0xe6bb;
    private int indicatorPostion = 50;
    private float labelSize;//标签字体大小
    private int scaleSpace;//刻度之间间隔
    private int labelAndScaleSpace;//标签与长刻度的间隔
    private int longScaleLen;
    private int scaleWidth;//刻度线条宽度
    private int indicatorWidth;//指示器宽
    private float shortLongtScaleRatio;//短刻度与长刻度比例，短/长
    private int orientation = HORIZONTAL;
    private boolean isEdgeDim = true;//两端边缘模糊
    private OnValueUpdateCallback updateCallback;
    private TextFormatterCallback textFormatterCallback;
    private Paint paint;
    private int labelHeight = 0;
    private float value;
    private int start = 0;
    private int contentLen = 0;
    private int scales = 0;
    private float totalValue = 0f;
    private GestureDetector gestureDetector;
    private ListenableScroller scroller;
    private boolean onFling;

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleView);
        min = typedArray.getInt(R.styleable.ScaleView_wswMin, 0);
        max = typedArray.getInt(R.styleable.ScaleView_wswMax, 100);
        bigStepScaleNum = typedArray.getInt(R.styleable.ScaleView_wswBigStepScaleNum, 5);
        twoBigStepDifValue = typedArray.getFloat(R.styleable.ScaleView_wswTwoBigStepDifValue, 5f);
        labelColor = typedArray.getColor(R.styleable.ScaleView_wswLabelColor, -0x676768);
        indicatorColor = typedArray.getColor(R.styleable.ScaleView_wswIndicatorColor, -0xe6bb);
        indicatorPostion = typedArray.getInt(R.styleable.ScaleView_wswIndicatorPostion, 50);
        labelSize = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswLabelSize, WidgetUtils.dp2px(context, 14f));
        scaleSpace = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswScaleSpace, WidgetUtils.dp2px(context, 8f));
        labelAndScaleSpace = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswLabelAndScaleSpace, WidgetUtils.dp2px(context, 20f));
        longScaleLen = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswLongScaleLen, WidgetUtils.dp2px(context, 30f));
        scaleWidth = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswScaleWidth, WidgetUtils.dp2px(context, 1f));
        indicatorWidth = typedArray.getDimensionPixelOffset(R.styleable.ScaleView_wswIndicatorWidth, WidgetUtils.dp2px(context, 3f));
        shortLongtScaleRatio = typedArray.getFloat(R.styleable.ScaleView_wswShortLongScaleRatio, 2f / 3);
        isEdgeDim = typedArray.getBoolean(R.styleable.ScaleView_wswEdgeDim, true);
        value = typedArray.getFloat(R.styleable.ScaleView_wswValue, min);
        orientation = typedArray.getInt(R.styleable.ScaleView_wswOrientation, HORIZONTAL);
        typedArray.recycle();
        init();
        updateParams();
        setValue(value);
    }

    public interface OnValueUpdateCallback {
        void onValueUpdate(float value);
    }

    /**
     * 标签文本格式
     */
    public interface TextFormatterCallback {
        String format(float value);
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(labelSize);
        String text = "0.00";
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        labelHeight = rect.bottom - rect.top;
        scroller = new ListenableScroller(getContext());
        scroller.setOnScrollListener(this);
    }

    @Override
    public void onScroll(ListenableScroller scroller) {
        doMove(null);
    }

    @Override
    public void onScrollFinish(ListenableScroller scroller) {
    }

    @Override
    public void onFlingFinish(ListenableScroller scroller) {
        autoScroll();
    }

    private int scrollOffset() {
        return orientation == HORIZONTAL ? getScrollX() : getScrollY();
    }

    private void updateParams() {
        scales = (int) ((max - min) / (twoBigStepDifValue / bigStepScaleNum));
        //刻度部分长度
        contentLen = scales * scaleSpace;
        totalValue = max - min;
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scroller.fling(getScrollX(), getScrollY(), (int) -velocityX, (int) -velocityY, -contentLen, contentLen, -contentLen, contentLen);
                onFling = true;
                return true;
            }
        });
        updateScroll();
    }

    public Params obtainParams() {
        return new Params(this);
    }

    public void setValue(float value) {
        value = alignValue(value);
        if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }
        this.value = value;
        if (updateCallback != null) {
            updateCallback.onValueUpdate(value);
        }
        updateScroll();
    }

    public float getValue() {
        return value;
    }

    private String getLabel(float value) {
        return textFormatterCallback != null ? textFormatterCallback.format(value) : String.valueOf(value);
    }

    private float alignValue(float value) {
        //算最小单位
        float unit = totalValue / scales;
        //求余
        float remainder = value % unit;
        return Math.abs(remainder) > unit / 2f ? value + unit - remainder : value - remainder;
    }

    private void autoScroll() {
        float targetValue = alignValue(value);
        int dx = orientation == HORIZONTAL ? getScrollByValue(targetValue) - getScrollX() : 0;
        int dy = orientation == HORIZONTAL ? 0 : getScrollByValue(targetValue) - getScrollY();
        scroller.startScroll(getScrollX(), getScrollY(), dx, dy);
    }

    private int getScrollByValue(float value) {
        return (int) (orientation == HORIZONTAL ? (value - min) / totalValue * contentLen :
                contentLen - (value - min) / totalValue * contentLen);
    }

    //数值改变会影响滚动的位置，需要更新
    private void updateScroll() {
        scrollToTarget(getScrollByValue(value));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //不画标签部分长度
        float blank;
        if (orientation == HORIZONTAL) {
            blank = getWidth() * indicatorPostion / 100f;
            int extraScales = (int) (blank / scaleSpace);
            float startX = blank - extraScales * scaleSpace;
            int extra = extraScales % bigStepScaleNum;
            //刻度
            paint.setStrokeWidth(scaleWidth);
            paint.setColor(labelColor);
            for (int i = 0; i <= scales + extraScales * 2; i++) {
                float scaleX = startX + i * scaleSpace;
                if (isEdgeDim) {
                    int alpha = (int) (parabola(getWidth() * 5 / 12f, getWidth() / 2f + getScrollX() - scaleX) * 255);
                    paint.setAlpha(alpha);
                }
                if ((i - extra) % bigStepScaleNum == 0) {
                    canvas.drawLine(scaleX, 0f, scaleX, longScaleLen, paint);
                    if ((i - extraScales) / bigStepScaleNum % 2 == 0 && i >= extraScales && i <= scales + extraScales) {
                        //标签
                        String label = getLabel(min + (i - extraScales) / bigStepScaleNum * twoBigStepDifValue);
                        float textWidth = paint.measureText(label);
                        canvas.drawText(label, scaleX - textWidth / 2, longScaleLen + labelAndScaleSpace + labelHeight, paint);
                    }
                } else {
                    canvas.drawLine(scaleX, 0f, scaleX, longScaleLen * shortLongtScaleRatio, paint);
                }
            }
            //指示线
            paint.setAlpha(255);
            paint.setStrokeWidth(indicatorWidth);
            paint.setColor(indicatorColor);
            canvas.drawLine(getScrollX() + blank, 0f, getScrollX() + blank, labelAndScaleSpace / 2 + longScaleLen, paint);
        } else {
            blank = getHeight() * indicatorPostion / 100f;
            int extraScales = (int) (blank / scaleSpace);
            float startY = blank - extraScales * scaleSpace;
            int extra = extraScales % bigStepScaleNum;
            //刻度
            paint.setStrokeWidth(scaleWidth);
            paint.setColor(labelColor);
            for (int i = 0; i <= scales + extraScales * 2; i++) {
                float scaleY = contentLen + blank * 2 - (startY + i * scaleSpace);
                if (isEdgeDim) {
                    int alpha = (int) (parabola(getHeight() * 5 / 12f, getHeight() / 2f + getScrollY() - scaleY) * 255);
                    paint.setAlpha(alpha);
                }
                if ((i - extra) % bigStepScaleNum == 0) {
                    canvas.drawLine(getWidth() - longScaleLen, scaleY, getWidth(), scaleY, paint);
                    if ((i - extraScales) / bigStepScaleNum % 2 == 0 && i >= extraScales && i <= scales + extraScales) {
                        //标签
                        String label = getLabel(min + (i - extraScales) / bigStepScaleNum * twoBigStepDifValue);
                        float textWidth = paint.measureText(label);
                        canvas.drawText(label, getWidth() - labelAndScaleSpace - longScaleLen - textWidth, scaleY + labelHeight / 2f, paint);
                    }
                } else {
                    canvas.drawLine(getWidth() - longScaleLen * shortLongtScaleRatio, scaleY, getWidth(), scaleY, paint);
                }
            }
            //指示线
            paint.setAlpha(255);
            paint.setStrokeWidth(indicatorWidth);
            paint.setColor(indicatorColor);
            canvas.drawLine(getWidth() - labelAndScaleSpace / 2f - longScaleLen, getScrollY() + blank, getWidth(), getScrollY() + blank, paint);
        }
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2.0));
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onFling = false;
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }
            start = getPosition(event);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            doMove(event);
        }
        gestureDetector.onTouchEvent(event);
        if (!onFling && event.getAction() == MotionEvent.ACTION_UP) {//抬起
            //如果没对齐刻度，自动滚过去
            autoScroll();
        }
        return true;
    }

    private void doMove(MotionEvent event) {
        int scroll;
        if (event == null) {
            scroll = orientation == HORIZONTAL ? scroller.getCurrX() : scroller.getCurrY();
        } else {
            int move = getPosition(event);
            scroll = scrollOffset() + start - move;
            start = move;
        }
        if (scroll < 0) {
            scroll = 0;
        } else if (scroll > contentLen) {
            scroll = contentLen;
        }
        scrollToTarget(scroll);
        invalidate();
        //计算当前指向的值
        value = orientation == HORIZONTAL ?
                min + scroll * totalValue / contentLen : max - scroll * totalValue / contentLen;
        if (updateCallback != null) {
            updateCallback.onValueUpdate(value);
        }
    }

    private void scrollToTarget(int scroll) {
        if (orientation == HORIZONTAL) {
            scrollTo(scroll, 0);
        } else {
            scrollTo(0, scroll);
        }
    }

    private int getPosition(MotionEvent event) {
        return (int) (orientation == HORIZONTAL ? event.getX() : event.getY());
    }

    public static class Params {
        private ScaleView scaleView;
        private int min;
        private int max;
        private int bigStepScaleNum;//隔多少个短刻度一个长刻度
        private float twoBigStepDifValue;//两个长刻度之间值的大小
        private int labelColor;
        private int indicatorColor;
        private int indicatorPostion;
        private float labelSize;//标签字体大小
        private int scaleSpace;//刻度之间间隔
        private int labelAndScaleSpace;//标签与长刻度的间隔
        private int longScaleLen;
        private int scaleWidth;//刻度线条宽度
        private int indicatorWidth;//指示器宽
        private float shortLongtScaleRatio;//短刻度与长刻度比例，短/长
        private int orientation;
        private boolean isEdgeDim;//两端边缘模糊
        private OnValueUpdateCallback updateCallback;
        private TextFormatterCallback textFormatterCallback;

        Params(ScaleView scaleView) {
            this.scaleView = scaleView;
            int min = scaleView.min;
            int max = scaleView.max;
            int bigStepScaleNum = scaleView.bigStepScaleNum;//隔多少个短刻度一个长刻度
            float twoBigStepDifValue = scaleView.twoBigStepDifValue;//两个长刻度之间值的大小
            int labelColor = scaleView.labelColor;
            int indicatorColor = scaleView.indicatorColor;
            int indicatorPostion = scaleView.indicatorPostion;
            float labelSize = scaleView.labelSize;//标签字体大小
            int scaleSpace = scaleView.scaleSpace;//刻度之间间隔
            int labelAndScaleSpace = scaleView.labelAndScaleSpace;//标签与长刻度的间隔
            int longScaleLen = scaleView.longScaleLen;
            int scaleWidth = scaleView.scaleWidth;//刻度线条宽度
            int indicatorWidth = scaleView.indicatorWidth;//指示器宽
            float shortLongtScaleRatio = scaleView.shortLongtScaleRatio;//短刻度与长刻度比例，短/长
            int orientation = scaleView.orientation;
            boolean isEdgeDim = scaleView.isEdgeDim;//两端边缘模糊
        }

        /**
         * 设置取值范围
         */
        public void setScope(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * 隔多少个短刻度一个长刻度
         */
        public void setBigStepScaleNum(int bigStepScaleNum) {
            if (bigStepScaleNum <= 0) {
                throw new IllegalArgumentException("bigStepScaleNum must be greater than 0");
            }
            this.bigStepScaleNum = bigStepScaleNum;
        }

        /**
         * 两个长刻度之间值的大小
         */
        public void setTwoBigStepDifValue(float twoBigStepDifValue) {
            if (bigStepScaleNum <= 0) {
                throw new IllegalArgumentException("twoBigStepDifValue must be greater than 0");
            }
            this.twoBigStepDifValue = twoBigStepDifValue;
        }

        /**
         * 方向
         */
        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }

        /**
         * 标签颜色
         */
        public void setLabelColor(int labelColor) {
            this.labelColor = labelColor;
        }

        /**
         * 指示器颜色
         */
        public void setIndicatorColor(int indicatorColor) {
            this.indicatorColor = indicatorColor;
        }

        /**
         * 标签字体大小
         */
        public void setLabelSize(int labelSize) {
            this.labelSize = labelSize;
        }

        /**
         * 刻度之间间隔
         */
        public void setScaleSpace(int scaleSpace) {
            this.scaleSpace = scaleSpace;
        }

        /**
         * 刻度与标签之间间隔
         */
        public void setLabelAndScaleSpace(int labelAndScaleSpace) {
            this.labelAndScaleSpace = labelAndScaleSpace;
        }

        /**
         * 长刻度尺寸
         */
        public void setLongScaleLen(int longScaleLen) {
            this.longScaleLen = longScaleLen;
        }

        /**
         * 刻度线条宽度
         */
        public void setScaleWidth(int scaleWidth) {
            this.scaleWidth = scaleWidth;
        }

        /**
         * 指示器宽
         */
        public void setIndicatorWidth(int indicatorWidth) {
            this.indicatorWidth = indicatorWidth;
        }

        /**
         * 短刻度与长刻度比例，短/长
         */
        public void setShortLongtScaleRatio(float shortLongtScaleRatio) {
            this.shortLongtScaleRatio = shortLongtScaleRatio;
        }

        /**
         * 两端是否边缘模糊
         */
        public void setEdgeDim(boolean isEdgeDim) {
            this.isEdgeDim = isEdgeDim;
        }

        /**
         * 设置指示器位置，即取哪个位置的值
         *
         * @param postion 取值位置占View的百分比。水平方向从左到右：0~100；垂直方向从上到下0~100
         */
        public void setIndicatorPosition(int postion) {
            if (postion < 0) {
                postion = 0;
            } else if (postion > 100) {
                postion = 100;
            }
            this.indicatorPostion = postion;
        }

        public void setOnValueUpdateCallback(OnValueUpdateCallback callback) {
            updateCallback = callback;
        }

        public void setLabelFormatter(TextFormatterCallback callback) {
            textFormatterCallback = callback;
        }

        /**
         * 应用参数
         */
        public void apply() {
            scaleView.textFormatterCallback = textFormatterCallback;
            scaleView.updateCallback = updateCallback;
            scaleView.indicatorPostion = indicatorPostion;
            scaleView.isEdgeDim = isEdgeDim;
            scaleView.shortLongtScaleRatio = shortLongtScaleRatio;
            scaleView.indicatorWidth = indicatorWidth;
            scaleView.scaleWidth = scaleWidth;
            scaleView.longScaleLen = longScaleLen;
            scaleView.labelAndScaleSpace = labelAndScaleSpace;
            scaleView.scaleSpace = scaleSpace;
            scaleView.labelSize = labelSize;
            scaleView.indicatorColor = indicatorColor;
            scaleView.labelColor = labelColor;
            scaleView.orientation = orientation;
            scaleView.twoBigStepDifValue = twoBigStepDifValue;
            scaleView.bigStepScaleNum = bigStepScaleNum;
            scaleView.min = min;
            scaleView.max = max;
            scaleView.updateParams();
            scaleView.invalidate();
        }
    }
}
