package com.atom.atomstep.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atom.atomstep.R

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.atom.atomstep.utils.ToastUtil


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    protected inline fun <reified T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes resId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate(inflater, resId, container, false)

    fun toast(str: String) {
        ToastUtil.showToast(str)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheet)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = params
        //设置背景透明
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]
        mViewModel
    }

}