package com.atom.atomstep.data.dao

import androidx.room.*
import com.atom.atomstep.data.entity.StepEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 *  author : liuxe
 *  date : 2023/5/5 09:55
 *  description :
 */
@Dao
interface StepDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStep(step: StepEntity)

    /**
     * 查询今天
     * @return Flow<List<TransRcordEntity>>
     */
    @Query(
        "SELECT * FROM step_table WHERE timestamp >= date('now', 'localtime', 'start of day') AND timestamp< date('now', 'localtime', 'start of day', '+1 day')"
    )
    fun queryStepToday(): Flow<List<StepEntity>>

    /**
     * 根据日期查询
     * @param day String
     * @return Flow<List<StepEntity>>
     */
    @Query(
        "SELECT * FROM step_table\n" + "WHERE strftime('%Y-%m-%d', timestamp) = :day"
    )
    fun queryStepByDay(day: String): Flow<List<StepEntity>>

    /**
     * 查询本周
     * @return Flow<List<TransRcordEntity>>
     */
    @Query("SELECT * FROM step_table WHERE strftime('%W', timestamp / 1000, 'unixepoch', 'localtime') = strftime('%W', 'now', 'localtime')")
    fun queryStepCurWeek(): Flow<List<StepEntity>>

    /**
     * 查询当月
     * @return Flow<List<TransRcordEntity>>
     */
    @Query("SELECT * FROM step_table WHERE strftime('%m', timestamp / 1000, 'unixepoch', 'localtime') = strftime('%m', 'now', 'localtime')")
    fun queryStepCurMonth(): Flow<List<StepEntity>>

    /**
     * 查询所有
     */
    @Query("SELECT * FROM step_table ORDER BY timestamp DESC")
    fun queryStepAll(): Flow<List<StepEntity>>

    @Delete
    fun deleteStep(step: StepEntity)

    @Update
    fun updateStep(step: StepEntity)

}