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
class DrinkRepository : BaseRepository() {


    val drinkDao = AtomApp.appDatabase.drinkDao()

    fun queryDrinkEntityByDay() = drinkDao.queryDrinkEntityByDay(LocalDate.now())
    fun queryDrinkByDay() = drinkDao.queryDrinkByDay(LocalDate.now())

    suspend fun insertDrink(drink: DrinkEntity) = drinkDao.insertDrink(drink)

    suspend fun updateDrink(drink: DrinkEntity) = drinkDao.updateDrink(drink)

}