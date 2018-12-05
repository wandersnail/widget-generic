package cn.zfs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 时间: 2017/6/27 11:17
 * 作者: 曾繁盛
 * 邮箱: 43068145@qq.com
 * 功能: 可自定义长按事件的长按时长的按钮
 */

public class LongPressView extends View {
	private static final int TOUCH_SLOP = 100;//移动的阈值
	private int mLastMotionX, mLastMotionY;
	private long longPressDuration = 2000;//长按事件触发时长
	private LongPressRunnable longPressRunnable;
	private boolean isMoved;

	public LongPressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LongPressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		longPressRunnable = new LongPressRunnable();
	}

	/**
	 * @param duration 长按事件触发时长
	 */
	public void setOnLongPressDuration(long duration) {
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
                    if (isMoved) break;
                    if (Math.abs(mLastMotionX - x) > TOUCH_SLOP || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                        //移动超过阈值，则表示移动了  
                        isMoved = true;
                        removeCallbacks(longPressRunnable);
                    }
                    break;
                case MotionEvent.ACTION_UP://释放了				  
                    removeCallbacks(longPressRunnable);
                    break;
            }
		    return true;
		}
		return super.dispatchTouchEvent(event);
	}

	private class LongPressRunnable implements Runnable {
		@Override
		public void run() {
			performLongClick();
		}
	}
}
