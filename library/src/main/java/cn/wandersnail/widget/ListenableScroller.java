package cn.wandersnail.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import java.lang.ref.WeakReference;

/**
 * date: 2019/8/22 20:19
 * author: zengfansheng
 */
public class ListenableScroller extends OverScroller {
    private OnScrollListener listener;
    private Handler handler = new ScrollEventHandler(this);
    private boolean flingMode;

    public ListenableScroller(Context context) {
        super(context);
    }

    public ListenableScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public interface OnScrollListener {
        void onScroll(ListenableScroller scroller);

        void onScrollFinish(ListenableScroller scroller);

        void onFlingFinish(ListenableScroller scroller);
    }

    @Override
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        super.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
        flingMode = true;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, duration);
        flingMode = false;
        handler.sendEmptyMessage(0);
    }

    private static class ScrollEventHandler extends Handler {
        private WeakReference<ListenableScroller> weakRef;
        private int lastX = 0;
        private int lastY = 0;
        
        ScrollEventHandler(ListenableScroller scroller) {
            super(Looper.getMainLooper());
            weakRef = new WeakReference<>(scroller);
        }

        @Override
        public void handleMessage(Message msg) {
            ListenableScroller scroller = weakRef.get();
            if (scroller != null) {
                OnScrollListener listener = scroller.listener;
                if (scroller.computeScrollOffset()) {
                    if (lastX != scroller.getCurrX() || lastY != scroller.getCurrY()) {
                        if (listener != null) {
                            listener.onScroll(scroller);
                        }
                        lastX = scroller.getCurrX();
                        lastY = scroller.getCurrY();
                    }
                }
                if (scroller.isFinished()) {
                    if (listener != null) {
                        if (scroller.flingMode) {
                            listener.onFlingFinish(scroller);
                        } else {
                            listener.onScrollFinish(scroller);
                        }
                    }                    
                } else {
                    sendEmptyMessageDelayed(0, 10);
                }
            }
        }
    }
}
