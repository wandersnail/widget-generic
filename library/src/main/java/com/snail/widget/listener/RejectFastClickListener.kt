package com.snail.widget.listener

import android.util.SparseLongArray
import android.view.View

/**
 * 防止快速点击的
 *
 * date: 2019-06-16 12:29
 * author: zengfansheng
 */
abstract class RejectFastClickListener(private val minInterval: Long = 1000) {
    private val clickTimeArr = SparseLongArray()

    protected fun check(v: View?, cb: (accept: Boolean) -> Unit) {
        if (v != null) {
            val lastClickTime = clickTimeArr[v.id]
            if (System.currentTimeMillis() - lastClickTime >= minInterval) {
                cb.invoke(true)
                clickTimeArr.put(v.id, System.currentTimeMillis())
            } else {
                cb.invoke(false)
            }
        }
    }
}