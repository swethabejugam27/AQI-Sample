package com.swetha.aqisample.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_NAME = "secure_prefs"
        private const val TOKEN_KEY = "auth_token"
    }

    init {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        sharedPreferences = EncryptedSharedPreferences.create(
            PREFS_NAME,
            masterKeyAlias,
            context.applicationContext, // Use application context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove(TOKEN_KEY).apply()
    }
}

//@file:Suppress("DEPRECATION")
//
//package com.swetha.aqisample.utils
//
//import android.content.Context
//import androidx.security.crypto.EncryptedSharedPreferences
//import androidx.security.crypto.MasterKeys
//
//class TokenManager(context: Context) {
//
//    private val sharedPreferences = EncryptedSharedPreferences.create(
//        "secure_prefs",
//        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
//        context,
//        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//    )
//
//    fun saveToken(token: String) {
//        sharedPreferences.edit().putString("token", token).apply()
//    }
//
//    fun getToken(): String? {
//        return sharedPreferences.getString("token", null)
//    }
//
//    fun clearToken() {
//        sharedPreferences.edit().remove("token").apply()
//    }
//}
