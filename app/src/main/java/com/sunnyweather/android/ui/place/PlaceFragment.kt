package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.R
import kotlinx.android.synthetic.main.fragment_place.*

class PlaceFragment : Fragment() {

    /**
     * 使用了 lazy 函数这种懒加载技术来获取 PlaceViewModel 的实例
     * 允许我们在整个类中随时使用 viewModel 这个变量，而完全不用关心它何时初始化、是否为空等前提条件
     * */
    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

    /**
     * 在 onCreateView() 方法中加载了前面编写的 fragment_place 布局
     * */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    /**
     * onActivityCreated()方法
     * 先给 RecyclerView 设置了 LayoutManager 和适配器，并使用 PlaceViewModel 中的 placeList 集合作为数据源
     * 调用了 EditText 的 addTextChangedListener() 方法来监听搜索框内容的变化情况
     * 每当搜索框中的内容发生了变化，我们就获取新的内容，然后传递给 PlaceViewModel 的 searchPlaces() 方法，这样就可以发起搜索城市数据的网络请求了
     * 当输入搜索框中的内容为空时，就将 RecyclerView 隐藏起来，同时将背景图显示出来
     *
     * 解决了搜索城市数据请求的发起，还要能获取到服务器响应的数据才行，需要借助 LiveData 来完成了
     * 对 PlaceViewModel 中的 placeLiveData 对象进行观察，当有任何数据变化时，就会回调到传入的 Observer 接口实现中
     * 然后对回调的数据进行判断：如果数据不为空，那么就将这些数据添加到 PlaceViewModel 的 placeList 集合中，并通知 PlaceAdapter 刷新界面
     * 如果数据为空，则说明发生了异常，此时弹出一个 Toast 提示，并将具体的异常原因打印出来
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 给 RecyclerView 设置了 LayoutManager 和适配器，并使用 PlaceViewModel 中的 placeList 集合作为数据源
        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter

        // 调用了 EditText 的 addTextChangedListener() 方法来监听搜索框内容的变化情况
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()

            // 当搜索框中的内容发生了变化，获取新的内容，传递给 PlaceViewModel 的 searchPlaces() 方法，发起搜索城市数据的网络请求
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            }

            // 当输入搜索框中的内容为空时，将 RecyclerView 隐藏起来，同时将背景图显示出来
            else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        // 对 PlaceViewModel 中的 placeLiveData 对象进行观察，当有任何数据变化时，就会回调到传入的 Observer 接口实现中
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer{ result ->

            // 对回调的数据进行判断
            val places = result.getOrNull()

            // 如果数据不为空，将数据添加到 PlaceViewModel 的 placeList 集合中，并通知 PlaceAdapter 刷新界面
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }

            // 如果数据为空，弹出一个 Toast 提示，打印具体的异常原因
            else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}