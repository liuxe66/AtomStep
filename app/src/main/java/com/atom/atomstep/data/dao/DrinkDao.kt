package com.atom.atomstep.data.dao

import androidx.room.*
import com.atom.atomstep.data.entity.DrinkEntity
import com.atom.atomstep.data.entity.StepEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 *  author : liuxe
 *  date : 2023/5/5 09:55
 *  description :
 */
@Dao
interface DrinkDao {

    /**
     * 根据日期查询
     * @param day String
     * @return Flow<List<StepEntity>>
     */
    @Query("SELECT * FROM drink_table WHERE date = :date")
    fun queryDrinkByDay(date: LocalDate): Flow<DrinkEntity?>

    @Query("SELECT * FROM drink_table WHERE date = :date")
    fun queryDrinkEntityByDay(date: LocalDate): DrinkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrink(step: DrinkEntity)

    @Update
    suspend fun updateDrink(step: DrinkEntity)

}