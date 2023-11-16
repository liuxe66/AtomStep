package com.atom.atomstep.utils

import android.util.Log

/**
 *  author : liuxe
 *  date : 2021/7/12 2:56 下午
 *  description :
 */
object LogUtils {

    fun d(msg: String) {
        Log.d("atom",msg)
    }

    fun w(msg: String) {
        Log.w("atom",msg)
    }

    fun e(msg: String) {
        Log.e("atom",msg)
    }

}