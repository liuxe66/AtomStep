package com.atom.atomstep.ui

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseBottomSheetDialogFragment
import com.atom.atomstep.databinding.DialogAppOutBinding
import com.atom.atomstep.databinding.DialogGenderBinding
import com.atom.atomstep.databinding.FragmentSelectBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.Preference
import com.atom.atomstep.utils.Preference.Companion.userAge
import com.atom.atomstep.utils.Preference.Companion.userGender
import com.atom.atomstep.widget.wheelview.contract.OnWheelChangedListener
import com.atom.atomstep.widget.wheelview.widget.WheelView

/**
 *  author : liuxe
 *  date : 2023/11/23 13:28
 *  description :
 */
class AppOutDialog : BaseBottomSheetDialogFragment() {
    private lateinit var mBinding: DialogAppOutBinding

    lateinit var callback: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.dialog_app_out, container)

        mBinding.apply {
            tvSubmit.throttleClick {
                callback.invoke()
                dialog?.dismiss()
            }
        }
        return mBinding.root
    }
}