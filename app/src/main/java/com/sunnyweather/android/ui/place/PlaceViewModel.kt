package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel : ViewModel() {

    private val searchLivaData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    /**
     * 定义了一个 searchPlaces() 方法，但是没有直接调用仓库层中的bsearchPlaces()b方法
     * 而是将传入的搜索参数 query 赋值给了一个 searchLiveData 对象
     * 并使用 Transformations 的 switchMap() 方法来观察这个对象
     * 否则仓库层返回的 LiveData 对象将无法进行观察
     * */
    fun searchPlaces(query: String) {
        searchLivaData.value = query
    }

    /**
     * 当 searchPlaces() 函数被调用时， switchMap() 方法所对应的转换函数就会执行
     * 在转换函数中，只需要调用仓库层中定义的 searchPlaces() 方法就可以发起网络请求
     * 同时将仓库层返回的 LiveData 对象转换成一个可供 Activity 观察的 LiveData 对象
     * */
    val placeLiveData = Transformations.switchMap(searchLivaData) { query ->
        Repository.searchPlaces(query)
    }
}