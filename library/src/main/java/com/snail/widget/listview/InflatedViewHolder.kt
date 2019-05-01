package com.snail.widget.listview

import android.content.Context
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 * 自动填充
 *
 * date: 2019/5/1 15:52
 * author: zengfansheng
 */
abstract class InflatedViewHolder<T>(context: Context, @LayoutRes private val layoutResId: Int) : BaseViewHolder<T>() {
    val rootView: View = View.inflate(context, layoutResId, null)

    override fun createView(): View {
        return rootView
    }
    
    fun <V : View> getView(@IdRes resId: Int): V {
        return rootView.findViewById(resId)
    }
}