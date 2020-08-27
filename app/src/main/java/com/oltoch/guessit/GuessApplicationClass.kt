package com.oltoch.guessit

import android.app.Application
import timber.log.Timber

class GuessApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
