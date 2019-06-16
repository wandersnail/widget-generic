package com.snail.widget.listener

import android.view.View
import android.widget.AdapterView

/**
 * 可以控制是否执行点击事件回调
 *
 * date: 2019-06-16 12:15
 * author: zengfansheng
 */
interface RejectableItemClickCallback : AdapterView.OnItemClickListener {
    fun onReject(parent: AdapterView<*>?, view: View, position: Int, id: Long)

    fun onAccept(parent: AdapterView<*>?, view: View, position: Int, id: Long)
}