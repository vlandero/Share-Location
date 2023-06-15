package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.example.mobile.DTOs.UserLoginRequestDTO
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.MainActivity
import com.example.mobile.R
import com.example.mobile.api.Auth
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Register : Fragment()  {
    private lateinit var usernameRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var confirmPasswordRegister: EditText
    private lateinit var emailRegister: EditText
    private lateinit var nameRegister: EditText
    private lateinit var phoneRegister: EditText
    private lateinit var photosRegister: LinearLayout
    private lateinit var aboutRegister: EditText
    private lateinit var ageRegister: EditText
    private lateinit var locationRegister: AutoCompleteTextView
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameRegister = view.findViewById(R.id.usernameRegister)
        passwordRegister = view.findViewById(R.id.passwordRegister)
        confirmPasswordRegister = view.findViewById(R.id.confirmPasswordRegister)
        emailRegister = view.findViewById(R.id.emailRegister)
        nameRegister = view.findViewById(R.id.nameRegister)
        phoneRegister = view.findViewById(R.id.phoneRegister)
        aboutRegister = view.findViewById(R.id.aboutRegister)
        ageRegister = view.findViewById(R.id.ageRegister)
        locationRegister = view.findViewById(R.id.locationRegister)
        registerButton = view.findViewById(R.id.registerButton)

        // cream un dropdown cu locatiile available
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.locations_array,
            android.R.layout.simple_dropdown_item_1line
        ).also { adapter ->
            locationRegister.setAdapter(adapter)
        }
        locationRegister.keyListener = null
        locationRegister.setOnClickListener {
            locationRegister.showDropDown()
        }
        locationRegister.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT

        confirmPasswordRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = passwordRegister.text.toString().trim()
                val confirmPassword = s.toString().trim()
                if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                    confirmPasswordRegister.error = "Password and Confirm Password must match"
                } else {
                    confirmPasswordRegister.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        emailRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString().trim()
                if (email.isNotEmpty() && !isEmailValid(email)) {
                    emailRegister.error = "Invalid email address"
                } else {
                    emailRegister.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        // Live validation for phone field
        phoneRegister.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phone = s.toString().trim()
                if (phone.isNotEmpty() && !isPhoneValid(phone)) {
                    phoneRegister.error = "Invalid phone number"
                } else {
                    phoneRegister.error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        registerButton.setOnClickListener {
            val username = usernameRegister.text.toString()
            val password = passwordRegister.text.toString()
            val confirmPassword = confirmPasswordRegister.text.toString()
            val email = emailRegister.text.toString()
            val name = nameRegister.text.toString()
            val phone = phoneRegister.text.toString()
            val about = aboutRegister.text.toString()
            val age = ageRegister.text.toString()
            val location = locationRegister.text.toString()
            //-----------------validation------------------
            if (username.isEmpty()) {
                usernameRegister.error = "Username required"
                usernameRegister.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordRegister.error = "Password required"
                passwordRegister.requestFocus()
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                confirmPasswordRegister.error = "Confirm Password required"
                confirmPasswordRegister.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                emailRegister.error = "Email required"
                emailRegister.requestFocus()
                return@setOnClickListener
            }
            if (!isEmailValid(email)) {
                emailRegister.error = "Invalid email address"
                emailRegister.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                nameRegister.error = "Name required"
                nameRegister.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                phoneRegister.error = "Phone required"
                phoneRegister.requestFocus()
                return@setOnClickListener
            }
            if (!isPhoneValid(phone)) {
                phoneRegister.error = "Invalid phone number"
                phoneRegister.requestFocus()
                return@setOnClickListener
            }
            if (about.isEmpty()) {
                aboutRegister.error = "About required"
                aboutRegister.requestFocus()
                return@setOnClickListener
            }
            if (age.isEmpty()) {
                ageRegister.error = "Age required"
                ageRegister.requestFocus()
                return@setOnClickListener
            }
            if (location.isEmpty()) {
                locationRegister.error = "Location required"
                locationRegister.requestFocus()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                confirmPasswordRegister.error = "Password and Confirm Password must be the same"
                confirmPasswordRegister.requestFocus()
                return@setOnClickListener
            }
            //-----------------validation------------------

            val apiCall = ApiCall()
            val gson = Gson()
            val photos = arrayListOf<String>()
            val newUserDto = UserRegisterRequestDTO(
                username,
                password,
                age,
                email,
                name,
                phone,
                photos,
                about,
                location
            )
            // inregistrare user
            apiCall.registerUserAsync(newUserDto) { result, error ->
                if (result != null) {
                    val userToBeStored = gson.fromJson(result, UserToBeStoredDTO::class.java)
                    println(userToBeStored)
                    // daca e ok inregisrearea, vom loga userul
                    val userLoginDto = UserLoginRequestDTO(username, password)
                    apiCall.loginUserAsync(userLoginDto) { result, exception ->
                        if (exception != null) {
                            println("Login error after register: $exception")
                        } else {
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
                } else {
                    if (error != null) {
                        println(error.message)
                    }
                }
            }
        }
    }
    private fun isEmailValid(email: String): Boolean {
        val pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(pattern.toRegex())
    }

    private fun isPhoneValid(phone: String): Boolean {
        val pattern = "^[0-9]{10}$"
        return phone.matches(pattern.toRegex())
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            Register().apply {
                arguments = Bundle().apply {
                }
            }
    }
}