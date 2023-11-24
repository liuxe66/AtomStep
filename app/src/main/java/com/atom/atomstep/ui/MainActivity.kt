package com.atom.atomstep.ui

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.accessibility.AccessibilityManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.atom.atomstep.R
import com.atom.atomstep.app.AtomApp
import com.atom.atomstep.base.BaseDataBindingActivity
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.databinding.ActivityMainBinding
import com.atom.atomstep.ext.launchActivity
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.ext.toActivity
import com.atom.atomstep.service.StepService
import com.atom.atomstep.ui.mine.SelectAgeDialog
import com.atom.atomstep.ui.splash.FirstSettingActivity
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.utils.StepCountCheckUtil
import com.atom.atomstep.vm.StepVM
import com.gyf.immersionbar.ImmersionBar
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


class MainActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityMainBinding>(R.layout.activity_main)

    private lateinit var mainPageAdapter: MainPageAdapter

    private var isFirstOpen by Preference(Preference.firstOpen,true)

    private val homeTag = 0
    private val tableTag = 1
    private val mineTag = 2

    private var curTag = homeTag

    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(false)
            .keyboardEnable(false).init()
    }

    override fun init(savedInstanceState: Bundle?) {

        // 安装 SplashScreen
        val splashScreen = installSplashScreen()

        // 设置 SplashScreen 的显示时间
        splashScreen.setKeepVisibleCondition{
            MainScope().launch {
                delay(1000)
            }
            false
        }

        if (isFirstOpen){
            toActivity<FirstSettingActivity> {  }
        } else {
            mainPageAdapter = MainPageAdapter(this)

            mBinding.apply {
                vpMain.adapter = mainPageAdapter

                vpMain.offscreenPageLimit = 1
                vpMain.isUserInputEnabled = false

                curTag = homeTag
                showTag(curTag)
                vpMain.setCurrentItem(curTag, false)
                ImmersionBar.with(this@MainActivity).statusBarDarkFont(false).init()
                clBottomHome.throttleClick {
                    curTag = homeTag
                    showTag(curTag)
                    vpMain.setCurrentItem(curTag, false)
                    ImmersionBar.with(this@MainActivity).statusBarDarkFont(false).init()
                }
                clBottomRank.throttleClick {
                    curTag = tableTag
                    showTag(curTag)
                    vpMain.setCurrentItem(curTag, false)
                    ImmersionBar.with(this@MainActivity).statusBarDarkFont(false).init()
                }
                clBottomMine.throttleClick {
                    curTag = mineTag
                    showTag(curTag)
                    vpMain.setCurrentItem(curTag, false)
                    ImmersionBar.with(this@MainActivity).statusBarDarkFont(false).init()
                }
            }



            requestPermission()
        }
    }


    private fun requestPermission() {

        PermissionX.init(this).permissions(
            Manifest.permission.ACTIVITY_RECOGNITION,
            PermissionX.permission.POST_NOTIFICATIONS
        ).request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                LogUtils.e("权限被允许: $grantedList")
                startStepService()
            } else {
                LogUtils.e("权限被拒绝: $deniedList")
                toast("请允许获取健身运动信息，不然不能为你计步哦~")
            }
        }

    }

    private fun startStepService() {
        /**
         * 这里判断当前设备是否支持计步
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            setupService()
        } else {
            toast("暂不支持计步功能")
        }
    }

    /**
     * 开启计步服务
     */
    private fun setupService() {
        val intent = Intent(this, StepService::class.java)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) startForegroundService(intent)
        else startService(intent)
    }


    override fun onBackPressed() {
        val dialog = AppOutDialog()
        dialog.show(supportFragmentManager, "appout")
        dialog.callback = {
           finish()
        }
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