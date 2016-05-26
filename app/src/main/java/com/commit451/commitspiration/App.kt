package com.commit451.commitspiration

import android.app.Application

import timber.log.Timber

/**
 * App!
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
