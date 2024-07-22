package com.swetha.aqisample.ui

import AQIRepository
import AQIViewModel
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.swetha.aqisample.model.AQIResponse
import com.swetha.aqisample.viewmodel.AQIViewModelFactory
import java.util.Locale
import com.swetha.aqisample.network.Result
import com.swetha.aqisample.network.RetrofitInstance
import com.swetha.aqisample.utils.TokenManager

class MainActivity : ComponentActivity() {

    private val api = RetrofitInstance.api
    private val repository = AQIRepository(api)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var tokenManager: TokenManager
    private lateinit var aqiViewModel: AQIViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        tokenManager = TokenManager(this) // Pass the Activity context here

        val viewModelFactory = AQIViewModelFactory(repository, tokenManager)
        aqiViewModel = ViewModelProvider(this, viewModelFactory).get(AQIViewModel::class.java)

        setContent {
            AQIApp(viewModel = aqiViewModel, requestLocationAndUpdateAQI = ::requestLocationAndUpdateAQI)
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationAndUpdateAQI() {
        if (checkLocationPermissions()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latitude = it.latitude
                    val longitude = it.longitude
                    val cityName = getCityName(latitude, longitude)
                    Log.d("MainActivity", "Location: $cityName")
                    aqiViewModel.getAQI(cityName)
                }
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun getCityName(lat: Double, lon: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        return addresses?.get(0)?.locality ?: "Unknown"
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationAndUpdateAQI()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Perform any necessary cleanup
        Log.d("MainActivity", "Resources cleaned up in onDestroy")
    }}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AQIApp(viewModel: AQIViewModel, requestLocationAndUpdateAQI: () -> Unit) {
    var location by remember { mutableStateOf("") }
    val aqiData by viewModel.aqiData.observeAsState()
    val isLoading = aqiData is Result.Loading

    LaunchedEffect(Unit) {
        requestLocationAndUpdateAQI()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("AQI App") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Enter Location") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (location.isNotBlank()) {
                    viewModel.getAQI(location)
                }
            }) {
                Text("Fetch AQI")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                aqiData?.let { result ->
                    when (result) {
                        is Result.Success -> AQIContent(data = result.data)
                        is Result.Failure -> Text(text = "Error: ${result.exception.message}")
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
fun AQIContent(data: AQIResponse) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "City: ${data.data?.city?.name ?: "Unknown"}", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "AQI: ${data.data?.aqi ?: "N/A"}", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Latitude: ${data.data?.city?.geo?.get(0) ?: "N/A"}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Longitude: ${data.data?.city?.geo?.get(1) ?: "N/A"}", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(8.dp))
        data?.data?.iaqi?.let {
            Text(text = "Dominant Pollutant: ${data.data.dominentpol}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "PM2.5: ${it.pm25?.v ?: "N/A"}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Temperature: ${it.t?.v ?: "N/A"} °C", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Humidity: ${it.h?.v ?: "N/A"} %", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Wind Speed: ${it.w?.v ?: "N/A"} m/s", style = MaterialTheme.typography.body1)
        }
    }
}
