package com.atom.atomstep.ui.table

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.databinding.FragmentTableBinding
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.vm.StepVM

/**
 *  author : liuxe
 *  date : 2023/10/16 14:39
 *  description :
 */
class TableFragment : BaseDataBindingFragment() {
    private lateinit var mBinding: FragmentTableBinding

    private val mStepVM by createViewModel<StepVM>()
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_table, container)

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())

            webEchart.settings.allowFileAccess = true;
            //开启脚本支持
            webEchart.settings.javaScriptEnabled = true;
            webEchart.loadUrl("file:///android_asset/index.html")


        }


        return mBinding.root
    }

}