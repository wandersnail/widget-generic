package com.snail.widget.textview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.snail.widget.R

/**
 * Created by zeng on 2016/4/9.
 * 带可清除的EditText
 */
open class ClearEditText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = android.R.attr.editTextStyle) : AppCompatEditText(context, attrs, defStyle), View.OnFocusChangeListener, TextWatcher {
    //EditText右侧的删除按钮  
    private var mClearDrawable: Drawable? = null
    private var hasFoucs: Boolean = false
    private var focusChangeListener: FocusChangeListener? = null

    interface FocusChangeListener {
        fun onFocusChange(hasFocus: Boolean)
    }

    init {
        init()
    }

    private fun init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片,获取图片的顺序是左上右下（0,1,2,3）  
        mClearDrawable = compoundDrawables[2]
        if (mClearDrawable == null) {
            mClearDrawable = ContextCompat.getDrawable(context, R.drawable.ic_clear)
        }
        // 默认设置隐藏图标  
        setClearIconVisible(false)
        // 设置焦点改变的监听  
        onFocusChangeListener = this
        // 设置输入框里面内容发生改变的监听  
        addTextChangedListener(this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            if (compoundDrawables[2] != null) {
                //getCompoundPaddingRight()它的值就是清空小图标的宽度加上paddingRight的值
                if (width - event.x <= compoundPaddingRight) {
                    setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun setEnabled(enabled: Boolean) {
        if (!enabled) {
            setClearIconVisible(false) //隐藏消除图标
        }
        super.setEnabled(enabled)
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        this.hasFoucs = hasFocus
        if (hasFocus && isEnabled) {
            setClearIconVisible(text!!.isNotEmpty())
        } else {
            setClearIconVisible(false)
        }
        if (focusChangeListener != null) {
            focusChangeListener!!.onFocusChange(hasFocus)
        }
    }

    fun setFocusChangeListener(listener: FocusChangeListener) {
        focusChangeListener = listener
    }

    private fun setClearIconVisible(visible: Boolean) {
        val right = if (visible) mClearDrawable else null
        setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], compoundDrawables[1], right, compoundDrawables[3])
    }

    override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        if (hasFoucs) {
            setClearIconVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}
}