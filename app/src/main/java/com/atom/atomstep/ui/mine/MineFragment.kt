package com.atom.atomstep.ui.mine

import android.Manifest
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.databinding.FragmentHomeBinding
import com.atom.atomstep.databinding.FragmentMineBinding
import com.atom.atomstep.service.StepService
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.StepCountCheckUtil
import com.atom.atomstep.vm.StepVM
import com.drake.channel.receiveTag
import com.drake.channel.sendTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DecimalFormat

/**
 *  author : liuxe
 *  date : 2023/10/16 14:39
 *  description :
 */
class MineFragment : BaseDataBindingFragment() {
    private lateinit var mBinding: FragmentMineBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_mine, container)

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())
        }


        return mBinding.root
    }

}