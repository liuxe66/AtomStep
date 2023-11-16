package com.atom.atomstep.app

import android.app.Application
import android.content.Context
import com.atom.atomstep.data.db.AppDatabase
import kotlin.properties.Delegates


/**
 *  author : liuxe
 *  date : 5/17/21 2:28 PM
 *  description :
 */
class AtomApp : Application() {
    companion object {
        var context: Context by Delegates.notNull()
        var appDatabase: AppDatabase by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        appDatabase = AppDatabase.getDatabase(this)
    }
}