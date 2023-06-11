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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Login.newInstance] factory method to
 * create an instance of this fragment.
 */
class Login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
            val dto = UserLoginRequestDTO(username, password)
            val apiCall = ApiCall()
            apiCall.loginUserAsync(dto) { result, exception ->
                if (exception != null) {
                    println("Login error: $exception")
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}