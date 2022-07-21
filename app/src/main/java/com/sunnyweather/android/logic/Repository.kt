package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers

/**
 * 作为仓库层的统一封装入口
 * */
object Repository {
    /**
     * 返回一个 LiveData 对象：将异步获取的数据以响应式编程的方式通知给上一层
     *
     * 上述代码中的 liveData() 函数是 lifecycle-livedata-ktx 库提供的功能
     * 它可以自动构建并返回一个 LiveData 对象
     * 在它的代码块中提供一个挂起函数的上下文，这样我们就可以在 liveData() 函数的代码块中调用任意的挂起函数了
     *
     * 将 liveData() 函数的线程参数类型指定成了 Dispatchers.IO，这样代码块中的所有代码就都运行在子线程中了
     * Android 不允许在主线程中进行网络请求的，诸如读写数据库之类的本地数据操作也是不建议在主线程中进行的，因此在仓库层进行一次线程转换
     * */
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {

            // 调用了 searchPlaces() 函数来搜索城市数据
            // 如果服务器响应的状态是 ok，那么就使用 Kotlin 内置的 Result.success() 方法来包装获取的城市数据列表
            // 否则使用 Result.failure() 方法来包装一个异常信息
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }

        // 使用 emit() 方法将包装的结果发射出去，这个 emit() 方法其实类似于调用 LiveData 的 setValue()方法来通知数据变化
        // 只不过这里我们无法直接取得返回的 LiveData 对象，所以 lifecycle-livedata-ktx 库提供了这样一个替代方法
        emit(result)
    }
}