package com.songjachin.weather.logic.dao

import android.content.Context
import androidx.core.content.edit

import com.google.gson.Gson
import com.songjachin.weather.SunnyWeatherApplication
import com.songjachin.weather.logic.model.Place

/**
 * Created by matthew
 *
 *
 */
object PlaceDao {
    fun savePlace(place: Place){
        sharedPreferences().edit{
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavePlace():Place{
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather",
        Context.MODE_PRIVATE)
}