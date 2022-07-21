package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 创建 Retrofit 构建器
 * */
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * create() 方法一
     * 获取对象方法：
     *  val appService = ServiceCreator.create(AppService::class.java)
     * */
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**
     * create() 方法二
     * 获取对象方法：
     *  val appService = ServiceCreator.create<AppService>()
     * */
    inline fun <reified T> create(): T = create(T::class.java)
}