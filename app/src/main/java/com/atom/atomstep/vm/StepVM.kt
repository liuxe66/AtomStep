package com.atom.atomstep.vm

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.atom.atomstep.base.BaseViewModel
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.data.repo.StepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2023/11/16 17:27
 *  description :
 */
class StepVM : BaseViewModel() {
    private val repository = StepRepository()

    /**
     * 查询今天步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepToday() = liveData(Dispatchers.IO) {
        repository.queryStepToday().collectLatest {
            emit(it)
        }
    }

    /**
     * 查询指定日期
     * @param day LocalDate
     * @return LiveData<StepEntity?>
     */
    fun queryStepByDay(day:LocalDate) = liveData(Dispatchers.IO) {
        repository.queryStepByDay(day).collectLatest {
            emit(it)
        }
    }

    /**
     * 查询所有 倒序
     * @return LiveData<List<StepEntity>>
     */
    fun queryStepAll() = liveData(Dispatchers.IO) {
        repository.queryStepAll().collectLatest {
            emit(it)
        }
    }

    /**
     * 查询本周步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurWeek() = liveData(Dispatchers.IO) {
        val now = LocalDate.now()
        val startOfWeek = now.minusDays(now.dayOfWeek.value - 1L)
        val endOfWeek = startOfWeek.plusDays(6)

        repository.queryStepCurWeek(startOfWeek,endOfWeek).collectLatest {
            emit(it)
        }
    }

    /**
     * 查询本月步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurMonth()  = liveData(Dispatchers.IO) {
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
    fun insertStep(step: StepEntity) = viewModelScope.launch {
        repository.insertStep(step)
    }

    /**
     * 删除
     * @param step StepEntity
     * @return Flow<String>
     */
    fun deleteStep(step: StepEntity) = viewModelScope.launch {
        repository.deleteStep(step)
    }

    /**
     * 更新
     * @param step StepEntity
     * @return Flow<String>
     */
    fun updateStep(step: StepEntity) = viewModelScope.launch {
        repository.updateStep(step)
    }
}