package com.atom.atomstep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

/**
 *  author : liuxe
 *  date : 2023/11/15 15:11
 *  description :
 */
@Entity(tableName = "step_table")
data class StepEntity(
    @PrimaryKey(autoGenerate = true) val stepId: Int = 1, val timestamp: LocalDate, val step: Int
)
