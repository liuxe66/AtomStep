package com.atom.atomstep.ui.mine

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseBottomSheetDialogFragment
import com.atom.atomstep.databinding.FragmentSelectBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.utils.Preference.Companion.userAge
import com.atom.atomstep.widget.wheelview.contract.OnWheelChangedListener
import com.atom.atomstep.widget.wheelview.widget.WheelView

/**
 *  author : liuxe
 *  date : 2023/11/23 13:28
 *  description :
 */
class SelectGoatDialog : BaseBottomSheetDialogFragment() {
    private lateinit var mBinding: FragmentSelectBinding

    private var vibrator: Vibrator? = null
    private var goatStep by Preference(Preference.goatStep, 5000)
    private var temp = goatStep
    lateinit var callback: () -> Unit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_select, container)

        mBinding.apply {

            tvTitle.text = "目标步数"
            ivClose.setOnClickListener {
                callback.invoke()
                dialog?.dismiss()
            }
            tvSubmit.throttleClick {
                goatStep = temp
                callback.invoke()
                dialog?.dismiss()
            }
            wheelView.setData(setRange(1000, 12000, 1000), (temp - 1000) / 1000)
            wheelView.setOnWheelChangedListener(object : OnWheelChangedListener {
                override fun onWheelScrolled(view: WheelView?, offset: Int) {

                }

                override fun onWheelSelected(view: WheelView?, position: Int) {
                    temp = (1 + position) * 1000
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