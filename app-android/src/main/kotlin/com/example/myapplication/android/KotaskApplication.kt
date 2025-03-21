package com.example.myapplication.android

import android.app.Application
import io.amirhparhizgar.kotask.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KotaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@KotaskApplication)
            modules(AppModule)
        }
    }
}
