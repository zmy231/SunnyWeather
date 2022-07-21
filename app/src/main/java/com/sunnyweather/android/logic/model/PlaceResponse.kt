package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 定义城市数据模型
 * */

data class PlaceResponse(val status: String, val places: List<Place>)

// 由于JSON中一些字段的命名可能与Kotlin的命名规范不太一致
// 因此这里使用了 @SerializedName 注解的方式，来让 JSON 字段和 Kotlin 字段之间建立映射关系
data class Place(val name: String, val location: Location,
                 @SerializedName("formatted_address") val address: String)

data class Location(val lat: String, val lng: String)

/**
 * 查询城市的数据信息
 * https://api.caiyunapp.com/v2/place?query=北京&token={token}&lang=zh_CN
 *
 * 查看具体的天气信息
 * https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/realtime.json
 *
 * 获取未来几天的天气信息
 * https://api.caiyunapp.com/v2.5/{token}/116.4073963,39.9041999/daily.json
 * */