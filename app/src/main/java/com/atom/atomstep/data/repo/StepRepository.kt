package com.atom.atomstep.data.repo

import com.atom.atomstep.app.AtomApp
import com.atom.atomstep.base.BaseRepository
import com.atom.atomstep.data.entity.DrinkEntity
import com.atom.atomstep.data.entity.StepEntity
import java.time.LocalDate
import java.time.LocalDateTime

/**
 *  author : liuxe
 *  date : 2023/11/16 17:10
 *  description :
 */
class StepRepository : BaseRepository() {

    val stepDao = AtomApp.appDatabase.stepDao()
    val drinkDao = AtomApp.appDatabase.drinkDao()

    /**
     * 查询今天步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepToday() = stepDao.queryStepByDay(LocalDate.now())

    /**
     * 查询今天步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepByDay(day:LocalDate) = stepDao.queryStepByDay(day)

    /**
     * 查询本周步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurWeek(startOfWeek: LocalDate, endOfWeek: LocalDate) =
        stepDao.queryStepCurWeek(startOfWeek, endOfWeek)

    /**
     * 查询本月步数
     * @return Flow<List<StepEntity>>
     */
    fun queryStepCurMonth(startOfMonth: LocalDate, endOfMonth: LocalDate) =
        stepDao.queryStepCurMonth(startOfMonth, endOfMonth)

    /**
     * 查询所有 倒序
     * @return Flow<List<StepEntity>>
     */
    fun queryStepAll() = stepDao.queryStepAll()

    /**
     * 插入
     * @param step StepEntity
     * @return Flow<String>
     */
    suspend fun insertStep(step: StepEntity) = stepDao.insertStep(step)

    suspend fun updateStep(step: StepEntity) = stepDao.updateStep(step)



    fun queryDrinkByDay() = drinkDao.queryDrinkByDay(LocalDate.now())

    suspend fun insertDrink(drink: DrinkEntity) = drinkDao.insertDrink(drink)

    suspend fun updateDrink(drink: DrinkEntity) = drinkDao.updateDrink(drink)

}