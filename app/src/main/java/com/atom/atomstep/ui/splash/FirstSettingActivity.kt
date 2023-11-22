package com.atom.atomstep.ui.splash

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingActivity
import com.atom.atomstep.databinding.ActivityFirstSettingBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.ext.toActivity
import com.atom.atomstep.ui.MainActivity
import com.atom.atomstep.ui.MainPageAdapter
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.widget.wheelview.contract.OnWheelChangedListener
import com.atom.atomstep.widget.wheelview.widget.WheelView
import com.gyf.immersionbar.ImmersionBar

class FirstSettingActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityFirstSettingBinding>(R.layout.activity_first_setting)
    private lateinit var settingPageAdapter: FirstSettingPageAdapter
    private var curIndex = 0
    private var userGender by Preference(Preference.userGender, 18)
    private var userAge by Preference(Preference.userAge, 18)
    private var userHeight by Preference(Preference.userHeight, 18)
    private var userWeight by Preference(Preference.userWeight, 18)
    private var firstOpen by Preference(Preference.firstOpen, true)
    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(false)
            .keyboardEnable(false).init()
    }

    override fun init(savedInstanceState: Bundle?) {
        settingPageAdapter = FirstSettingPageAdapter(this)
        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(this@FirstSettingActivity)

            vpSetting.adapter = settingPageAdapter

            vpSetting.offscreenPageLimit = 4
            vpSetting.isUserInputEnabled = false

            vpSetting.currentItem = curIndex

            tvSubmit.throttleClick {
                if (curIndex == 3) {
                    firstOpen = false
                    LogUtils.e("userGender:$userGender,userAge:$userAge,userHeight:$userHeight,userWeight:$userWeight")
                    toActivity<MainActivity>()
                } else {
                    curIndex++
                    vpSetting.currentItem = curIndex
                }

            }

            flBack.throttleClick {
                onBackPressed()
            }

        }


    }

    override fun onBackPressed() {
        if (curIndex == 0){
            super.onBackPressed()
        } else {
            curIndex--
            mBinding.vpSetting.currentItem = curIndex
        }

    }


}