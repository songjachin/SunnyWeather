package com.songjachin.weather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.songjachin.weather.logic.Repository
import com.songjachin.weather.logic.model.Location

/**
 * Created by matthew
 *
 *
 */
class WeatherViewModel : ViewModel(){
    //Location==lng+lat
    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""
    //同理
    val weatherLiveData = Transformations.switchMap(locationLiveData){
        location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String){
        locationLiveData.value = Location(lng, lat)
    }
}
