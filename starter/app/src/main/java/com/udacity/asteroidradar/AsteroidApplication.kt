package com.udacity.asteroidradar

import android.app.Application
import timber.log.Timber
import com.udacity.asteroidradar.work.setupWorkers

class AsteroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        setupWorkers(applicationContext)
    }
}