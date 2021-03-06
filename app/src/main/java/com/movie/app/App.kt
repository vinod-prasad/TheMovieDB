package com.movie.app

import android.app.Application
import timber.log.Timber

class App : Application(){

    override fun onCreate() {
        super.onCreate()

        //Logger
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}