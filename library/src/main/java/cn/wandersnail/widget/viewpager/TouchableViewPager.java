package cn.wandersnail.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * date: 2019/8/22 23:32
 * author: zengfansheng
 */
public class TouchableViewPager extends ViewPager {
    private boolean touchable;

    public TouchableViewPager(@NonNull Context context) {
        super(context);
    }

    public TouchableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置ViewPager是否可滑动
     */
    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    /**
     * 是否禁用其事件，实现不能滑动
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return touchable && super.onTouchEvent(ev);
    }

    /**
     * 是否截触摸事件，让事件可以向内层传递
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return touchable && super.onInterceptTouchEvent(ev);
    }

    /**
     * 设置切换动画时长
     */
    public void setScrollDuration(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(getContext(), new OvershootInterpolator(0.6f));
            field.set(this, viewPagerScroller);
            viewPagerScroller.duration = duration;
        } catch (Exception ignore) {
        }
    }

    private class ViewPagerScroller extends Scroller {
        int duration;

        ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }
    }
}
