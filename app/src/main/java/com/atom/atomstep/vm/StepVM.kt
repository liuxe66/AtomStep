package com.atom.atomstep.vm

import com.atom.atomstep.base.BaseViewModel
import com.atom.atomstep.data.entity.StepEntity
import com.atom.atomstep.data.repo.StepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 *  author : liuxe
 *  date : 2023/11/16 17:27
 *  description :
 */
class StepVM:BaseViewModel() {
    private val repository = StepRepository()

    /**
     * 查询今天步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepToday() = flow {
        repository.queryStepToday().collectLatest {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 查询本周步数
     * @return Flow<List<StepEntity>>
     */
    fun getStepCurWeek() = flow {
        repository.queryStepCurWeek().collectLatest {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 查询本月步数
     * @return Flow<List<StepEntity>>
     */
    fun getStepCurMonth() = flow {
        repository.queryStepCurMonth().collectLatest {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 插入
     * @param step StepEntity
     * @return Flow<String>
     */
    fun insertStep(step: StepEntity) = flow {
        repository.insertStep(step)
        emit("")
    }.flowOn(Dispatchers.IO)

    /**
     * 删除
     * @param step StepEntity
     * @return Flow<String>
     */
    fun deleteStep(step: StepEntity) = flow {
        repository.deleteStep(step)
        emit("")
    }.flowOn(Dispatchers.IO)

    /**
     * 更新
     * @param step StepEntity
     * @return Flow<String>
     */
    fun updateStep(step: StepEntity) = flow {
        repository.updateStep(step)
        emit("")
    }.flowOn(Dispatchers.IO)
}