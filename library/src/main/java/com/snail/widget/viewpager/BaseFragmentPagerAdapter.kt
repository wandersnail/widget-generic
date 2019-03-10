package com.snail.widget.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

/**
 * 描述: 基本ViewPager的基类
 * 时间: 2018/8/26 22:57
 * 作者: zengfansheng
 */
open class BaseFragmentPagerAdapter(fm: FragmentManager, private val fragments: MutableList<out Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItemPosition(any: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}
