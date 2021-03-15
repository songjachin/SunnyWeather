package com.songjachin.weather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by matthew
 * 2、retrofit构建，object是单例，因此create可以直接引用
 *
 */
object ServiceCreator {
    /**
     * object 是单例模式
     */
    private const val BASE_URL = "https://api.caiyunapp.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun getRetrofit(): Retrofit = retrofit
    /**
     *
     */
    fun <T> create(serviceClass: Class<T>):T = retrofit.create(serviceClass)

    /**
     *
     */
    inline fun <reified T> create(): T = getRetrofit().create(T::class.java)
}