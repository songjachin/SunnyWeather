package com.songjachin.weather.logic

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.songjachin.weather.logic.dao.PlaceDao
import com.songjachin.weather.logic.model.Place
import com.songjachin.weather.logic.model.Weather
import com.songjachin.weather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * Created by matthew
 * data 数据仓库
 * 此为单例
 */
object  Repository {

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavePlace() = PlaceDao.getSavePlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    //liveData====liveData是本层包裹的
    fun searchPlace(query: String):LiveData<Result<List<Place>>> = liveData(Dispatchers.IO){
        //liveData提供一个挂起函数的上下文，同时Dispatcher.io指定了子线程
        val result = try{
            //block()--->
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
            //block()
        }catch (e: Exception){
            Result.failure<List<Place>>(e)
        }
        emit(result)//
    }
    //dispatcher.io是coroutineContext
    fun searchPlaces(query: String) = define(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
    }

    private fun <T> define(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun refreshWeather(lng: String, lat: String) = define(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }
}
//fun main(){
  //  val start = System.currentTimeMillis()
    //runBlocking{
      //  repeat(100000){
        //    launch {
          //      print(".")
          //  }
       // }

    //}
    //val executor =  Executors.newSingleThreadExecutor()
    //val task = Runnable { print(".") }
    //repeat(100000){
        //executor.schedule(task, 1, TimeUnit.SECONDS)
      //  executor.execute(task)
    //}
    //val end = System.currentTimeMillis()
    //println()
    //println(end - start)
//}