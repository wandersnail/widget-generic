package com.snail.widget.listview

import android.view.View

/**
 * 主要用于ListView的item布局创建及数据设置
 */
abstract class BaseViewHolder<T> {
    /**
     * 和Adapter绑定了，可在此设置View的数据，更新View
     */
    abstract fun onBind(item: T, position: Int)

    /**
     * 创建界面
     */
    abstract fun createView(): View
}