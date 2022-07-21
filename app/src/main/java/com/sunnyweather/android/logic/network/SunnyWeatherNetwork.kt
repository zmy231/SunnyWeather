package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 定义统一的网络数据源访问入口，对所有网络请求的 API 进行封装
 * */
object SunnyWeatherNetwork {

    // 用ServiceCreator创建了一个PlaceService接口的动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    // 定义了一个 searchPlaces() 函数，在这里调用刚刚在 PlaceService 接口中定义的 searchPlaces() 方法，以发起搜索城市数据请求
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    // 使用 11.7.3 小节中学习的技巧来简化 Retrofit 回调的写法
    // 由于是需要借助协程技术来实现的，因此这里又定义了一个 await() 函数，并将 searchPlaces() 函数也声明成挂起函数
    // 至于 await() 函数的实现，之前在 11.7.3 小节就解析过
    private suspend fun <T> Call<T>.await() : T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}