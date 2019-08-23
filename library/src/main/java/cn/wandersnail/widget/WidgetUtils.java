package cn.wandersnail.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * date: 2019/8/22 15:39
 * author: zengfansheng
 */
public class WidgetUtils {
    /**
     * @param normal   正常时的Drawable
     * @param pressed  按压时的Drawable
     * @param selected 被选中时的Drawable
     * @param disabled 不可用时的Drawable
     */
    public static StateListDrawable createStateListDrawable(Drawable normal, Drawable pressed, Drawable selected, Drawable disabled) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        drawable.addState(new int[]{android.R.attr.state_selected}, selected);
        drawable.addState(new int[0], normal); //normal一定要最后
        return drawable;
    }

    /**
     * 创建带边框背景
     * @param fillColor    背景色
     * @param frameWidth   边框亮度
     * @param frameColor   边框颜色
     * @param cornerRadius 圆角
     */
    public static GradientDrawable createDrawable(int fillColor, int frameWidth, int frameColor, int cornerRadius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(cornerRadius);
        drawable.setColor(fillColor);
        drawable.setStroke(frameWidth, frameColor);
        return drawable;
    }

    /**
     * @param color       背景色
     * @param cornerRadii 圆角大小
     * @param leftTop     左上是否圆角
     * @param rightTop    右上是否圆角
     * @param leftBottom  左下是否圆角
     * @param rightBottom 右下是否圆角
     */
    public static GradientDrawable createDrawable(int color, float cornerRadii, boolean leftTop, 
                                                  boolean rightTop, boolean leftBottom, boolean rightBottom) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadii(new float[]{
                leftTop ? cornerRadii : 0, leftTop ? cornerRadii : 0,
                rightTop ? cornerRadii : 0, rightTop ? cornerRadii : 0,
                rightBottom ? cornerRadii : 0, rightBottom ? cornerRadii : 0,
                leftBottom ? cornerRadii : 0, leftBottom ? cornerRadii : 0
        });
        drawable.setColor(color);
        return drawable;
    }

    /**
     * @param normal  正常时的颜色
     * @param pressed 按压时的颜色
     * @param selected 选中时的颜色
     * @param disabled 不可用时的颜色
     */
    public static ColorStateList createColorStateList(int normal, int pressed, int selected, int disabled) {
        //normal一定要最后
        int[][] states = {
                {-android.R.attr.state_enabled},
                {android.R.attr.state_pressed, android.R.attr.state_enabled},
                {android.R.attr.state_selected, android.R.attr.state_enabled},
                {}
        };
        return new ColorStateList(states, new int[]{disabled, pressed, selected, normal});
    }

    /**
     * 获取显示屏幕宽度，不包含状态栏和导航栏
     */
    public static int getDisplayScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取显示屏幕高度
     */
    public static int getDisplayScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static float dp2pxF(Context context, float dpValue) {
        return dpValue * context.getResources().getDisplayMetrics().density + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dpF(Context context, float pxValue) {
        return pxValue / context.getResources().getDisplayMetrics().density + 0.5f;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * @param cornerRadii 圆角大小，dp
     */
    public static StateListDrawable createDrawableSelecor(int normal, int pressed, float cornerRadii, boolean leftTop,
                                                          boolean rightTop, boolean leftBottom, boolean rightBottom) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(pressed, cornerRadii, leftTop, rightTop, leftBottom, rightBottom));
        drawable.addState(new int[0], createDrawable(normal, cornerRadii, leftTop, rightTop, leftBottom, rightBottom));
        return drawable;
    }
}
