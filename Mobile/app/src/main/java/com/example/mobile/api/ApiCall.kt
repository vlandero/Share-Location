import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.secrets.Secrets
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ApiCall {
    private val secrets = Secrets()
    private val mainUrl: String = secrets.API_URL + "/api/"
    private val client = OkHttpClient()

    fun registerUser(dto: UserRegisterRequestDTO): Thread {
        val url = mainUrl + "Users/register"
        val thread = Thread {
            try {
                val json = Gson().toJson(dto)
                val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    println(response.body.string())
                } else {
                    println("Error: ${response.code} - ${response.message}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
        return thread
    }
}
