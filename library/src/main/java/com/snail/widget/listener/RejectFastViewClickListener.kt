package com.snail.widget.listener

import android.view.View
import androidx.annotation.CallSuper

/**
 * 防止快速点击的
 *
 * date: 2019-06-16 11:59
 * author: zengfansheng
 *
 * @param minInterval 最小间隔，单位：毫秒
 */
class RejectFastViewClickListener @JvmOverloads constructor(private val callback: RejectableViewClickCallback, minInterval: Long = 1000) :
    RejectFastClickListener(minInterval), View.OnClickListener {

    @CallSuper
    override fun onClick(v: View?) {
        check(v) {
            if (it) {
                callback.onAccept(v!!)
            } else {
                callback.onReject(v!!)
            }
        }
    }
}