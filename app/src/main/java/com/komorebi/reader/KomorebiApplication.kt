package com.komorebi.reader

import android.app.Application
import androidx.room.Room
import com.komorebi.reader.data.local.database.KomorebiDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KomorebiApplication : Application() {

    private var database: KomorebiDatabase? = null

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            KomorebiDatabase::class.java,
            "komorebi_database"
        ).build()
    }

    fun getDatabase(): KomorebiDatabase = database!!
}