package com.songjachin.weather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.songjachin.weather.R
import com.songjachin.weather.logic.model.Place
import com.songjachin.weather.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * Created by matthew
 *
 *
 */
class PlaceAdapter(private val fragment: PlaceFragment): RecyclerView.Adapter<PlaceAdapter.InnerHolder>() {
    inner class InnerHolder(view: View) :RecyclerView.ViewHolder(view){
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    private val placeList  = ArrayList<Place>()
    fun addData(places: List<Place>) {
       placeList.clear()
       placeList.addAll(places)
       notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item,parent,false)
        return InnerHolder(view)
    }

    override fun getItemCount(): Int {
         return placeList.size
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        //子项设置
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address

        //子项监听，点击则保存place，转到weatherActivity中
        holder.itemView.setOnClickListener {
            val activity = fragment.activity
            fragment.viewModel.savePlace(place)
            if (activity is WeatherActivity) {
                activity.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.address
                activity.refreshWeather()
            } else {
                val intent = Intent(holder.itemView.context, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.address)
                }
                fragment.startActivity(intent)
                activity?.finish()
            }
        }

    }
}