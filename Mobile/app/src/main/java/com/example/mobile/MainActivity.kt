package com.example.mobile

import ApiCall
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mobile.DTOs.UserRegisterRequestDTO


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiButton = findViewById<android.widget.Button>(R.id.buttonApi);
        apiButton.setOnClickListener {
            println("Button clicked")
            val testDto = UserRegisterRequestDTO("test", "test", "test", "test", "test", arrayListOf<String>("test","test"), "test", "test")
            val thread = ApiCall().registerUser(testDto)
            thread.start()
        }
    }
}