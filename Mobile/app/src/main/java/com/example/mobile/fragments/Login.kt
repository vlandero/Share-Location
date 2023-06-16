package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.mobile.DTOs.UserLoginRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.mobile.api.Auth
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.helpers.Alerts

class Login : Fragment() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etUsername = view.findViewById(R.id.etUsername)
        etPassword = view.findViewById(R.id.etPassword)
        btnLogin = view.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if (username.isEmpty()) {
                etUsername.error = "Username required"
                etUsername.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                etPassword.error = "Password required"
                etPassword.requestFocus()
                return@setOnClickListener
            }

            val dto = UserLoginRequestDTO(username, password)
            val apiCall = ApiCall()
            apiCall.loginUserAsync(dto) { result, exception ->
                if (exception != null) {
                    println("Error logging in: $exception")
                    activity?.runOnUiThread {
                        Alerts.alert(requireActivity(), "Error", "Invalid username or password. Please try again.")
                    }
                    // Handle login error
                } else {
                    val gson = Gson()
                    val responseType = object : TypeToken<Map<String, String>>() {}.type
                    val response: Map<String, String> = gson.fromJson(result, responseType)
                    val token = response["token"]

                    // Decode the JWT token and store the decoded values in LocalStorage
                    if (token != null) {
                        val decodedToken = Auth().decodeJwtToken(token)
                        if (decodedToken != null) {
                            LocalStorage.storeInLocalStorage(requireActivity(), "user", Gson().toJson(decodedToken))

                            val userId = decodedToken["id"] as? String
                            println("User ID: $userId")
                            if (userId != null) {
                                apiCall.getUserByIdAsync(userId) { userResult, userException ->
                                    if (userException != null) {
                                        println("Error fetching user details: $userException")
                                        // Handle error
                                    } else {
                                        val user = Gson().fromJson(userResult, UserToBeStoredDTO::class.java)
                                        LocalStorage.storeInLocalStorage(requireActivity(), "user", Gson().toJson(user))
                                        (requireActivity() as MainActivity).apply {
                                            if (user.id.isNotEmpty()) {
                                                println("User is authenticated")
                                                loggedInFragments(user)
                                            } else {
                                                println("User is not authenticated")
                                                notLoggedInFragments()
                                            }
                                        }
                                    }
                                }
                            } else {
                                println("User ID was not found in the decoded token")
                            }
                        } else {
                            println("Failed to decode JWT token")
                        }
                    } else {
                        println("Token was not found in the response")
                    }
                }
            }
            println("Login button clicked")
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Login().apply {
                arguments = Bundle().apply {
                }
            }
    }
}