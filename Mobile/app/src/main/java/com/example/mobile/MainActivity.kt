package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.fragments.Chat
import com.example.mobile.fragments.Explore
import com.example.mobile.fragments.Home
import com.example.mobile.fragments.Login
import com.example.mobile.fragments.Profile
import com.example.mobile.fragments.Register


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private fun notLoggedInFragments () {
        replaceFragment(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_button -> replaceFragment(Home())
                R.id.login_button -> replaceFragment(Login())
                R.id.register_button -> replaceFragment(Register())

                else ->{

                }
            }
            true
        }
    }

    private fun loggedInFragments () {
        replaceFragment(Explore())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.explore_button -> replaceFragment(Explore())
                R.id.profile_button -> replaceFragment(Profile())
                R.id.chat_button -> replaceFragment(Chat())

                else ->{

                }
            }
            true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val authenticated = false
        if (authenticated) {
            loggedInFragments()
        } else {
            notLoggedInFragments()
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}