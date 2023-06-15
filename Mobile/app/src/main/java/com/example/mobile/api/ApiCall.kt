import com.example.mobile.DTOs.ManyToManyDTO
import com.example.mobile.DTOs.UserLoginRequestDTO
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
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

    private fun postApiCall(json: String, url: String, callback: (String?, Exception?) -> Unit) {
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

    private fun getApiCall(url: String, callback: (String?, Exception?) -> Unit) {
        try {
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                callback(response.body.string(), null)
            } else {
                val responseBody = response.body.string()
                callback(null, Exception(responseBody ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            println("Exception during getApiCall: $e") // Logging the error with its stack trace
            callback(null, e)
        }
    }

    private fun deleteApiCall(json: String, url: String, callback: (String?, Exception?) -> Unit) {
        try {
            val requestBody =
                json.toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url(url)
                .delete(requestBody)
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
                postApiCall(json, url) { result, e ->
                    if (e != null) {
                        if(e.message?.contains("IX_Users_Email") == true) {
                            callback(null, Exception("Email already exists"))
                        } else if(e.message?.contains("IX_Users_Username") == true) {
                            callback(null, Exception("Username already exists"))
                        } else {
                            println("Error registering user ${e}")
                            callback(null, e)
                        }
                    }
                    else {
                        println("User registered successfully")
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
                postApiCall(json, url) { result, e ->
                    if (e != null) {
                        callback(null, e)
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
    fun getConnectedUsersAsync(id: String, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/connected-users/" + id
        GlobalScope.launch {
            try {
                getApiCall(url) { result, e ->
                    if (e != null) {
                        callback(null, Exception("Error in getApiCall: ${e.message}", e)) // Add more detail to the exception
                    }
                    else {
                        callback(result, null)
                    }
                }
            } catch (e: Exception) {
                callback(null, Exception("Exception caught in getConnectedUsersAsync: ${e.message}", e)) // Add more detail to the exception
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun getUserByIdAsync(id: String, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/get-by-id/" + id
        GlobalScope.launch {
            try {
                getApiCall(url) { result, e ->
                    if (e != null) {
                        callback(null, e)
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
    fun modifyUserAsync(dto: UserToBeStoredDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/modify"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                postApiCall(json, url) { result, e ->
                    if (e != null) {
                        callback(null, Exception("Error modifying user"))
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
    fun connectAsync(dto: ManyToManyDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/connect"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                postApiCall(json, url) { result, e ->
                    if (e != null) {
                        callback(null, Exception("Error connecting users"))
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
    fun rejectAsync(dto: ManyToManyDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/reject"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                postApiCall(json, url) { result, e ->
                    if (e != null) {
                        callback(null, Exception("Error rejecting users"))
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
    fun getFeedAsync(name: String, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/feed/$name"
        println("url: " + url)
        GlobalScope.launch {
            try{
                getApiCall(url) { result, e ->
                    if (e != null) {
                        callback(null, e)
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
    fun deleteRejectionAsync(dto: ManyToManyDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/delete-rejection"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                deleteApiCall(json, url) { result, e ->
                    if (e != null) {
                        callback(null, Exception("Error deleting rejection"))
                    } else {
                        callback(result, null)
                    }
                }
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteConnectionAsync(dto: ManyToManyDTO, callback: (String?, Exception?) -> Unit) {
        val url = mainUrl + "Users/delete-connection"
        GlobalScope.launch {
            try {
                val json = Gson().toJson(dto)
                deleteApiCall(json, url) { result, e ->
                    if (e != null) {
                        println("Error deleting connection: ${e}")
                        callback(null, Exception("Error deleting connection"))
                    } else {
                        println("Connection deleted successfully")
                        callback(result, null)
                    }
                }
            } catch (e: Exception) {
                callback(null, e)
            }
        }
    }


}
