package com.atom.atomstep.utils

import java.math.BigDecimal

/**
 * author : liuxe
 * date : 2023/7/10 16:35
 * description :
 */
object DoubleUtils {
    /**
     * 对double数据进行取精度.
     * @param value  double数据.
     * @param scale  精度位数(保留的小数位数).
     * @return 精度计算后的数据.
     */
    fun round(value: Double, scale: Int = 2): Double {
        var bd = BigDecimal.valueOf(value)
        bd = bd!!.setScale(scale)
        val d = bd.toDouble()
        return d
    }

    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    fun sum(d1: Double, d2: Double): Double {
        val bd1 = BigDecimal.valueOf(d1)
        val bd2 = BigDecimal.valueOf(d2)
        return bd1.add(bd2).toDouble()
    }

    /**
     * double 相减
     * @param d1
     * @param d2
     * @return
     */
    fun sub(d1: Double, d2: Double): Double {
        val bd1 = BigDecimal.valueOf(d1)
        val bd2 = BigDecimal.valueOf(d2)
        return bd1.subtract(bd2).toDouble()
    }

    /**
     * double 乘法
     * @param d1
     * @param d2
     * @return
     */
    fun mul(d1: Double, d2: Double): Double {
        val bd1 = BigDecimal.valueOf(d1)
        val bd2 = BigDecimal.valueOf(d2)
        return bd1.multiply(bd2).toDouble()
    }

    /**
     * double 除法
     * @param d1
     * @param d2
     * @param scale 四舍五入 小数点位数
     * @return
     */
    fun div(d1: Double, d2: Double, scale: Int): Double {
        val bd1 = BigDecimal.valueOf(d1)
        val bd2 = BigDecimal.valueOf(d2)
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).toDouble()
    }

    fun formatDouble(num: Double): String {
        return String.format("%.2f", num)
    }
}