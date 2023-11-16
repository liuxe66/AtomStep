package com.atom.atomstep.binding

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter

/**
 *  author : liuxe
 *  date : 4/28/21 12:59 PM
 *  description :
 */
@BindingAdapter("bindStatusBarHeight")
fun bindStatusBarHeignt(view:View,statusBarHeight:Int){
    var layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = statusBarHeight
    view.layoutParams = layoutParams;
}