package cn.wandersnail.widget.dialog;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import cn.wandersnail.widget.R;
import cn.wandersnail.widget.WidgetUtils;

/**
 * date: 2019/8/22 14:56
 * author: zengfansheng
 */
public class DefaultAlertDialog extends BaseDialog<DefaultAlertDialog> {
    private TextView tvTitle;
    private View titleDivider;
    private FrameLayout layoutContent;
    private LinearLayout layoutText;
    private TextView tvMsg;
    private TextView tvSubMsg;
    private View horizontalDivider;
    private TextView tvNegative;
    private View verticalDivider;
    private TextView tvPositive;
    private boolean autoDismiss;
    private View.OnClickListener negativeListener;
    private View.OnClickListener positiveListener;
    private int autoDismissDelayMillis = 1500;
    private boolean negativeVisible; //消极按钮，左
    private boolean positiveVisible; //积极按钮，右
    private int cornerRadii;
    private int backColor = Color.WHITE; //对话框背景色
    private int titleBackColor = -0x22070708; //标题栏背景色
    private boolean clickDismiss = true;

    private void assignViews() {
        tvTitle = view.findViewById(R.id.tvTitle);
        titleDivider = view.findViewById(R.id.titleDivider);
        layoutContent = view.findViewById(R.id.layoutContent);
        layoutText = view.findViewById(R.id.layoutText);
        tvMsg = view.findViewById(R.id.tvMsg);
        tvSubMsg = view.findViewById(R.id.tvSubMsg);
        horizontalDivider = view.findViewById(R.id.horizontalDivider);
        tvNegative = view.findViewById(R.id.tvNegative);
        verticalDivider = view.findViewById(R.id.verticalDivider);
        tvPositive = view.findViewById(R.id.tvPositive);
    }


    public DefaultAlertDialog(@NonNull Activity activity) {
        super(activity, R.layout.dialog_default_alert);
        assignViews();
        View.OnClickListener clickListener = v -> {
            if (clickDismiss) {
                dismiss();
            }
            if (tvNegative == v) {
                if (negativeListener != null) {
                    negativeListener.onClick(v);
                }
            } else if (tvPositive == v) {
                if (positiveListener != null) {
                    positiveListener.onClick(v);
                }
            }
        };
        tvNegative.setOnClickListener(clickListener);
        tvPositive.setOnClickListener(clickListener);
        cornerRadii = WidgetUtils.dp2px(activity, 8f);
        setBackgroundColor(backColor);
        setTitleBackgroundColor(titleBackColor);
        updateButtonVisible();
        int width = (int) (Math.min(WidgetUtils.getDisplayScreenWidth(activity), WidgetUtils.getDisplayScreenHeight(activity)) * 0.8);
        setSize(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private Runnable dismissRunnable = this::dismiss;

    @CallSuper
    @Override
    public void onShow() {
        if (autoDismiss) {
            view.removeCallbacks(dismissRunnable);
            view.postDelayed(dismissRunnable, autoDismissDelayMillis);
        }
    }

    @CallSuper
    @Override
    public void onDismiss() {
        view.removeCallbacks(dismissRunnable);
    }

    private void updateButtonVisible() {
        tvPositive.setVisibility(positiveVisible ? View.VISIBLE : View.GONE);
        tvNegative.setVisibility(negativeVisible ? View.VISIBLE : View.GONE);
        horizontalDivider.setVisibility(negativeVisible || positiveVisible ? View.VISIBLE : View.GONE);
        verticalDivider.setVisibility(negativeVisible && positiveVisible ? View.VISIBLE : View.GONE);
        updateButtonBackground();
    }

    private void updateButtonBackground() {
        if (negativeVisible && positiveVisible) {
            tvNegative.setBackground(WidgetUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii,
                    false, false, true, false));
            tvPositive.setBackground(WidgetUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii,
                    false, false, false, true));
        } else if (negativeVisible) {
            tvNegative.setBackground(WidgetUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii,
                    false, false, true, true));
        } else {
            tvPositive.setBackground(WidgetUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, cornerRadii,
                    false, false, true, true));
        }
    }

    /**
     * 设置对话框的圆角大小
     *
     * @param cornerRadii 圆角大小
     */
    public DefaultAlertDialog setCornerRadii(int cornerRadii) {
        this.cornerRadii = cornerRadii;
        setBackgroundColor(backColor);
        setTitleBackgroundColor(titleBackColor);
        updateButtonBackground();
        return this;
    }

    /**
     * 设置点击按键后自动dismiss对话框
     */
    public DefaultAlertDialog setClickDismiss(boolean clickDismiss) {
        this.clickDismiss = clickDismiss;
        return this;
    }

    /**
     * 设置背景色
     *
     * @param color 颜色值
     */
    public DefaultAlertDialog setBackgroundColor(@ColorInt int color) {
        backColor = color;
        view.setBackground(WidgetUtils.createDrawable(color, cornerRadii, true, true, true, true));
        return this;
    }

    /**
     * 是否自动消失
     */
    public DefaultAlertDialog setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
        return this;
    }

    /**
     * 自动消失延时时间
     *
     * @param autoDismissDelayMillis 毫秒
     */
    public DefaultAlertDialog setAutoDismissDelayMillis(int autoDismissDelayMillis) {
        this.autoDismissDelayMillis = autoDismissDelayMillis;
        return this;
    }

    public DefaultAlertDialog setTitle(CharSequence text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        titleDivider.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    public DefaultAlertDialog setTitle(@StringRes int resId) {
        tvTitle.setVisibility(resId != 0 ? View.VISIBLE : View.GONE);
        titleDivider.setVisibility(resId != 0 ? View.VISIBLE : View.GONE);
        tvTitle.setText(resId);
        return this;
    }

    public DefaultAlertDialog setTitleTextColor(@ColorInt int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setTitleTypeface(Typeface typeface) {
        tvTitle.setTypeface(typeface);
        return this;
    }

    public DefaultAlertDialog setTitleTextSize(int unit, float size) {
        tvTitle.setTextSize(unit, size);
        return this;
    }

    public DefaultAlertDialog setTitleBackgroundColor(@ColorInt int color) {
        titleBackColor = color;
        tvTitle.setBackground(WidgetUtils.createDrawable(color, cornerRadii, true, true, false, false));
        return this;
    }

    public DefaultAlertDialog setTitleDividerColor(@ColorInt int color) {
        titleDivider.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题分割线高度
     *
     * @param height 高度，像素
     */
    public DefaultAlertDialog setTitleDividerHeight(int height) {
        ViewGroup.LayoutParams params = titleDivider.getLayoutParams();
        params.height = height;
        titleDivider.setLayoutParams(params);
        return this;
    }

    public DefaultAlertDialog setContentViewPadding(int left, int top, int right, int bottom) {
        layoutContent.setPadding(left, top, right, bottom);
        return this;
    }

    public DefaultAlertDialog setContentView(View view) {
        layoutContent.removeAllViews();
        layoutContent.addView(view);
        return this;
    }

    public DefaultAlertDialog setContentView(View view, ViewGroup.LayoutParams params) {
        layoutContent.removeAllViews();
        layoutContent.addView(view, params);
        return this;
    }

    public DefaultAlertDialog setMessage(CharSequence text) {
        tvMsg.setText(text);
        return this;
    }

    public DefaultAlertDialog setMessage(@StringRes int resId) {
        tvMsg.setText(resId);
        return this;
    }

    public DefaultAlertDialog setMessageTextColor(@ColorInt int color) {
        tvMsg.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setMessageTypeface(Typeface typeface) {
        tvMsg.setTypeface(typeface);
        return this;
    }

    public DefaultAlertDialog setMessageTextSize(int unit, float size) {
        tvMsg.setTextSize(unit, size);
        return this;
    }

    public DefaultAlertDialog setSubMessage(CharSequence text) {
        tvSubMsg.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvSubMsg.setText(text);
        return this;
    }

    public DefaultAlertDialog setSubMessage(@StringRes int resId) {

        tvSubMsg.setText(resId);
        return this;
    }

    public DefaultAlertDialog setSubMessageTextColor(@ColorInt int color) {
        tvSubMsg.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setSubMessageTypeface(Typeface typeface) {
        tvSubMsg.setTypeface(typeface);
        return this;
    }

    public DefaultAlertDialog setSubMessageTextSize(int unit, float size) {
        tvSubMsg.setTextSize(unit, size);
        return this;
    }

    public DefaultAlertDialog setTextGravity(int gravity) {
        layoutText.setGravity(gravity);
        return this;
    }

    public DefaultAlertDialog setNegativeButtonTypeface(Typeface tf) {
        tvNegative.setTypeface(tf);
        return this;
    }

    public DefaultAlertDialog setNegativeButton(@StringRes int resId, View.OnClickListener listener) {
        negativeVisible = true;
        tvNegative.setText(resId);
        negativeListener = listener;
        updateButtonVisible();
        return this;
    }

    public DefaultAlertDialog setNegativeButton(CharSequence text, View.OnClickListener listener) {
        negativeVisible = true;
        tvNegative.setText(text);
        negativeListener = listener;
        updateButtonVisible();
        return this;
    }

    public DefaultAlertDialog setNegativeButtonTextColor(int normal, int pressed) {
        tvNegative.setTextColor(getColorStateList(normal, pressed));
        return this;
    }

    public DefaultAlertDialog setNegativeButtonTextColor(int normal, int pressed, int disabled) {
        tvNegative.setTextColor(getColorStateList(normal, pressed, disabled));
        return this;
    }

    public DefaultAlertDialog setNegativeButtonTextColor(@ColorInt int color) {
        tvNegative.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setNegativeButtonTextSize(int unit, float size) {
        tvNegative.setTextSize(unit, size);
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTypeface(Typeface tf) {
        tvPositive.setTypeface(tf);
        return this;
    }

    public DefaultAlertDialog setPositiveButton(@StringRes int resId, View.OnClickListener listener) {
        positiveVisible = true;
        tvPositive.setText(resId);
        positiveListener = listener;
        updateButtonVisible();
        return this;
    }

    public DefaultAlertDialog setPositiveButton(CharSequence text, View.OnClickListener listener) {
        positiveVisible = true;
        tvPositive.setText(text);
        positiveListener = listener;
        updateButtonVisible();
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTextColor(int normal, int pressed) {
        tvPositive.setTextColor(getColorStateList(normal, pressed));
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTextColor(int normal, int pressed, int disabled) {
        tvPositive.setTextColor(getColorStateList(normal, pressed, disabled));
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTextColor(@ColorInt int color) {
        tvPositive.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTextSize(int unit, float size) {
        tvPositive.setTextSize(unit, size);
        return this;
    }

    private ColorStateList getColorStateList(int normal, int pressed) {
        int[][] states = new int[][]{new int[]{android.R.attr.state_pressed}, new int[0]};
        return new ColorStateList(states, new int[]{pressed, normal});
    }

    private ColorStateList getColorStateList(int normal, int pressed, int disabled) {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{-android.R.attr.state_enabled},
                new int[0]
        };
        return new ColorStateList(states, new int[]{pressed, disabled, normal});
    }
}
