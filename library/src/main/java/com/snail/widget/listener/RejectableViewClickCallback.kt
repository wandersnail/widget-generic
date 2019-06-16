package com.snail.widget.listener

import android.view.View

/**
 * 可以控制是否执行点击事件回调
 *
 * date: 2019-06-16 11:55
 * author: zengfansheng
 */
interface RejectableViewClickCallback {
    fun onReject(v: View)

    fun onAccept(v: View)
}