package cn.wandersnail.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 下拉刷新，控制在顶部才拦截触摸事件
 * <p>
 * date: 2019/8/22 19:12
 * author: zengfansheng
 */
public class PullRefreshLayout extends SwipeRefreshLayout {
    private AdapterView<?> lv;

    public PullRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public PullRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    private boolean isListViewNotAtTop() {
        if (lv != null) {
            View view = lv.getChildAt(0);
            return view != null && (lv.getFirstVisiblePosition() != 0 || view.getTop() != 0);
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            if (view instanceof AdapterView<?>) {
                lv = (AdapterView<?>) view;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isListViewNotAtTop() && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isListViewNotAtTop() && super.onInterceptTouchEvent(ev);
    }
}
