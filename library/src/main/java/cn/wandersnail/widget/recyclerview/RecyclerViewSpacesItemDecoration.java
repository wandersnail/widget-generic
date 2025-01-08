package cn.wandersnail.widget.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

/**
 * date: 2020/12/10 10:46
 * author: zengfansheng
 */
public class RecyclerViewSpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final HashMap<String, Integer> mSpaceValueMap;
    public static final String TOP_DECORATION = "top_decoration";
    public static final String BOTTOM_DECORATION = "bottom_decoration";
    public static final String LEFT_DECORATION = "left_decoration";
    public static final String RIGHT_DECORATION = "right_decoration";
    
    public RecyclerViewSpacesItemDecoration(HashMap<String, Integer> mSpaceValueMap) {
        this.mSpaceValueMap = mSpaceValueMap;
    }
    
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        Integer top = mSpaceValueMap.get(TOP_DECORATION);
        if (top != null) {
            outRect.top = top;
        }
        Integer left = mSpaceValueMap.get(LEFT_DECORATION);
        if (left != null) {
            outRect.left = left;
        }
        Integer right = mSpaceValueMap.get(RIGHT_DECORATION);
        if (right != null) {
            outRect.right = right;
        }
        Integer bottom = mSpaceValueMap.get(BOTTOM_DECORATION);
        if (bottom != null) {
            outRect.bottom = bottom;
        }
    }
}
