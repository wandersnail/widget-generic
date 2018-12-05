package cn.zfs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import com.zfs.commons.interfaces.IString;

import java.util.List;


/**
 * 描述: 横向滑动文本选择器
 * 时间: 2018/8/25 21:01
 * 作者: zengfansheng
 */
public class ScrollCheckTab extends View {
    private Paint paint;    
    private int position;
    private SparseArray<TabInfo> tabInfoArray = new SparseArray<>();
    private Builder builder;
    private float downX;
    private boolean click;
    private boolean scroll;
    private Scroller scroller;
    private boolean isInitialized;//是否已绘制完成一次
    private boolean hasCacheCheck;//是否有缓存的选中任务
    private OnCheckChangeListener listener;
    private long lastEventTriggerTime;//事件上次触发时间
    
    public ScrollCheckTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);        
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scroller = new Scroller(context);        
    }

    public void setBuidler(@NonNull Builder buidler) {
        this.builder = buidler;
        tabInfoArray.clear();
        paint.setTextSize(builder.textSize);
        paint.setTypeface(builder.typeface);
        for (int i = 0, tabListSize = buidler.tabList.size(); i < tabListSize; i++) {
            tabInfoArray.put(i, new TabInfo(0, 0));
        }        
        invalidate();
    }
    
    public Builder getBuilder() {
        return builder;
    }

    public void setOnCheckChangeListener(OnCheckChangeListener listener) {
        this.listener = listener;
    }
    
    /**
     * 选中指定tab
     */
    public void check(int position) {
        doCheck(position, false, true);
    }

    /**
     * 选中指定tab，不触发回调
     */
    public void checkNoEvent(int position) {
        doCheck(position, false, false);
    }

    /**
     * 选中指定tab，无动画
     */
    public void checkImmediately(int position) {
        doCheck(position, true, true);
    }

    /**
     * 选中指定tab，无动画，不触发回调
     */
    public void checkImmediatelyNoEvent(int position) {
        doCheck(position, true, false);
    }
    
    private void doCheck(int position, boolean immediately, boolean notify) {
        if (builder != null && builder.tabList != null && !builder.tabList.isEmpty() && position >= 0 &&
                position < builder.tabList.size() && this.position != position) {
            int startScrollX = tabInfoArray.get(this.position).scrollX;
            int targetScrollX = tabInfoArray.get(position).scrollX;
            this.position = position;
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
                listener.onCheck(position, builder.tabList.get(position));
            }
        }
    }

    /**
     * 获取当前选择tab位置
     */
    public int getCheckedPosition() {
        return position;
    }

    /**
     * 获取选中的tab
     */
    public IString getCheckedTab() {
        return builder == null || builder.tabList == null || position <= builder.tabList.size() || 
                position >= builder.tabList.size() ? null : builder.tabList.get(position);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (builder.tabList != null && !builder.tabList.isEmpty()) {            
            float startX = 0;
            //画文本
            for (int i = 0, tabListSize = builder.tabList.size(); i < tabListSize; i++) {
                paint.setColor(i == position ? builder.checkColor : builder.normalColor);
                String s = builder.tabList.get(i).getString();
                float w = paint.measureText(s);
                Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                int baseline = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
                startX = i == 0 ? (getWidth() - w) / 2 : startX + builder.tabSpace;
                TabInfo info = tabInfoArray.get(i);
                info.centerX = startX + w / 2;
                info.textW = w;
                if (info.scrollX == Integer.MAX_VALUE) {
                    info.scrollX = (int) (info.centerX - getWidth() / 2);
                }
                canvas.drawText(s, startX, baseline, paint);
                startX += w;
            }
            isInitialized = true;
            if (hasCacheCheck) {
                hasCacheCheck = false;
                scrollTo(tabInfoArray.get(position).scrollX, 0);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEnabled() && builder != null && builder.tabList != null && !builder.tabList.isEmpty()) {            
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:	
                    downX = event.getX();
                    scroll = false;
                    click = true;
            		break;
                case MotionEvent.ACTION_MOVE:
                    float distance = event.getX() - downX;
                    //限制单次触发
                    if (Math.abs(distance) > 5 && !scroll) {
                        click = false;
                        if (Math.abs(distance) > 30) {
                            scroll = true;
                            if (System.currentTimeMillis() - lastEventTriggerTime >= builder.eventTriggerLimitMillis) {
                                lastEventTriggerTime = System.currentTimeMillis();
                                if (distance > 0) {//向右滑
                                    check(position - 1);
                                } else {//向左滑
                                    check(position + 1);
                                }                                
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (click) {
                        click = false;
                        int position = -1;
                        for (int i = 0, arrSize = tabInfoArray.size(); i < arrSize; i++) {
                            TabInfo info = tabInfoArray.valueAt(i);
                            if (info.contains(event.getX() + getScrollX())) {//判断点击是否在tab范围内
                                position = i;
                                break;
                            }
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

    private class TabInfo {
        float centerX;//文本中心X坐标
        float textW;//文本宽度
        int scrollX = Integer.MAX_VALUE;//scrollX多少滑到这个tab

        TabInfo(float centerX, float textW) {
            this.centerX = centerX;
            this.textW = textW;
        }
        
        boolean contains(float x) {
            return x >= centerX - textW / 2 && x <= centerX + textW / 2;
        }
    }
    
    public interface OnCheckChangeListener {
        void onCheck(int position, IString tab);
    }
    
    public static class Builder {
        private List<IString> tabList;
        private int checkColor = 0xFF5071E6;
        private int normalColor = Color.GRAY;
        private int textSize = 30;
        private int tabSpace = 15;
        private Typeface typeface;        
        private int eventTriggerLimitMillis;

        /**
         * 文本大小
         * @param textSize 像素
         */
        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        public Builder setTypeface(Typeface typeface) {
            this.typeface = typeface;
            return this;
        }
        
        /**
         * 设置文本颜色
         * @param normalColor 正常文本颜色
         * @param checkColor 选中文本颜色
         */
        public Builder setTextColor(int normalColor, int checkColor) {
            this.normalColor = normalColor;
            this.checkColor = checkColor;
            return this;
        }

        /**
         * 文本之间间距
         * @param tabSpace 单位：像素
         */
        public Builder setTabSpace(int tabSpace) {
            this.tabSpace = tabSpace;
            return this;
        }

        public Builder setTabs(@NonNull List<IString> list) {
            tabList = list;
            return this;
        }
        
        public Builder setEventTriggerLimitTime(int millis) {
            eventTriggerLimitMillis = millis;
            return this;
        }

        public List<IString> getTabs() {
            return tabList;
        }        
    }
}
