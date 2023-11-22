package com.atom.atomstep.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.databinding.FragmentSelectHeightBinding
import com.atom.atomstep.databinding.FragmentSelectWeightBinding
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.widget.wheelview.contract.OnWheelChangedListener
import com.atom.atomstep.widget.wheelview.widget.WheelView

/**
 *  author : liuxe
 *  date : 2023/11/20 14:53
 *  description :
 */
class SelectHeightFragment : BaseDataBindingFragment() {

    private lateinit var mBinding: FragmentSelectHeightBinding

    private var userHeight by Preference(Preference.userHeight,170)
    private var vibrator: Vibrator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        mBinding = binding(inflater, R.layout.fragment_select_height, container)

        userHeight = 170
        mBinding.apply {
            wheelHeight.setData(setRange(60, 220, 1), 110)
            wheelHeight.setOnWheelChangedListener(object : OnWheelChangedListener {
                override fun onWheelScrolled(view: WheelView?, offset: Int) {

                }

                override fun onWheelSelected(view: WheelView?, position: Int) {
                    userHeight = 60 + position
                }

                override fun onWheelScrollStateChanged(view: WheelView?, state: Int) {
                }

                override fun onWheelLoopFinished(view: WheelView?) {
                }

                override fun onWheelChanged() {
                    vibrate()
                }

            })
        }
        return mBinding.root
    }


    fun setRange(min: Int, max: Int, step: Int): MutableList<String> {
        val minValue = Math.min(min, max)
        val maxValue = Math.max(min, max)
        // 指定初始容量，避免OutOfMemory
        val capacity = (maxValue - minValue) / step
        val data: MutableList<String> = ArrayList(capacity)
        var i = minValue
        while (i <= maxValue) {
            data.add(i.toString())
            i += step
        }
        return data
    }

    private fun vibrate() {
        vibrator?.cancel()
        vibrator?.vibrate(VibrationEffect.createOneShot(50, 255))
    }
}