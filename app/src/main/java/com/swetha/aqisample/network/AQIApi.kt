package com.swetha.aqisample.network

import com.swetha.aqisample.model.AQIResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AQIApi {

    /*To get AQIResponse by sending city name and token*/
    @GET("feed/{city}")
    suspend fun getAQI(
        @Path("city") city: String,
        @Query("token") token: String
    ): retrofit2.Response<AQIResponse>
}
