package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        // 彩云天气开发者平台的令牌值
        const val TOKEN = "yMApfziAhGE3O40v"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}