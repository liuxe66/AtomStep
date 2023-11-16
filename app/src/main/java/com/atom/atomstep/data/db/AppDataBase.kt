package com.atom.atomstep.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.atom.atomstep.data.dao.StepDao
import com.atom.atomstep.data.entity.StepEntity

/**
 *  author : liuxe
 *  date : 2023/3/22 16:13
 *  description :
 */
@Database(
    entities = [StepEntity::class], version = 1, exportSchema = false
)
@TypeConverters(value = [LocalTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun stepDao(): StepDao

    companion object {

        @JvmStatic
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "atomstep.db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}