package com.snail.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snail.commons.utils.ImageUtils;
import com.snail.commons.utils.UiUtils;


/**
 * 描述: 默认提示对话框
 * 时间: 2018/5/15 08:43
 * 作者: zengfansheng
 */
public class DefaultAlertDialog extends BaseDialog implements View.OnClickListener {
    private TextView tvTitle;
    private TextView tvMsg;
    private TextView tvSubMsg;
    private TextView tvNegative;
    private View verticalDivider;
    private View horizontalDivider;
    private View titleDivider;
    private LinearLayout layoutText;
    private FrameLayout layoutContent;
    private TextView tvPositive;
    private View.OnClickListener negativeListener;
    private View.OnClickListener positiveListener;
    private boolean autoDismiss;
    private int autoDismissDelayMillis = 1500;
    private Handler handler;
    private boolean negativeVisible;//消极按钮，左
    private boolean positiveVisible;//积极按钮，右
    private int cornerRadii;
    private int backColor = Color.WHITE;//对话框背景色
    private int titleBackColor = 0xDDF8F8F8;//标题栏背景色
    private boolean clickDismiss = true;

    private void initViews() {
        tvTitle = contentView.findViewById(R.id.tvTitle);
        tvMsg = contentView.findViewById(R.id.tvMsg);
        tvSubMsg = contentView.findViewById(R.id.tvSubMsg);
        tvNegative = contentView.findViewById(R.id.tvNegative);
        verticalDivider = contentView.findViewById(R.id.verticalDivider);
        horizontalDivider = contentView.findViewById(R.id.horizontalDivider);
        layoutText = contentView.findViewById(R.id.layoutText);
        layoutContent = contentView.findViewById(R.id.layoutContent);
        titleDivider = contentView.findViewById(R.id.titleDivider);
        tvPositive = contentView.findViewById(R.id.tvPositive);
        tvNegative.setOnClickListener(this);
        tvPositive.setOnClickListener(this);
        cornerRadii = UiUtils.dip2px(8);
        setBackgroundColor(backColor);
        setTitleBackgroundColor(titleBackColor);
        updateButtonVisible();
    }

    public DefaultAlertDialog(@NonNull Activity activity) {
        super(activity, R.layout.dialog_default_alert);
        initViews();
        init();
    }

    private void init() {
        int width = (int) (Math.min(UiUtils.getDisplayScreenWidth(), UiUtils.getDisplayScreenHeight()) * 0.8);
        setSize(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(dismissRunnable);
            }
        });
        handler = new Handler(Looper.getMainLooper());
    }

    private Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };

    @Override
    public DefaultAlertDialog show() {
        super.show();
        if (autoDismiss) {
            handler.removeCallbacks(dismissRunnable);
            handler.postDelayed(dismissRunnable, autoDismissDelayMillis);
        }
        return this;
    }

    @Override
    public void onClick(View v) {
        if (clickDismiss) {
            dismiss();
        }
        if (tvNegative.equals(v)) {
            if (negativeListener != null) {
                negativeListener.onClick(v);
            }
        } else if (tvPositive.equals(v)) {
            if (positiveListener != null) {
                positiveListener.onClick(v);
            }
        }
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
            tvNegative.setBackground(ImageUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, 
                    cornerRadii, false, false, true, false));
            tvPositive.setBackground(ImageUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, 
                    cornerRadii, false, false, false, true));
        } else if (negativeVisible) {
            tvNegative.setBackground(ImageUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, 
                    cornerRadii, false, false, true, true));
        } else if (positiveVisible) {
            tvPositive.setBackground(ImageUtils.createDrawableSelecor(Color.TRANSPARENT, 0x11000000, 
                    cornerRadii, false, false, true, true));
        }
    }

    @Override
    public DefaultAlertDialog setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        return this;
    }

    @Override
    public DefaultAlertDialog setCanceledOnTouchOutside(boolean flag) {
        super.setCanceledOnTouchOutside(flag);
        return this;
    }

    /**
     * 设置对话框的圆角大小
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
    
    public DefaultAlertDialog setBackgroundColor(@ColorInt int color) {
        backColor = color;
        contentView.setBackground(ImageUtils.createDrawable(color, cornerRadii, true, true, true, true));
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

    public DefaultAlertDialog setTitle(int resId) {
        tvTitle.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        titleDivider.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        tvTitle.setText(resId);
        return this;
    }

    public DefaultAlertDialog setTitleTextColor(@ColorInt int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setTitleTypeface(Typeface tf) {
        tvTitle.setTypeface(tf);
        return this;
    }

    public DefaultAlertDialog setTitleTextSize(int unit, float size) {
        tvTitle.setTextSize(unit, size);
        return this;
    }
        
    public DefaultAlertDialog setTitleBackgroundColor(@ColorInt int color) {
        titleBackColor = color;
        tvTitle.setBackground(ImageUtils.createDrawable(color, cornerRadii, true, true, false, false));
        return this;
    }
    
    public DefaultAlertDialog setTitleDividerColor(@ColorInt int color) {
        titleDivider.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置标题分割线高度
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
    
    public DefaultAlertDialog setContentView(@NonNull View view) {
        layoutContent.removeAllViews();
        layoutContent.addView(view);
        return this;
    }

    public DefaultAlertDialog setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
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

    public DefaultAlertDialog setMessageTypeface(Typeface tf) {
        tvMsg.setTypeface(tf);
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

    public DefaultAlertDialog setSubMessage(int resId) {
        tvSubMsg.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        tvSubMsg.setText(resId);
        return this;
    }

    public DefaultAlertDialog setSubMessageTextColor(@ColorInt int color) {
        tvSubMsg.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setSubMessageTypeface(Typeface tf) {
        tvSubMsg.setTypeface(tf);
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

    public DefaultAlertDialog setNegativeButtonTextColor(@ColorInt int color) {
        tvNegative.setTextColor(color);
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

    public DefaultAlertDialog setNegativeButtonTextSize(int unit, float size) {
        tvNegative.setTextSize(unit, size);
        return this;
    }

    public DefaultAlertDialog setPositiveButton(CharSequence text, View.OnClickListener listener) {
        positiveVisible = true;
        tvPositive.setText(text);
        positiveListener = listener;
        updateButtonVisible();
        return this;
    }

    public DefaultAlertDialog setPositiveButton(@StringRes int resId, View.OnClickListener listener) {
        positiveVisible = true;
        tvPositive.setText(resId);
        positiveListener = listener;
        updateButtonVisible();
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTextColor(@ColorInt int color) {
        tvPositive.setTextColor(color);
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTextColor(int normal, int pressed) {
        tvPositive.setTextColor(getColorStateList(normal, pressed));
        return this;
    }

    public DefaultAlertDialog setsetPositiveButtonTextColor(int normal, int pressed, int disabled) {
        tvPositive.setTextColor(getColorStateList(normal, pressed, disabled));
        return this;
    }
    
    public DefaultAlertDialog setPositiveButtonTextSize(int unit, float size) {
        tvPositive.setTextSize(unit, size);
        return this;
    }

    public DefaultAlertDialog setPositiveButtonTypeface(Typeface tf) {
        tvPositive.setTypeface(tf);
        return this;
    }

    private ColorStateList getColorStateList(int normal, int pressed) {
        int[][] states = new int[][]{{android.R.attr.state_pressed}, {}};
        return new ColorStateList(states, new int[]{pressed, normal});
    }

    private ColorStateList getColorStateList(int normal, int pressed, int disabled) {
        int[][] states = new int[][]{{android.R.attr.state_pressed}, {-android.R.attr.state_enabled}, {}};
        return new ColorStateList(states, new int[]{pressed, disabled, normal});
    }
}
