package com.example.mobile

import ApiCall
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.api.Auth
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_form)
        //val apiButton = findViewById<android.widget.Button>(R.id.buttonApi);
//        val apiCall = ApiCall()
//        val gson = Gson()
//        // example call with callbacks for register function
//        apiButton.setOnClickListener {
//            println("Button clicked")
//            val testDto = UserRegisterRequestDTO("test", "test", "test1", "test", "test", arrayListOf<String>("test","test"), "test", "test")
//            apiCall.registerUserAsync(testDto) { result, error ->
//                if (result != null) {
//                    val userToBeStored = gson.fromJson(result, UserToBeStoredDTO::class.java)
//                    println(userToBeStored)
//                } else {
//                    if (error != null) {
//                        println(error.message)
//                    }
//                }
//                println("after")
//            }
//        }
    }
}