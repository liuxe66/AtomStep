package com.atom.atomstep.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.annotation.Nullable
import com.atom.atomstep.R
import com.atom.atomstep.app.AtomApp
import com.atom.atomstep.data.constant.ConstantData
import com.atom.atomstep.data.constant.ConstantData.Companion.ACTION_DATA
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.ui.MainActivity
import com.atom.atomstep.utils.LogUtils
import com.atom.atomstep.utils.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

/**
 * Created by fySpring
 * Date: 2020/4/22
 * To do:
 */
class StepService : Service(), SensorEventListener {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    //当前日期
    private var currentDate: String? = null

    //当前步数
    private var currentStep: Int = 0

    //传感器
    private var sensorManager: SensorManager? = null

    //计步传感器类型 0-counter 1-detector
    private var stepSensor = -1

    //广播接收
    private var mInfoReceiver: BroadcastReceiver? = null

    //是否有当天的记录
    private var hasRecord: Boolean = false

    //未记录之前的步数
    private var hasStepCount: Int = 0

    //下次记录之前的步数
    private var previousStepCount: Int = 0
    private var builder: Notification.Builder? = null

    private var notificationManager: NotificationManager? = null
    private var nfIntent: Intent? = null

    private val stepDao = AtomApp.appDatabase.stepDao()

    override fun onCreate() {
        super.onCreate()
        initBroadcastReceiver()
        initTodayData()
    }



    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        /**
         * 此处设将Service为前台，不然当APP结束以后很容易被GC给干掉，
         * 这也就是大多数音乐播放器会在状态栏设置一个原理大都是相通的
         */
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        builder = Notification.Builder(this.applicationContext, ConstantData.CHANNEL_ID)
        val notificationChannel =
            NotificationChannel(
                ConstantData.CHANNEL_ID,
                ConstantData.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN
            )
        notificationChannel.enableLights(false)//如果使用中的设备支持通知灯，则说明此通知通道是否应显示灯
        notificationChannel.setShowBadge(false)//是否显示角标
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        notificationManager?.createNotificationChannel(notificationChannel)
        builder?.setChannelId(ConstantData.CHANNEL_ID)

        /**
         * 设置点击通知栏打开的界面，此处需要注意了，
         * 如果你的计步界面不在主界面，则需要判断app是否已经启动，
         * 再来确定跳转页面，这里面太多坑
         */
        nfIntent = Intent(this, MainActivity::class.java)
        setStepBuilder()
        // 参数一：唯一的通知标识；参数二：通知消息。
        startForeground(ConstantData.NOTIFY_ID, builder?.build())// 开始前台服务

        return START_STICKY
    }


    /**
     * 初始化广播
     */
    private fun initBroadcastReceiver() {
        val filter = IntentFilter()
        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED)
        filter.addAction(Intent.ACTION_TIME_CHANGED)
        filter.addAction(Intent.ACTION_TIME_TICK)

        mInfoReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    //监听日期变化
                    Intent.ACTION_DATE_CHANGED, Intent.ACTION_TIME_CHANGED, Intent.ACTION_TIME_TICK -> {
                        saveStepData()
                        isNewDay()
                    }
                }
            }
        }
        //注册广播
        registerReceiver(mInfoReceiver, filter)
    }

    /**
     * 初始化当天数据
     */
    private fun initTodayData() {

        coroutineScope.launch {
            val stepEntity = stepDao.queryStepCurrent(LocalDate.now())
            LogUtils.e("stepEntity:$stepEntity")
            currentStep =
                if (stepEntity == null) 0 else Integer.parseInt(stepEntity.step.toString())
            getStepDetector()
            sendDataToActivity(currentStep)
        }


    }

    /**
     * 监听晚上0点变化初始化数据
     */
    private fun isNewDay() {
        val time = "00:00"
        if (time == SimpleDateFormat("HH:mm").format(Date()) || currentDate != TimeUtil.getCurrentDate()) {
            initTodayData()
        }
    }

    /**
     * 获取传感器实例
     */
    private fun getStepDetector() {
        if (sensorManager != null) {
            sensorManager = null
        }
        // 获取传感器管理器的实例
        sensorManager = this
            .getSystemService(Context.SENSOR_SERVICE) as SensorManager

        addCountStepListener()
    }

    /**
     * 添加传感器监听
     */
    private fun addCountStepListener() {
        val countSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val detectorSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (countSensor != null) {
            stepSensor = 0
            sensorManager!!.registerListener(
                this@StepService,
                countSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else if (detectorSensor != null) {
            stepSensor = 1
            sensorManager!!.registerListener(
                this@StepService,
                detectorSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        LogUtils.e("======stepSensor=======$stepSensor")
    }

    /**
     * 由传感器记录当前用户运动步数，注意：该传感器只在4.4及以后才有，并且该传感器记录的数据是从设备开机以后不断累加，
     * 只有当用户关机以后，该数据才会清空，所以需要做数据保护
     */
    override fun onSensorChanged(event: SensorEvent) {

        if (stepSensor == 0) {
            val tempStep = event.values[0].toInt()
            if (!hasRecord) {
                hasRecord = true
                hasStepCount = tempStep
            } else {
                val thisStepCount = tempStep - hasStepCount
                currentStep += thisStepCount - previousStepCount
                previousStepCount = thisStepCount
                saveStepData()
            }

        } else if (stepSensor == 1) {
            if (event.values[0].toDouble() == 1.0) {
                currentStep++
                saveStepData()
            }
        }

        LogUtils.e("======onSensorChanged=======$currentStep")
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }


    /**
     * 保存当天的数据到数据库中，并去刷新通知栏
     */
    private fun saveStepData() {
        coroutineScope.launch {

            LogUtils.e("===saveStepData====")
            var data = stepDao.queryStepCurrent(LocalDate.now())
            if (data == null){
                data = StepEntity()
                stepDao.insertStep(data)
            } else {
                data.step = currentStep
                stepDao.updateStep(data)
            }
        }
        setStepBuilder()
        sendDataToActivity(currentStep)
    }

    private fun setStepBuilder() {
        builder?.setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                nfIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        ) // 设置PendingIntent
            ?.setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable.ic_launcher
                )
            )
            ?.setContentTitle("今日步数" + currentStep + "步")
            ?.setSmallIcon(R.drawable.ic_launcher)
            ?.setContentText("健康，每一步都值得")
        // 获取构建好的Notification
        val stepNotification = builder?.build()
        //调用更新
        notificationManager?.notify(ConstantData.NOTIFY_ID, stepNotification)
    }

    private fun sendDataToActivity(data: Int) {
        val intent = Intent(ACTION_DATA)
        intent.putExtra("data", data)
        sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        //主界面中需要手动调用stop方法service才会结束
        stopForeground(true)
        unregisterReceiver(mInfoReceiver)
    }
}