package cn.zfs.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by zengfs on 2016/2/2.
 * 可控制是否能滑动
 */
public class TouchControlableViewPager extends ViewPager {
	
	private boolean isTouchEnabled = true;
	
	public TouchControlableViewPager(Context context) {
		super(context);
	}

	public TouchControlableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置ViewPager是否可滑动
	 */
	public void setTouchEnabled(boolean enable) {
		isTouchEnabled = enable;
	}
	
	/**
	 * 是否禁用其事件，实现不能滑动
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return isTouchEnabled && super.onTouchEvent(ev);
	}

	/**
	 * 是否截触摸事件，让事件可以向内层传递
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return isTouchEnabled && super.onInterceptTouchEvent(ev);
	}

    /**设置切换动画时长*/
    public void setScrollDuration(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(getContext(), new OvershootInterpolator(0.6F));
            field.set(this, viewPagerScroller);
            viewPagerScroller.setDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 控制ViewPager动画切换时长
     */
    public class ViewPagerScroller extends Scroller {
        private int duration;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }
    }
}
