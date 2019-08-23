package cn.wandersnail.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 可自定义长按事件的长按时长的按钮
 * <p>
 * date: 2019/8/23 09:23
 * author: zengfansheng
 */
public class LongPressView extends View {
    private static final int TOUCH_SLOP = 100; //移动的阈值
    private int mLastMotionX;
    private int mLastMotionY;
    private int longPressDuration = 2000; //长按事件触发时长
    private Runnable longPressRunnable = this::performLongClick;
    private boolean isMoved;

    public LongPressView(Context context) {
        super(context);
    }

    public LongPressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LongPressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param duration 长按事件触发时长
     */
    public void setOnLongPressDuration(int duration) {
        longPressDuration = duration;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isEnabled()) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    isMoved = false;
                    postDelayed(longPressRunnable, longPressDuration);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isMoved) {
                        if (Math.abs(mLastMotionX - x) > TOUCH_SLOP || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                            //移动超过阈值，则表示移动了  
                            isMoved = true;
                            removeCallbacks(longPressRunnable);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP: //释放了				  
                    removeCallbacks(longPressRunnable);
                    break;
            }
            return true;
        }
        return super.dispatchTouchEvent(event);
    }
}
