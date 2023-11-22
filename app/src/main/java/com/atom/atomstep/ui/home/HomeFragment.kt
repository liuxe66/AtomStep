package com.atom.atomstep.ui.home

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.data.constant.ConstantData.Companion.ACTION_DATA
import com.atom.atomstep.databinding.FragmentHomeBinding
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.utils.Preference.Companion.userHeight
import com.atom.atomstep.vm.StepVM
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.LocalDate


/**
 *  author : liuxe
 *  date : 2023/10/16 14:39
 *  description :
 */
class HomeFragment : BaseDataBindingFragment() {
    private lateinit var mBinding: FragmentHomeBinding

    private val df = DecimalFormat("#.##")
    private val mStepVM by createViewModel<StepVM>()

    private var lastStepNum = 0

    private var mBroadcastReceiver: BroadcastReceiver? = null
    private var userGender by Preference(Preference.userGender, 1)
    private var userHeight by Preference(Preference.userHeight, 1)
    private var mStepWeekAdapter = StepWeekAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_home, container)

        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_DATA) {
                    updateView()
                }
            }
        }

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())

            rvWeek.layoutManager = GridLayoutManager(requireActivity(), 7)
            rvWeek.adapter = mStepWeekAdapter

            updateView()
        }
        return mBinding.root
    }

    private fun updateView() {
        mStepVM.queryStepCurWeek().observe(requireActivity(), Observer {
            LogUtils.e("===queryStepCurWeek===")
            mStepWeekAdapter.submitList(it)
        })

        mStepVM.queryStepToday().observe(requireActivity(), Observer {
            setDatas(it?.step ?: 0)
        })
    }


    var hasPlayAnim = false

    /**
     * 设置记录数据
     */
    private fun setDatas(curStepNum: Int) {

        mBinding.apply {

            val animator = ValueAnimator.ofInt(lastStepNum, curStepNum) // 从0增加到1000

            animator.duration = 500

            animator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                tvStepNum.text = value.toString() // 在TextView中显示动画值
                lastStepNum = curStepNum
            }
            animator.start() // 开始动画


            val progressValue = (60000f / 6000 * 100)
            if (progressValue >= 100) {
                progressStepNum.setProgress(100f)
            } else {
                progressStepNum.setProgress(progressValue)
            }
            if (!hasPlayAnim) {
                progressStepNum.startAnimal()
                hasPlayAnim = true
            }


            tvTodayStep.text = step2time(curStepNum)
            tvTodayCal.text = step2Cal(curStepNum)
            tvTodayKm.text = step2km(curStepNum)

            if (userGender == 1) {
                ivPerson.load(R.drawable.ic_man)
            } else {
                ivPerson.load(R.drawable.ic_woman)
            }
        }


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
     * 每一步消耗 0.05千卡
     */
    private fun step2Cal(steps: Int): String {
        val totalCal = steps * 0.04
        //保留两位有效数字
        return df.format(totalCal)
    }


    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override fun onResume() {
        super.onResume()
        requireActivity().registerReceiver(mBroadcastReceiver, IntentFilter(ACTION_DATA))
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(mBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}