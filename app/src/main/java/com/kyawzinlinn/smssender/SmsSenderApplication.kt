package com.kyawzinlinn.smssender

import android.app.Application
import com.kyawzinlinn.smssender.di.AppContainer
import com.kyawzinlinn.smssender.di.AppDataContainer

class SmsSenderApplication : Application () {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        container = AppDataContainer(this)
    }
}