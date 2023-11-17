package com.atom.atomstep.ui.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.databinding.FragmentHomeBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.vm.StepVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_home, container)

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())


            tvDrinkAdd.throttleClick {
               setDatas()
            }
        }
        setDatas()


        return mBinding.root
    }


    /**
     * 设置记录数据
     */
    private fun setDatas() {

        mStepVM.queryStepToday().observe(requireActivity(), Observer {

            LogUtils.e("stepEntity:$it")

            val curStepNum = it?.step ?: 0

            mBinding.apply {
                if (curStepNum == 0) {
                    tvStepNum.text = "0"
                } else {
                    val animator = ValueAnimator.ofInt(lastStepNum, curStepNum) // 从0增加到1000

                    animator.duration = 1000

                    animator.addUpdateListener { animation ->
                        val value = animation.animatedValue as Int
                        tvStepNum.text = value.toString() // 在TextView中显示动画值
                        lastStepNum = curStepNum
                    }
                    animator.start() // 开始动画
                }


                var progressValue = (curStepNum / 6000 * 100).toInt().toFloat()
                progressStepNum.setProgress(progressValue)

                tvTodayStep.text = curStepNum.toString()
                tvTodayCal.text = step2Cal(curStepNum)
                tvTodayKm.text = step2km(curStepNum)
            }
        })

    }

    /**
     * 简易计算公里数，假设一步大约有0.7米
     *
     * @param steps 用户当前步数
     * @return
     */
    private fun step2km(steps: Int): String {
        val totalMeters = steps * 0.7
        //保留两位有效数字
        return df.format(totalMeters / 1000)
    }

    /**
     * 步数转换卡路里
     *
     * 男每一步消耗 5千卡
     * 女每一步消耗 4千卡
     *
     */
    private fun step2Cal(steps: Int): String {
        val totalCal = steps * 5
        //保留两位有效数字
        return totalCal.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}