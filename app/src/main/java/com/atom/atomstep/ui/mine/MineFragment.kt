package com.atom.atomstep.ui.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import coil.transform.CircleCropTransformation
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.databinding.FragmentMineBinding
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.Preference

/**
 *  author : liuxe
 *  date : 2023/10/16 14:39
 *  description :
 */
class MineFragment : BaseDataBindingFragment() {
    private lateinit var mBinding: FragmentMineBinding
    private var userGender by Preference(Preference.userGender, 18)
    private var userAge by Preference(Preference.userAge, 18)
    private var userHeight by Preference(Preference.userHeight, 18)
    private var userWeight by Preference(Preference.userWeight, 18)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_mine, container)

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())

            if (userGender == 1) {
                ivPhoto.load(R.drawable.ic_gender_man) {
                    transformations(CircleCropTransformation())
                }
            } else {
                ivPhoto.load(R.drawable.ic_gender_woman) {
                    transformations(CircleCropTransformation())
                }
            }

            tvAge.text = userAge.toString()
            tvHeight.text = "$userHeight cm"
            tvWeight.text = "$userWeight kg"
            tvGender.text = if (userGender == 1) {
                "男"
            } else {
                "女"
            }
        }


        return mBinding.root
    }

}