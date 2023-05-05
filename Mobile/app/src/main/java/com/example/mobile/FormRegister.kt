package com.example.mobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ApiCall
import com.example.mobile.DTOs.UserLoginRequestDTO
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.api.Auth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FormRegister: AppCompatActivity() {
    private lateinit var usernameRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var emailRegister: EditText
    private lateinit var nameRegister: EditText
    private lateinit var phoneRegister: EditText
    private lateinit var photosRegister: LinearLayout
    private lateinit var aboutRegister: EditText
    private lateinit var locationRegister: EditText
    private lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {




        usernameRegister = findViewById(R.id.username)
        passwordRegister = findViewById(R.id.password)
        emailRegister = findViewById(R.id.email)
        nameRegister = findViewById(R.id.name)
        phoneRegister = findViewById(R.id.phone)
        photosRegister = findViewById(R.id.photos_layout)
        aboutRegister = findViewById(R.id.about)
        locationRegister = findViewById(R.id.location)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val apiCall = ApiCall()
            val gson = Gson()
            //photos????------------------------------------------------

            val testDto = UserRegisterRequestDTO(usernameRegister.text.toString(), passwordRegister.text.toString(), emailRegister.text.toString(), nameRegister.text.toString(), phoneRegister.text.toString(), arrayListOf<String>("test","test"), aboutRegister.text.toString(), locationRegister.text.toString())
            apiCall.registerUserAsync(testDto) { result, error ->
                if (result != null) {
                    val userToBeStored = gson.fromJson(result, UserToBeStoredDTO::class.java)
                    println(userToBeStored)
                } else {
                    if (error != null) {
                        println(error.message)
                    }
                }
                println("after")
            }

            val testDto2 = UserLoginRequestDTO(usernameRegister.text.toString(), passwordRegister.text.toString())
            //login after register with login fromn apiCall
            apiCall.loginUserAsync(testDto2) { result, error ->
                if (result != null) {
                    val userToBeStored = gson.fromJson(result, UserToBeStoredDTO::class.java)
                    println(userToBeStored)
                } else {
                    if (error != null) {
                        println(error.message)
                    }
                }
                println("after")
            }

            super.onCreate(savedInstanceState)
            setContentView(R.layout.profile_page)




        }
    }




}