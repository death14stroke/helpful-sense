package com.andruid.magic.helpfulsense.application

import android.app.Application
import com.andruid.magic.helpfulsense.BuildConfig
import com.andruid.magic.helpfulsense.database.DbRepository
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())
        DbRepository.init(this)
    }
}