package com.swetha.aqisample.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.swetha.aqisample.ui.theme.AQISampleTheme
import com.swetha.aqisample.utils.TokenManager

class SplashActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(this)
        tokenManager.saveToken("a22e6c8bacb6a7b338ff091dd76c2669ed42e315")
        setContent {
            AQISampleTheme {
                Surface(color = MaterialTheme.colors.background) {
                    SplashScreen()
                }
            }
        }
    }

    @Composable
    fun SplashScreen() {
        LaunchedEffect(Unit) {
            val token = tokenManager.getToken()
            if (token != null) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }
            finish()
        }
    }
}
