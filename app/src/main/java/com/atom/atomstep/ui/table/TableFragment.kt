package com.atom.atomstep.ui.table

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.data.constant.ConstantData
import com.atom.atomstep.databinding.FragmentTableBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.utils.Preference.Companion.userHeight
import com.atom.atomstep.vm.StepVM
import com.google.gson.Gson
import java.text.DecimalFormat
import java.time.DayOfWeek
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2023/10/16 14:39
 *  description :
 */
class TableFragment : BaseDataBindingFragment() {
    private lateinit var mBinding: FragmentTableBinding

    private val mStepVM by createViewModel<StepVM>()
    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var goatStep by Preference(Preference.goatStep, 5000)
    private var tableType by Preference(Preference.tableType, 1)
    private var userHeight by Preference(Preference.userHeight, 170)
    private val df = DecimalFormat("#.##")
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(
            mBroadcastReceiver, IntentFilter(ConstantData.ACTION_DATA)
        )
//        updateView()
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(mBroadcastReceiver)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_table, container)

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())

            mBroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == ConstantData.ACTION_DATA) {
                        updateView()
                    }
                }
            }

            webEchart.settings.allowFileAccess = true;
            //开启脚本支持
            webEchart.settings.javaScriptEnabled = true;
            if (tableType == 1) {
                tvBar.isSelected = true
                tvLine.isSelected = false
                webEchart.loadUrl("file:///android_asset/indexbar.html")
            } else {
                tvBar.isSelected = false
                tvLine.isSelected = true
                webEchart.loadUrl("file:///android_asset/index.html")
            }

            tvBar.throttleClick {
                tableType = 1
                tvBar.isSelected = true
                tvLine.isSelected = false
                webEchart.loadUrl("file:///android_asset/indexbar.html")
            }

            tvLine.throttleClick {
                tableType = 0
                tvBar.isSelected = false
                tvLine.isSelected = true
                webEchart.loadUrl("file:///android_asset/index.html")
            }

            webEchart.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    if (newProgress == 100) {
                        updateView()
                    }
                    super.onProgressChanged(view, newProgress)
                }
            }


        }


        return mBinding.root
    }

    fun updateView() {
        mStepVM.queryStepCurWeek().observe(requireActivity(), Observer {
            var defaultIndex = 0
            val xData = mutableListOf<String>()
            val yData = mutableListOf<Int>()
            var dayOfWeek = 0
            var stepOfWeek = 0

            var weekTimeStr =
                        "" + it[0].date.monthValue + "月" + it[0].date.dayOfMonth + "日" +
                        "-" + it[6].date.monthValue + "月" + it[6].date.dayOfMonth + "日"

            for (index in it.indices) {
                if (LocalDate.now() == it[index].date) {
                    defaultIndex = index
                }

                if (LocalDate.now() >= it[index].date) {
                    dayOfWeek++
                    stepOfWeek += it[index].step
                }

                val chineseDayOfWeek = if (LocalDate.now() == it[index].date) {
                    "今"
                } else {
                    when (it[index].date.dayOfWeek) {
                        DayOfWeek.SUNDAY -> "日"
                        DayOfWeek.MONDAY -> "一"
                        DayOfWeek.TUESDAY -> "二"
                        DayOfWeek.WEDNESDAY -> "三"
                        DayOfWeek.THURSDAY -> "四"
                        DayOfWeek.FRIDAY -> "五"
                        DayOfWeek.SATURDAY -> "六"
                    }
                }
                xData.add(chineseDayOfWeek)
                yData.add(it[index].step)
            }

            mBinding.apply {

                mBinding.apply {
                    tvTotalStep.text = stepOfWeek.toString()
                    tvTime.text = weekTimeStr
                    tvWeekStep.text = stepOfWeek.toString()
                    tvWeekMiles.text = step2km(stepOfWeek)
                    tvWeekTime.text = step2time(stepOfWeek)
                    tvWeekCalories.text = step2Cal(stepOfWeek)
                }
                webEchart.loadUrl(
                    "javascript:doCreateChart($defaultIndex,${Gson().toJson(xData)},${
                        Gson().toJson(
                            yData
                        )
                    });"
                )
            }


        })
    }
    /**
     * 简易计算运行时间
     * @param steps Int
     * @return String
     */
    private fun step2time(steps: Int): String {
        return (steps * 0.60 / 60).toInt().toString()
    }

    /**
     * 简易计算公里数，身高X0.4 步长
     *
     * @param steps 用户当前步数
     * @return
     */
    private fun step2km(steps: Int): String {
        val totalMeters = userHeight / 100 * 0.4 * steps
        //保留两位有效数字
        return df.format(totalMeters / 1000)
    }

    /**
     * 步数转换卡路里
     * 每一步消耗 0.04千卡
     */
    private fun step2Cal(steps: Int): String {
        val totalCal = steps * 0.04
        //保留两位有效数字
        return df.format(totalCal)
    }

}