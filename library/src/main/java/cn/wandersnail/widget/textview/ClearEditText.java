package cn.wandersnail.widget.textview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import cn.wandersnail.widget.R;

/**
 * 带可清除的EditText
 * <p>
 * date: 2019/8/22 22:52
 * author: zengfansheng
 */
public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    //EditText右侧的删除按钮
    private Drawable clearDrawable;
    private boolean hasFocus;
    private FocusChangeListener focusChangeListener;

    public interface FocusChangeListener {
        void onFocusChange(boolean hasFocus);
    }

    public ClearEditText(Context context) {
        this(context, null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3）  
        clearDrawable = getCompoundDrawables()[2];
        if (clearDrawable == null) {
            clearDrawable = ContextCompat.getDrawable(context, R.drawable.ic_clear);
        }
        // 默认设置隐藏图标  
        setClearIconVisible(false);
        // 设置焦点改变的监听  
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听  
        addTextChangedListener(this);
    }

    public void setFocusChangeListener(FocusChangeListener listener) {
        focusChangeListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!enabled) {
            setClearIconVisible(false); //隐藏消除图标
        }
        super.setEnabled(enabled);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                //getCompoundPaddingRight()它的值就是清空小图标的宽度加上paddingRight的值
                if (getWidth() - event.getX() <= getCompoundPaddingRight()) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus && isEnabled()) {
            setClearIconVisible(getText() != null && getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (focusChangeListener != null) {
            focusChangeListener.onFocusChange(hasFocus);
        }
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? clearDrawable : null;
        setCompoundDrawablesWithIntrinsicBounds(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }
}
