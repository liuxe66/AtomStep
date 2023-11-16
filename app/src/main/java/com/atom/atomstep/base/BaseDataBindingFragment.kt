package com.atom.atomstep.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atom.atomstep.utils.ToastUtil


abstract class BaseDataBindingFragment : Fragment() {

    protected inline fun <reified T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes resId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate(inflater, resId, container, false)

    fun toast(str: String) {
        ToastUtil.showToast(str)
    }

    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]

        mViewModel
    }
}