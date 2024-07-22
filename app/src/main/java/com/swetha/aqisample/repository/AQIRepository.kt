import android.util.Log

import com.swetha.aqisample.model.AQIResponse
import com.swetha.aqisample.network.AQIApi

class AQIRepository(private val api: AQIApi) {
    suspend fun getAQI(city: String, token: String): AQIResponse {
        try {
            val response = api.getAQI(city, token)
            if (response.isSuccessful) {
                return response.body() ?: throw IllegalStateException("Response body is null")
            } else {
                throw IllegalStateException("API call failed with code: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("AQIRepository", "Error fetching AQI data: ${e.message}", e)
            throw e
        }
    }
}

//// Retrofit instance with custom deserializer
//val gson = GsonBuilder()
//    .registerTypeAdapter(AQIResponse::class.java, AQIResponseDeserializer())
//    .create()
//
//val retrofit = Retrofit.Builder()
//    .baseUrl("https://api.example.com/")
//    .addConverterFactory(GsonConverterFactory.create(gson))
//    .build()
//
//val api: AQIApi = retrofit.create(AQIApi::class.java)
