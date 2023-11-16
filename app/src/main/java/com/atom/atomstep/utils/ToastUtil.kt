package com.atom.atomstep.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.atom.atomstep.app.AtomApp

/**
 * author : liuxe
 * date : 2021/8/20 1:40 下午
 * description :
 */
object ToastUtil {
    private var toast: Toast? = null
    var mHandler = Handler(Looper.getMainLooper())
    @JvmOverloads
    fun showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(text, duration)
        } else {
            mHandler.post { show(text, duration) }
        }
    }

    private fun show(text: String, duration: Int) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(AtomApp.context, text, duration)
        toast?.show()
    }
}