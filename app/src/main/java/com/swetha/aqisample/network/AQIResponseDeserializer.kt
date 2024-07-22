import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement

//package com.swetha.aqisample.network
//
//import com.google.gson.*
//import com.swetha.aqisample.model.AQIData
//import com.swetha.aqisample.model.AQIResponse
//import java.lang.reflect.Type
////
////class AQIResponseDeserializer : JsonDeserializer<AQIResponse> {
////    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AQIResponse {
////        val jsonObject = json.asJsonObject
////        val status = jsonObject.get("status").asString
////        val dataElement = jsonObject.get("data")
////
////        val data: Any = if (status == "ok") {
////            context.deserialize<AQIData>(dataElement, AQIData::class.java)
////        } else {
////            dataElement.asString
////        }
////
////        return AQIResponse(status, data)
////    }
//}
//
/**
 * Custom deserializer for AQIResponse to handle specific JSON parsing logic.
 */

import com.google.gson.*
import com.swetha.aqisample.model.AQIData
import com.swetha.aqisample.model.AQIResponse
import java.lang.reflect.Type

class AQIResponseDeserializer : JsonDeserializer<AQIResponse> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AQIResponse {
        val jsonObject = json.asJsonObject

        // Extracting the status field
        val status = jsonObject.get("status").asString
        return if (status == "ok") {
            val data = context.deserialize<AQIData>(jsonObject.get("data"), AQIData::class.java)
            AQIResponse(status, data)
        } else {
            val errorMessage = jsonObject.get("data").asString
            AQIResponse(status, null, errorMessage)
        }

    }
}
