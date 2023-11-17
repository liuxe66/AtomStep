package com.atom.atomstep.data.dao

import androidx.room.*
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
interface StepDao {

    /**
     * 根据日期查询
     * @param day String
     * @return Flow<List<StepEntity>>
     */
    @Query("SELECT * FROM step_table WHERE date = :date")
    fun queryStepByDay(date:LocalDate):  Flow<StepEntity?>

    /**
     * 根据日期查询
     * @param day String
     * @return Flow<List<StepEntity>>
     */
    @Query("SELECT * FROM step_table WHERE date = :date")
    fun queryStepCurrent(date:LocalDate): StepEntity?

    /**
     * 查询本周
     * @return Flow<List<TransRcordEntity>>
     */
    @Query("SELECT * FROM step_table WHERE date BETWEEN :startOfWeek AND :endOfWeek")
    fun queryStepCurWeek(startOfWeek: LocalDate, endOfWeek: LocalDate): Flow<List<StepEntity>>

    /**
     * 查询当月
     * @return Flow<List<TransRcordEntity>>
     */
    @Query("SELECT * FROM step_table WHERE date >= :startOfMonth AND date <= :endOfMonth")
    fun queryStepCurMonth(startOfMonth: LocalDate, endOfMonth: LocalDate): Flow<List<StepEntity>>

    /**
     * 查询所有
     */
    @Query("SELECT * FROM step_table ORDER BY date DESC")
    fun queryStepAll(): Flow<List<StepEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStep(step: StepEntity)

    @Delete
    suspend fun deleteStep(step: StepEntity)

    @Update
    suspend fun updateStep(step: StepEntity)

}