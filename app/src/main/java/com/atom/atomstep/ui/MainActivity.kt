package com.atom.atomstep.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingActivity
import com.atom.atomstep.databinding.ActivityMainBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.service.StepService
import com.atom.atomstep.utils.StepCountCheckUtil
import com.gyf.immersionbar.ImmersionBar

class MainActivity : BaseDataBindingActivity() {

    private val mBinding by binding<ActivityMainBinding>(R.layout.activity_main)

    private lateinit var mainPageAdapter: MainPageAdapter

    private val homeTag = 0
    private val tableTag = 1
    private val mineTag = 2

    private var curTag = homeTag
    override fun initStatusBar() {
        ImmersionBar.with(this).transparentStatusBar().statusBarDarkFont(false)
            .keyboardEnable(false).init()
    }

    override fun init(savedInstanceState: Bundle?) {
        mainPageAdapter = MainPageAdapter(this)


        mBinding.apply {
            vpMain.adapter = mainPageAdapter

            vpMain.offscreenPageLimit = 3
            vpMain.isUserInputEnabled = false

            curTag = homeTag
            showTag(curTag)
            vpMain.setCurrentItem(curTag, false)
            ImmersionBar.with(this@MainActivity).statusBarDarkFont(true).init()
            clBottomHome.throttleClick {
                curTag = homeTag
                showTag(curTag)
                vpMain.setCurrentItem(curTag, false)
                ImmersionBar.with(this@MainActivity).statusBarDarkFont(true).init()
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
        checkNotificationEnabled()
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1
                )
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACTIVITY_RECOGNITION
                    )
                ) {
                    //此处需要弹窗通知用户去设置权限
                    Toast.makeText(
                        this, "请允许获取健身运动信息，不然不能为你计步哦~", Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                startStepService()
            }
        } else {
            startStepService()
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

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startStepService()
                } else {
                    Toast.makeText(
                        this, "请允许获取健身运动信息，不然不能为你计步哦~", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * 开启计步服务
     */
    private fun setupService() {
        val intent = Intent(this, StepService::class.java)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startForegroundService(intent)
        else
            startService(intent)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun checkNotificationEnabled() {
        val isEnabled = isNotificationEnabled(this)
        if (!isEnabled) {
            val intent = Intent()
            if (Build.VERSION.SDK_INT >= 26) {
                // android 8.0引导
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            } else if (Build.VERSION.SDK_INT >= 21) {
                // android 5.0-7.0
                intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                intent.putExtra("app_package", packageName)
                intent.putExtra("app_uid", applicationInfo.uid)
            } else {
                // 其他
                intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                intent.data = Uri.fromParts("package", packageName, null)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun isNotificationEnabled(context: Context): Boolean {
        val notificationManagerCompat: NotificationManagerCompat =
            NotificationManagerCompat.from(context)
        return notificationManagerCompat.areNotificationsEnabled()
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