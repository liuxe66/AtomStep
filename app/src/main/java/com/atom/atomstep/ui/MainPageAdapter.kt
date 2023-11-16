package com.atom.atomstep.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.atom.atomstep.ui.home.HomeFragment

/**
 *  author : liuxe
 *  date : 2023/10/16 16:28
 *  description :
 */
class MainPageAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(
        HomeFragment(),
        HomeFragment(),
        HomeFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}