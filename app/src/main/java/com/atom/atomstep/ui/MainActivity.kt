package com.atom.atomstep.ui

import android.os.Bundle
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingActivity
import com.atom.atomstep.databinding.ActivityMainBinding
import com.atom.atomstep.ext.throttleClick
import com.gyf.immersionbar.ImmersionBar

class MainActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityMainBinding>(R.layout.activity_main)

    private lateinit var mainPageAdapter: MainPageAdapter

    private val homeTag = 0
    private val tableTag = 1
    private val mineTag = 2

    private var curTag = homeTag
    override fun initStatusBar() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .statusBarDarkFont(false)
            .keyboardEnable(false)
            .init()
    }

    override fun init(savedInstanceState: Bundle?) {
        mainPageAdapter = MainPageAdapter(this)
        mBinding.apply {
            vpMain.adapter = mainPageAdapter

            vpMain.offscreenPageLimit = 3
            vpMain.isUserInputEnabled = false

            curTag= homeTag
            showTag(curTag)
            vpMain.setCurrentItem(curTag, false)
            ImmersionBar.with(this@MainActivity)
                .statusBarDarkFont(true)
                .init()
            clBottomHome.throttleClick {
                curTag = homeTag
                showTag(curTag)
                vpMain.setCurrentItem(curTag, false)
                ImmersionBar.with(this@MainActivity)
                    .statusBarDarkFont(true)
                    .init()
            }
            clBottomRank.throttleClick {
                curTag = tableTag
                showTag(curTag)
                vpMain.setCurrentItem(curTag, false)
                ImmersionBar.with(this@MainActivity)
                    .statusBarDarkFont(false)
                    .init()
            }
            clBottomMine.throttleClick {
                curTag = mineTag
                showTag(curTag)
                vpMain.setCurrentItem(curTag, false)
                ImmersionBar.with(this@MainActivity)
                    .statusBarDarkFont(false)
                    .init()
            }
        }
    }

    override fun onBackPressed() {
        toast("退出")
    }


    /**
     * 切换底部标签
     * @param homeTAG Int
     */
    fun showTag(tag: Int) {

        mBinding.apply {
            ivBottomHome.isSelected = false
            ivBottomMine.isSelected = false
            ivBottomRank.isSelected = false
            when (tag) {
                homeTag -> {
                    ivBottomHome.isSelected = true
                }

                tableTag -> {
                    ivBottomRank.isSelected = true
                }

                mineTag -> {
                    ivBottomMine.isSelected = true
                }
            }
        }

    }

}