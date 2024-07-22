import android.util.Log
import androidx.lifecycle.*
import com.swetha.aqisample.model.AQIResponse
import kotlinx.coroutines.launch
import com.swetha.aqisample.network.Result
import com.swetha.aqisample.utils.TokenManager

class AQIViewModel(private val repository: AQIRepository, private val tokenManager: TokenManager) : ViewModel() {

    private val _aqiData = MutableLiveData<Result<AQIResponse>>()
    val aqiData: LiveData<Result<AQIResponse>> get() = _aqiData

    fun getAQI(city: String) {
        viewModelScope.launch {
            _aqiData.value = Result.Loading
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    val result = repository.getAQI(city, token)
                    Log.e("AQIViewModel", "token: $token")

                    if (result.status == "ok") {
                        _aqiData.value = Result.Success(result)
                    } else {
                        _aqiData.value = Result.Failure(Exception(result.error))
                    }
                } else {
                    _aqiData.value = Result.Failure(Exception("No token found"))
                }
            } catch (e: Exception) {
                _aqiData.value = Result.Failure(e)
                Log.e("AQIViewModel", "Error: ${e.message}", e)
            }
        }
    }
}
