import com.example.mobile.DTOs.UserLoginRequestDTO
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.secrets.Secrets
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiCall {
    private val secrets = Secrets()
    private val mainUrl: String = secrets.API_URL + "/api/"
    private val client = OkHttpClient()

    private fun apiCall(json: String, url: String, callback: (String?, Exception?) -> Unit) {
        try {
            val requestBody =
                json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                callback(response.body.string(), null)
            } else {
                val responseBody = response.body.string()
                callback(null, Exception(responseBody ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            callback(null, e)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun registerUserAsync(dto: UserRegisterRequestDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/register"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                apiCall(json, url) { result, e ->
                    if (e != null) {
                        if(e.message?.contains("IX_Users_Email") == true) {
                            callback(null, Exception("Email already exists"))
                        } else if(e.message?.contains("IX_Users_Username") == true) {
                            callback(null, Exception("Username already exists"))
                        } else {
                            callback(null, e)
                        }
                    }
                    else {
                        callback(result, null)
                    }
                }
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun loginUserAsync(dto: UserLoginRequestDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/login"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                apiCall(json, url) { result, e ->
                    if (e != null) {
                        callback(null, Exception("Invalid username or password"))
                    }
                    else {
                        callback(result, null)
                    }
                }
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }


}
