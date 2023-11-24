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
import com.atom.atomstep.databinding.DialogGenderBinding
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
class SelectGenderDialog : BaseBottomSheetDialogFragment() {
    private lateinit var mBinding: DialogGenderBinding

    private var userGender by Preference(Preference.userGender, 18)
    private var temp = userGender
    lateinit var callback: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.dialog_gender, container)

        mBinding.apply {

            tvTitle.text = "年龄"
            ivClose.setOnClickListener {
                callback.invoke()
                dialog?.dismiss()
            }
            tvSubmit.throttleClick {
                userGender = temp
                callback.invoke()
                dialog?.dismiss()
            }

            if (userGender == 1){
                llMan.isSelected  =true
                llWoman.isSelected  = false
            } else {
                llMan.isSelected  =false
                llWoman.isSelected  = true
            }


            llMan.throttleClick {
                temp = 1
                llMan.isSelected  =true
                llWoman.isSelected  = false
            }

            llWoman.throttleClick {
                temp = 0
                llMan.isSelected  =false
                llWoman.isSelected  = true
            }

        }
        return mBinding.root
    }
}