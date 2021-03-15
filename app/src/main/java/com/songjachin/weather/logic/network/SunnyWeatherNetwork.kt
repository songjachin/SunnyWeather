package com.songjachin.weather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Created by matthew
 * 3 网络搭建，通过接口获得数据返回
 *  相当于 response =（response（Model） + retrofit）
 */
object SunnyWeatherNetwork {

    /**
     * 通过retrofit构建的类
     */
   // val placeServices  = ServiceCreator.create(PlaceService::class.java)

    private val placeService = ServiceCreator.create<PlaceService>()

    private val weatherService= ServiceCreator.create<WeatherService>()

    /**
     * 与下面的await函数搭配，此时是挂起函数
     */
    suspend fun getDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng,lat).await()

    suspend fun getRealtimeWeather(lng: String, lat: String) = weatherService.getRealtimeWeather(lng, lat).await()

    // 仍是一个挂起函数，只能在协程作用域使用
    suspend fun searchPlaces(query:String) = placeService.searchPlaces(query).await()

    /**
     * await()是扩展函数
     */
    private suspend fun <T> Call<T>.await(): T{
        //当前协程挂起，Lambda传入一个Continuation参数
        return suspendCoroutine { continuation ->
            //此处有Call的上下文
            enqueue(object: Callback<T>{
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        //恢复协程并返回数据
                        continuation.resume(body)
                    }else{
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }
            })
        }
    }


}