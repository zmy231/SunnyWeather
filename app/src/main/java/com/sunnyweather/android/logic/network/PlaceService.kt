package com.sunnyweather.android.logic.network

import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 定义一个用于访问彩云天气城市搜索 API 的 Retrofit 接口
 * */
interface PlaceService {
    // 搜索城市数据的 API 中只有 query 这个参数是需要动态指定的，使用 @Query 注解的方式来进行实现
    // 另外两个参数是不会变的，因此固定写在 @GET 注解中即可
    // https://api.caiyunapp.com/v2/place?query=北京&token={token}&lang=zh_CN
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}