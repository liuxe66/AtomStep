package com.atom.atomstep.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 *  author : liuxe
 *  date : 2023/11/15 15:11
 *  description :
 */
@Entity(tableName = "drink_table")
data class DrinkEntity(
    @PrimaryKey(autoGenerate = true)
    val drinkId: Int = 0,
    var date: LocalDate = LocalDate.now(),
    var drinkNum: Int = 0
)
