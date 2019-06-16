package com.snail.widget.listener

import android.view.View
import android.widget.AdapterView

/**
 *
 *
 * date: 2019-06-16 12:28
 * author: zengfansheng
 */
class RejectFastItemClickListener @JvmOverloads constructor(private val callback: RejectableItemClickCallback, minInterval: Long = 1000) :
    RejectFastClickListener(minInterval), AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        check(view) {
            if (it) {
                callback.onAccept(parent, view!!, position, id)
            } else {
                callback.onReject(parent, view!!, position, id)
            }
        }
    }
}