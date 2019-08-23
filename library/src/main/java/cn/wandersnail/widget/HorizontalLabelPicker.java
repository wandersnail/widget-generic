package cn.wandersnail.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * 横向滑动文本选择器
 * <p>
 * date: 2019/8/23 08:43
 * author: zengfansheng
 */
public class HorizontalLabelPicker extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int checkedPosition;
    private SparseArray<LabelInfo> labelInfoArray = new SparseArray<>();
    private Builder builder;
    private float downX;
    private boolean click;
    private boolean scroll;
    private OverScroller scroller;
    private boolean isInitialized; //是否已绘制完成一次
    private boolean hasCacheCheck; //是否有缓存的选中任务
    private OnCheckChangeListener listener;
    private long lastEventTriggerTime; //事件上次触发时间

    public interface ILabel {
        String getText();
    }

    public interface OnCheckChangeListener {
        void onCheck(int position, ILabel label);
    }

    public HorizontalLabelPicker(Context context) {
        this(context, null);
    }

    public HorizontalLabelPicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLabelPicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 获取当前选择label位置
     */
    public int getCheckedPosition() {
        return checkedPosition;
    }

    /**
     * 获取选中的label
     */
    public ILabel getCheckedLabel() {
        if (builder == null || builder.labels == null || checkedPosition >= builder.labels.size()) {
            return null;
        } else {
            return builder.labels.get(checkedPosition);
        }
    }

    public void setBuilder(@NonNull Builder builder) {
        Objects.requireNonNull(builder, "builder can't be null");
        this.builder = builder;
        labelInfoArray.clear();
        paint.setTextSize(builder.textSize);
        paint.setTypeface(builder.typeface);
        int i = 0;
        int labelListSize = builder.labels == null ? 0 : builder.labels.size();
        while (i < labelListSize) {
            labelInfoArray.put(i, new LabelInfo(0f, 0f));
            i++;
        }
        scroller = new OverScroller(getContext(), builder.interpolator);
        invalidate();
    }

    @Nullable
    public List<ILabel> getLabels() {
        return builder.labels;
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 选中指定label
     */
    public void check(int position) {
        doCheck(position, false, true);
    }

    /**
     * 选中指定label，不触发回调
     */
    public void checkNoEvent(int position) {
        doCheck(position, false, false);
    }

    /**
     * 选中指定label，无动画
     */
    public void checkImmediately(int position) {
        doCheck(position, true, true);
    }

    /**
     * 选中指定label，无动画，不触发回调
     */
    public void checkImmediatelyNoEvent(int position) {
        doCheck(position, true, false);
    }

    /**
     * @return 返回第一个和给定的标签文本对上的条目位置，未找到时返回-1
     */
    public int firstPositionOf(String label) {
        if (builder != null && builder.labels != null) {
            for (int i = 0; i < builder.labels.size(); i++) {
                if (builder.labels.get(i).getText().equals(label)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 选中指定label
     */
    public void check(String label) {
        int position = firstPositionOf(label);
        if (position != -1) {
            doCheck(position, false, true);
        }
    }

    /**
     * 选中指定label，不触发回调
     */
    public void checkNoEvent(String label) {
        int position = firstPositionOf(label);
        if (position != -1) {
            doCheck(position, false, false);
        }
    }

    /**
     * 选中指定label，无动画
     */
    public void checkImmediately(String label) {
        int position = firstPositionOf(label);
        if (position != -1) {
            doCheck(position, true, true);
        }
    }

    /**
     * 选中指定label，无动画，不触发回调
     */
    public void checkImmediatelyNoEvent(String label) {
        int position = firstPositionOf(label);
        if (position != -1) {
            doCheck(position, true, false);
        }
    }

    private void doCheck(int position, boolean immediately, boolean notify) {
        if (builder != null && builder.labels != null && !builder.labels.isEmpty() && position >= 0 &&
                position < builder.labels.size() && checkedPosition != position) {
            int startScrollX = labelInfoArray.get(checkedPosition).scrollX;
            int targetScrollX = labelInfoArray.get(position).scrollX;
            checkedPosition = position;
            if (isInitialized) {
                if (immediately) {
                    scrollTo(targetScrollX, 0);
                } else {
                    scroller.startScroll(startScrollX, 0, targetScrollX - startScrollX, 0, 300);
                }
                invalidate();
            } else {
                hasCacheCheck = true;
            }
            if (notify && listener != null) {
                listener.onCheck(position, builder.labels.get(position));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (builder.labels != null && !builder.labels.isEmpty()) {
            float startX = 0f;
            //画文本
            int labelListSize = builder.labels.size();
            for (int i = 0; i <= labelListSize; i++) {
                paint.setColor(i == checkedPosition ? builder.checkColor : builder.normalColor);
                String s = builder.labels.get(i).getText();
                float w = paint.measureText(s);
                Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                int baseline = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
                startX = i == 0 ? (getWidth() - w) / 2f : startX + builder.labelSpace;
                LabelInfo info = labelInfoArray.get(i);
                info.centerX = startX + w / 2;
                info.textW = w;
                if (info.scrollX == Integer.MAX_VALUE) {
                    info.scrollX = (int) (info.centerX - getWidth() / 2f);
                }
                canvas.drawText(s, startX, baseline, paint);
                startX += w;
            }
            isInitialized = true;
            if (hasCacheCheck) {
                hasCacheCheck = false;
                scrollTo(labelInfoArray.get(checkedPosition).scrollX, 0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled() && builder != null && builder.labels != null && !builder.labels.isEmpty()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    scroll = false;
                    click = true;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float distance = event.getX() - downX;
                    //限制只能单次触发
                    if (Math.abs(distance) > 5 && !scroll) {
                        click = false;
                        if (Math.abs(distance) > 30) {
                            scroll = true;
                            if (System.currentTimeMillis() - lastEventTriggerTime >= builder.eventTriggerLimitMillis) {
                                lastEventTriggerTime = System.currentTimeMillis();
                                if (distance > 0) { //向右滑
                                    check(checkedPosition - 1);
                                } else { //向左滑
                                    check(checkedPosition + 1);
                                }
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (click) {
                        click = false;
                        int position = -1;
                        int i = 0;
                        int arrSize = labelInfoArray.size();
                        while (i < arrSize) {
                            LabelInfo info = labelInfoArray.valueAt(i);
                            if (info.contains(event.getX() + getScrollX())) { //判断点击是否在label范围内
                                position = i;
                                break;
                            }
                            i++;
                        }
                        if (position != -1 && System.currentTimeMillis() - lastEventTriggerTime >= builder.eventTriggerLimitMillis) {
                            lastEventTriggerTime = System.currentTimeMillis();
                            check(position);
                        }
                    }
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            invalidate();
        }
    }

    private class LabelInfo {
        float centerX;
        float textW;
        int scrollX = Integer.MAX_VALUE; //scrollX多少滑到这个label

        /**
         * @param centerX 文本中心X坐标
         * @param textW   文本宽度
         */
        LabelInfo(float centerX, float textW) {
            this.centerX = centerX;
            this.textW = textW;
        }

        boolean contains(float x) {
            return x >= centerX - textW / 2 && x <= centerX + textW / 2;
        }
    }

    public static class Builder {
        List<ILabel> labels;
        int checkColor = -0xaf8e1a;
        int normalColor = Color.GRAY;
        int textSize = 30;
        int labelSpace = 15;
        Typeface typeface;
        int eventTriggerLimitMillis;
        Interpolator interpolator = new AccelerateDecelerateInterpolator();

        /**
         * 设置标签
         */
        public Builder setLabels(@NonNull List<ILabel> labels) {
            this.labels = labels;
            return this;
        }

        /**
         * 文本大小
         *
         * @param textSize 像素
         */
        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * 设置文本颜色
         *
         * @param normalColor 正常文本颜色
         * @param checkColor  选中文本颜色
         */
        public Builder setTextColor(int normalColor, int checkColor) {
            this.normalColor = normalColor;
            this.checkColor = checkColor;
            return this;
        }

        /**
         * 文本之间间距
         *
         * @param labelSpace 单位：像素
         */
        public Builder setLabelSpace(int labelSpace) {
            this.labelSpace = labelSpace;
            return this;
        }

        /**
         * 设置字体
         */
        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }

        /**
         * 设置事件触发时间间隔限制
         */
        public Builder setEventTriggerLimitMillis(int eventTriggerLimitMillis) {
            this.eventTriggerLimitMillis = eventTriggerLimitMillis;
            return this;
        }

        /**
         * 设置滑动动画插值器
         */
        public Builder setInterpolator(Interpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }

        public List<ILabel> getLabels() {
            return labels;
        }
    }
}
