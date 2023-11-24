package com.atom.atomstep.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.databinding.FragmentSelectGenderBinding
import com.atom.atomstep.ext.throttleClick
import com.atom.atomstep.utils.Preference

/**
 *  author : liuxe
 *  date : 2023/11/20 14:53
 *  description :
 */
class SelectGenderFragment : BaseDataBindingFragment() {

    private lateinit var mBinding: FragmentSelectGenderBinding

    private var userGender by Preference(Preference.userGender,1)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_select_gender, container)

        mBinding.apply {
            llMan.isSelected = true

            llMan.throttleClick {
                userGender = 1
                llMan.isSelected  =true
                llWoman.isSelected  = false
            }

            llWoman.throttleClick {
                userGender = 0
                llMan.isSelected  =false
                llWoman.isSelected  = true
            }

        }
        return mBinding.root
    }
}