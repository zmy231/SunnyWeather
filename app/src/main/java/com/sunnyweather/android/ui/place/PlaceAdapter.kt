package com.sunnyweather.android.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.model.Place


/**
 * 为 RecyclerView 准备适配器了，让这个适配器继承自 RecyclerView.Adapter，并将泛型指定为 PlaceAdapter.ViewHolder
 * */
class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    /**
     * 定义了一个内部类 ViewHolder，它要继承自 RecyclerView.ViewHolder
     * 然后 ViewHolder 的主构造函数中要传入一个 View 参数，这个参数通常就是 RecyclerView 子项的最外层布局
     * 那么我们就可以通过 findViewById() 方法来获取布局中 TextView 的实例了
     * */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    /**
     * 由于 PlaceAdapter 是继承自 RecyclerView.Adapter 的，那么就必须重写 onCreateViewHolder()、onBindViewHolder() 和 getItemCount() 这3个方法
     */

    /**
     * onCreateViewHolder() 方法是用于创建 ViewHolder 实例的，在这个方法中将 place_item 布局加载进来
     * 然后创建一个 ViewHolder 实例，并把加载出来的布局传入构造函数当中，最后将 ViewHolder 的实例返回
     * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * onBindViewHolder() 方法用于对 RecyclerView 子项的数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
     * 通过 position 参数得到当前项的 place 实例，然后再将数据设置到 ViewHolder 的 TextView 当中即可
     * */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    /**
     * getItemCount() 方法用于告诉 RecyclerView 一共有多少子项，直接返回数据源的长度
     * */
    override fun getItemCount() = placeList.size
}