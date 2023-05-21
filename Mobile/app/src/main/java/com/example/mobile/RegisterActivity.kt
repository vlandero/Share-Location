package com.example.mobile

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import ApiCall
import android.content.Intent
import android.widget.ImageView
import com.example.mobile.DTOs.UserLoginRequestDTO
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.api.Auth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity: AppCompatActivity() {
    private lateinit var usernameRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var emailRegister: EditText
    private lateinit var nameRegister: EditText
    private lateinit var phoneRegister: EditText
    private lateinit var photosRegister: LinearLayout
    private lateinit var aboutRegister: EditText
    private lateinit var locationRegister: EditText
    private lateinit var registerButton: Button
    private lateinit var image_view: ImageView
    private lateinit var button_upload: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        usernameRegister = findViewById(R.id.usernameRegister)
        passwordRegister = findViewById(R.id.passwordRegister)
        emailRegister = findViewById(R.id.emailRegister)
        nameRegister = findViewById(R.id.nameRegister)
        phoneRegister = findViewById(R.id.phoneRegister)
        photosRegister = findViewById(R.id.photosRegister)
        aboutRegister = findViewById(R.id.aboutRegister)
        locationRegister = findViewById(R.id.locationRegister)
        registerButton = findViewById(R.id.registerButton)
        button_upload = findViewById(R.id.button_upload)


        val apiCall = ApiCall()
        val gson = Gson()

        registerButton.setOnClickListener {
            val userRegisterRequestDTO = UserRegisterRequestDTO(
                usernameRegister.text.toString(),
                passwordRegister.text.toString(),
                emailRegister.text.toString(),
                nameRegister.text.toString(),
                phoneRegister.text.toString(),
                arrayListOf<String>(),
                aboutRegister.text.toString(),
                locationRegister.text.toString()
            )
            apiCall.registerUserAsync(userRegisterRequestDTO) { result, error ->
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
        }




        fun openGallery() {//open gallery intent
            Intent(Intent.ACTION_PICK).also {
                it.type = "image/*"
                val file_ypes = arrayOf("image/jpeg", "image/png")
                it.putExtra(Intent.EXTRA_MIME_TYPES, file_ypes);//to filter only jpg and png
                startActivityForResult(it, 101)//start activity for result with request code 101

            }

                   }

        image_view.setOnClickListener {
            openGallery()
        }



        fun uploadImage() {
            val apiCall = ApiCall()
            val gson = Gson()
            val userLoginRequestDTO = UserLoginRequestDTO(
                usernameRegister.text.toString(),
                passwordRegister.text.toString()
            )
            apiCall.loginUserAsync(userLoginRequestDTO) { result, error ->
                if (result != null) {
                    val auth = gson.fromJson(result, Auth::class.java)
                    println(auth)
                } else {
                    if (error != null) {
                        println(error.message)
                    }
                }
                println("after")
            }
        }

        button_upload.setOnClickListener{
            uploadImage()
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {//handle result of gallery intent
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == RESULT_OK) {
                if (requestCode == 101) {
                    image_view.setImageURI(data?.data) //handle chosen image
                }
            }
        }
    }





}