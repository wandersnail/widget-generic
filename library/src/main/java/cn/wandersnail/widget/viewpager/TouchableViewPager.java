package cn.wandersnail.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * date: 2019/8/22 23:32
 * author: zengfansheng
 */
public class TouchableViewPager extends ViewPager {
    private boolean touchable;
    
    public TouchableViewPager(@NonNull Context context) {
        this(context, null);
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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return touchable && super.onTouchEvent(ev);
    }
    
    
}
