package com.atom.atomstep.ui.splash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.atom.atomstep.ui.home.HomeFragment
import com.atom.atomstep.ui.mine.MineFragment
import com.atom.atomstep.ui.table.TableFragment

/**
 *  author : liuxe
 *  date : 2023/10/16 16:28
 *  description :
 */
class FirstSettingPageAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragmentList = listOf(
        SelectGenderFragment(),
        SelectAgeFragment(),
        SelectHeightFragment(),
        SelectWeightFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}