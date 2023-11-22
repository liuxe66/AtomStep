package com.atom.atomstep.ui.home

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.atom.atomstep.R
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.databinding.ItemStepWeekBinding
import com.atom.atomstep.ext.gone
import com.atom.atomstep.ext.setColor
import com.atom.atomstep.ext.visible
import com.chad.library.adapter.base.BaseQuickAdapter
import java.time.DayOfWeek
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2023/11/21 13:53
 *  description :
 */
class StepWeekAdapter : BaseQuickAdapter<StepEntity, StepWeekAdapter.VH>() {
    class VH(
        parent: ViewGroup,
        val binding: ItemStepWeekBinding = ItemStepWeekBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: StepEntity?) {
        holder.binding.apply {
            item?.apply {

                val chineseDayOfWeek = when (item.date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> "日"
                    DayOfWeek.MONDAY -> "一"
                    DayOfWeek.TUESDAY -> "二"
                    DayOfWeek.WEDNESDAY -> "三"
                    DayOfWeek.THURSDAY -> "四"
                    DayOfWeek.FRIDAY -> "五"
                    DayOfWeek.SATURDAY -> "六"
                }


                tvWeek.text = chineseDayOfWeek
                tvDate.text = item.date.dayOfMonth.toString()

                if (LocalDate.now() == item.date){
                    tvWeek.setColor(R.color.green_1)
                    tvDate.setColor(R.color.green_1)
                    tvDate.text = "今"
                } else if (item.date > LocalDate.now()){
                    tvWeek.setColor(R.color.color_text_sub)
                    tvDate.setColor(R.color.color_text_sub)
                    tvDate.text = item.date.dayOfMonth.toString()
                } else {
                    tvWeek.setColor(R.color.color_text_sub)
                    tvDate.setColor(R.color.color_text_title)
                    tvDate.text = item.date.dayOfMonth.toString()
                }

                progressWeek.setProgress(item.step.toFloat())// 当前进度
                    .setMaxProgress(6000)// 进度条的最大值

                if (item.step >= 6000) {
                    val animator: ObjectAnimator =
                        ObjectAnimator.ofFloat(ivComplete, "alpha", 0f, 1f)
                    animator.duration = 300
                    animator.start()
                    ivComplete.visible()
                } else {
                    ivComplete.gone()
                }

            }
        }
    }



    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}