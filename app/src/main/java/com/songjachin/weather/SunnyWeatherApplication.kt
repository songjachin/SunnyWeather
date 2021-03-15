package com.songjachin.weather

import android.app.Application
import android.content.Context

/**
 * Created by matthew
 *
 *
 */
class SunnyWeatherApplication : Application(){
    companion object{
        const val TOKEN = "YrqnAOejRAJPFG4j"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}