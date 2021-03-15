package com.songjachin.weather.ui.place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.songjachin.weather.MainActivity
import com.songjachin.weather.R
import com.songjachin.weather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

/**
 * Created by matthew
 *
 *
 */
class PlaceFragment : Fragment() {
    //懒加载,委托？
    val viewModel by lazy{ViewModelProvider(this)[PlaceViewModel::class.java]}
    //延迟加载
    private lateinit var adapter: PlaceAdapter
    private  val TAG = "PlaceFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place, container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        judge()
        initView()
        initListener()
        observeData()
    }

    private fun observeData() {
        //搜索返回的数据的观察，不空则设置rv
        //result: LiveData<List<Place>> places: List<Place>
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            val places = result.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                adapter.addData(places)
                //Log.e(TAG, "onActivityCreated: ", )
            } else {
                Toast.makeText(activity, "can't find the target position", Toast.LENGTH_SHORT)
                    .show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun initListener() {
        //btn的监听
        btn_search.setOnClickListener {
            val content = searchPlaceEdit.editableText.toString()
            Log.d(TAG, "onActivityCreated: $content")
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        //搜索框的监听
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isEmpty()) {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initView() {
        //rv的设置
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        adapter = PlaceAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun judge() {
        //如果是MainActivity且已保存地址，跳转到weatherActivity
        if(activity is MainActivity && viewModel.isPlaceSaved()){
            val place = viewModel.getSavePlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng",place.location.lng)
                putExtra("location_lat",place.location.lat)
                putExtra("place_name",place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
    }
}

