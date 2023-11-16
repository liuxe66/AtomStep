package com.atom.atomstep.ui.home

import android.Manifest
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.atom.atomstep.R
import com.atom.atomstep.base.BaseDataBindingFragment
import com.atom.atomstep.data.constant.ConstantData
import com.atom.atomstep.databinding.FragmentHomeBinding
import com.atom.atomstep.service.StepService
import com.atom.atomstep.utils.DimenUtils
import com.atom.atomstep.utils.StepCountCheckUtil
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

/**
 *  author : liuxe
 *  date : 2023/10/16 14:39
 *  description :
 */
class HomeFragment : BaseDataBindingFragment() , Handler.Callback{
    private lateinit var mBinding: FragmentHomeBinding

    private val df = DecimalFormat("#.##")

    private var isBind = false

    private var messenger: Messenger? = null
    private val mGetReplyMessenger = Messenger(Handler(requireActivity().mainLooper))

    /**
     * 定时任务
     */
    private var timerTask: TimerTask? = null
    private var timer: Timer? = null

    override fun onResume() {
        super.onResume()

        Calendar.DAY_OF_WEEK_IN_MONTH
        mBinding.apply {
            val animator = ValueAnimator.ofInt(0, 1024) // 从0增加到1000

            animator.duration = 1000

            animator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                tvNum.text = value.toString() // 在TextView中显示动画值
            }
            animator.start() // 开始动画
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = binding(inflater, R.layout.fragment_home, container)

        mBinding.apply {
            statusBarHeight = DimenUtils.getStatusBarHeight(requireActivity())
        }
        return mBinding.root
    }




    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    1
                )
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.ACTIVITY_RECOGNITION
                    )
                ) {
                    //此处需要弹窗通知用户去设置权限
                    Toast.makeText(requireActivity(), "请允许获取健身运动信息，不然不能为你计步哦~", Toast.LENGTH_SHORT).show()
                }
            } else {
                startStepService()
            }
        } else {
            startStepService()
        }
    }


    private fun startStepService() {
        /**
         * 这里判断当前设备是否支持计步
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(requireActivity())) {

        } else {

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    startStepService()
                } else {
                    Toast.makeText(requireActivity(), "请允许获取健身运动信息，不然不能为你计步哦~", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 开启计步服务
     */
    private fun setupService() {
        val intent = Intent(requireActivity(), StepService::class.java)
        isBind = requireActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            requireActivity().startForegroundService(intent)
        else
            requireActivity().startService(intent)
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    private val conn = object : ServiceConnection {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            timerTask = object : TimerTask() {
                override fun run() {
                    try {
                        messenger = Messenger(service)
                        val msg = Message.obtain(null, ConstantData.MSG_FROM_CLIENT)
                        msg.replyTo = mGetReplyMessenger
                        messenger!!.send(msg)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
            }
            timer = Timer()
            timer!!.schedule(timerTask, 0, 500)
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        override fun onServiceDisconnected(name: ComponentName) {

        }
    }

    /**
     * 设置记录数据
     */
    private fun setDatas() {

    }

    /**
     * 简易计算公里数，假设一步大约有0.7米
     *
     * @param steps 用户当前步数
     * @return
     */
    private fun countTotalKM(steps: Int): String {
        val totalMeters = steps * 0.7
        //保留两位有效数字
        return df.format(totalMeters / 1000)
    }

    /**
     * 获取全部运动历史纪录
     */
    private fun getRecordList() {

    }

    override fun handleMessage(msg: Message): Boolean {
        when (msg.what) {
            //这里用来获取到Service发来的数据
            ConstantData.MSG_FROM_SERVER ->
            {

            }
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        //记得解绑Service，不然多次绑定Service会异常
        if (isBind) requireActivity().unbindService(conn)
    }
}