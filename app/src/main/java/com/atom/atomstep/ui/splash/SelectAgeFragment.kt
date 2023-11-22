package com.atom.atomstep.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.databinding.FragmentSelectAgeBinding
import com.atom.atomstep.databinding.FragmentSelectGenderBinding
import com.atom.atomstep.databinding.FragmentSelectWeightBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.utils.Preference.Companion.userWeight
import com.atom.atomstep.widget.wheelview.contract.OnWheelChangedListener
import com.atom.atomstep.widget.wheelview.widget.WheelView

/**
 *  author : liuxe
 *  date : 2023/11/20 14:53
 *  description :
 */
class SelectAgeFragment : BaseDataBindingFragment() {

    private lateinit var mBinding: FragmentSelectAgeBinding

    private var userAge by Preference(Preference.userAge,18)
    private var vibrator: Vibrator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        mBinding = binding(inflater, R.layout.fragment_select_age, container)

        userAge = 18
        mBinding.apply {
            wheelAge.setData(setRange(10, 60, 1), 8)
            wheelAge.setOnWheelChangedListener(object : OnWheelChangedListener {
                override fun onWheelScrolled(view: WheelView?, offset: Int) {

                }

                override fun onWheelSelected(view: WheelView?, position: Int) {
                    userAge = 10 + position
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