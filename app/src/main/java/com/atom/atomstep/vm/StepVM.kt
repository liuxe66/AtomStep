package com.atom.atomstep.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.atom.atomstep.base.BaseViewModel
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.data.repo.StepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2023/11/16 17:27
 *  description :
 */
class StepVM : BaseViewModel() {
    private val repository = StepRepository()

    val stepCurWeekData = MutableLiveData<List<StepEntity>>()

    /**
     * 查询今天步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepToday() = liveData {
        repository.queryStepToday().collectLatest {
            emit(it)
        }
    }

    /**
     * 查询指定日期
     * @param day LocalDate
     * @return LiveData<StepEntity?>
     */
    fun queryStepByDay(day: LocalDate) = liveData{
        repository.queryStepByDay(day).collectLatest {
            emit(it)
        }
    }

    /**
     * 查询所有 倒序
     * @return LiveData<List<StepEntity>>
     */
    fun queryStepAll() = liveData {
        repository.queryStepAll().collectLatest {
            emit(it)
        }
    }

    /**
     * 查询本周步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurWeek() = liveData{
        val now = LocalDate.now()
        val startOfWeek = now.minusDays(now.dayOfWeek.value - 1L)
        val endOfWeek = startOfWeek.plusDays(6)

        repository.queryStepCurWeek(startOfWeek, endOfWeek).collectLatest {

            val dateList = it

            val resultList = mutableListOf<StepEntity>()

            for (day in 0L until 7L) {
                val dataForDay = dateList.filter { step ->
                    step.date == startOfWeek.plusDays(day)
                }
                if (dataForDay.isEmpty()) {
                    val defaultValue = StepEntity()
                    defaultValue.date = startOfWeek.plusDays(day)
                    resultList.add(defaultValue) // 如果没有数据，添加默认值
                } else {
                    resultList.addAll(dataForDay) // 如果有数据，添加数据
                }
            }
            emit(resultList)
        }
    }

    /**
     * 查询本月步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurMonth() = liveData(Dispatchers.IO) {
        val now = LocalDate.now()
        val startOfMonth = now.withDayOfMonth(1)
        val endOfMonth = now.withDayOfMonth(now.lengthOfMonth())

        repository.queryStepCurMonth(startOfMonth, endOfMonth).collectLatest {
            emit(it)
        }
    }

    /**
     * 插入
     * @param step StepEntity
     * @return Flow<String>
     */
    fun insertStep(step: StepEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertStep(step)
    }

    /**
     * 更新
     * @param step StepEntity
     * @return Flow<String>
     */
    fun updateStep(step: StepEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateStep(step)
    }
}