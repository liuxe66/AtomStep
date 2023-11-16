package com.atom.atomstep.data.repo

import com.atom.atomstep.app.AtomApp
import com.atom.atomstep.base.BaseRepository
import com.atom.atomstep.data.entity.StepEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 *  author : liuxe
 *  date : 2023/11/16 17:10
 *  description :
 */
class StepRepository:BaseRepository() {

    val stepDao = AtomApp.appDatabase.stepDao()

    /**
     * 查询今天步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepToday() = flow {
        stepDao.queryStepToday().collectLatest {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 查询本周步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurWeek() = flow {
        stepDao.queryStepCurWeek().collectLatest {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 查询本月步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurMonth() = flow {
        stepDao.queryStepCurMonth().collectLatest {
            emit(it)
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 插入
     * @param step StepEntity
     * @return Flow<String>
     */
    fun insertStep(step:StepEntity) = flow {
        stepDao.insertStep(step)
        emit("")
    }.flowOn(Dispatchers.IO)

    /**
     * 删除
     * @param step StepEntity
     * @return Flow<String>
     */
    fun deleteStep(step:StepEntity) = flow {
        stepDao.deleteStep(step)
        emit("")
    }.flowOn(Dispatchers.IO)

    /**
     * 更新
     * @param step StepEntity
     * @return Flow<String>
     */
    fun updateStep(step:StepEntity) = flow {
        stepDao.updateStep(step)
        emit("")
    }.flowOn(Dispatchers.IO)

}