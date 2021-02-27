package cn.zfs.widgetdemo;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * date: 2021/2/27 13:51
 * author: zengfansheng
 */
public class ExpandableView extends FrameLayout {
    private boolean moving = false;
    
    public ExpandableView(@NonNull Context context) {
        this(context, null);
    }

    public ExpandableView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.expandable_view, this, true);
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        View contentContainer = view.findViewById(R.id.contentContainer);
        contentContainer.measure(0, 0);
        int contentHeight = contentContainer.getMeasuredHeight();
        ViewGroup.LayoutParams lp = contentContainer.getLayoutParams();
        tvTitle.setOnClickListener(v -> {
            if (moving) {
                return;
            }
            moving = true;
            int start = 0, end = 0;
            Log.d("lp.height", "开始：" + lp.height);
            final boolean fold = lp.height > 0;
            if (fold) {
                start = contentHeight;
            } else {
                end = contentHeight;
            }
            ValueAnimator animator = ValueAnimator.ofInt(start, end);
            animator.addUpdateListener(animation -> {
                lp.height = (int) animation.getAnimatedValue();
                Log.d("lp.height", lp.height + "");
                contentContainer.setLayoutParams(lp);
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    moving = false;
                    Log.d("lp.height", "结束：" + lp.height);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.setDuration(300);            
            animator.start();
        });
    }
    
    
}
