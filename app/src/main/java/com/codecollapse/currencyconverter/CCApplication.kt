package com.codecollapse.currencyconverter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class CCApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setTimberConfiguration()
    }

    protected open fun setTimberConfiguration() {
        Timber.plant(Timber.DebugTree())
    }
}