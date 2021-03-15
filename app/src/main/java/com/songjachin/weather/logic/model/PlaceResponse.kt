package com.songjachin.weather.logic.model

import com.google.gson.annotations.SerializedName

/**
 * Created by matthew
 *1、规定数据模型PlaceResponse ( Place + status)---
 *
 */
data class PlaceResponse(val status:String, val places: List<Place>)

data class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address : String)

data class Location(val lng: String, val lat: String)