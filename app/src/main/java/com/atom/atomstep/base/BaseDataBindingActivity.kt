package com.atom.atomstep.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atom.atomstep.R
import com.gyf.immersionbar.ImmersionBar
import com.atom.atomstep.utils.ToastUtil


abstract class BaseDataBindingActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }

    /**
     * 初始化默认的viewModel
     */
    protected inline fun <reified VM : BaseViewModel> createViewModel(): Lazy<VM> = lazy {
        val mViewModel = ViewModelProvider(this)[VM::class.java]
        mViewModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initStatusBar()
        init(savedInstanceState)
    }

    protected abstract fun init(savedInstanceState: Bundle?)

    fun toast(str: String) {
        ToastUtil.showToast(str)
    }

    open fun initStatusBar() {
        ImmersionBar.with(this)
            .statusBarColor(R.color.white)
            .autoDarkModeEnable(true, 0.2f)
            .fitsSystemWindows(true)
            .keyboardEnable(false)
            .init()
    }

}