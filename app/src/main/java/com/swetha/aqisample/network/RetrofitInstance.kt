package com.swetha.aqisample.network


import AQIResponseDeserializer
import com.google.gson.GsonBuilder
import com.swetha.aqisample.model.AQIResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.waqi.info/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(AQIResponse::class.java, AQIResponseDeserializer())
        .create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: AQIApi by lazy {
        retrofit.create(AQIApi::class.java)
    }
}
