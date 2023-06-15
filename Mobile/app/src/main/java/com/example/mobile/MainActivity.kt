package com.example.mobile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.fragments.*
import com.example.mobile.helpers.Alerts
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    internal lateinit var binding: ActivityMainBinding
    private var authenticated = false

    fun notLoggedInFragments () {
        runOnUiThread {
            replaceFragment(Login())
            binding.bottomNavigationViewNotLoggedIn.visibility = View.VISIBLE
            binding.bottomNavigationViewLoggedIn.visibility = View.GONE
            binding.bottomNavigationViewNotLoggedIn.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.login_button -> replaceFragment(Login())
                    R.id.register_button -> replaceFragment(Register())

                    else -> {

                    }
                }
                true
            }
        }
    }
    fun loggedInFragments (user: UserToBeStoredDTO?) {
        if(user == null) {
            Alerts.alert(this, "Error", "User not found")
            return
        }
        val profilePairs = getProfilePairs(user)
        println("ProfilePairs: $profilePairs")
        val pictures = user.photos
        println("Pictures: $pictures")
        runOnUiThread {
            replaceFragment(Explore.newInstance(user))
            binding.bottomNavigationViewNotLoggedIn.visibility = View.GONE
            binding.bottomNavigationViewLoggedIn.visibility = View.VISIBLE
            binding.bottomNavigationViewLoggedIn.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.explore_button -> replaceFragment(Explore.newInstance(user))
                    R.id.profile_button -> replaceFragment(
                        Profile.newInstance(
                            profilePairs,
                            pictures
                        )
                    )
                    R.id.chat_button -> replaceFragment(Chat.newInstance(user))

                    else -> {

                    }
                }
                true
            }
        }
    }
    private fun getProfilePairs(user: UserToBeStoredDTO) : MutableList<Pair<String, String>>{
        val profilePairs = emptyList<Pair<String,String>>().toMutableList()
        profilePairs += Pair("Username", user.username)
        profilePairs += Pair("Name", user.name)
        profilePairs += Pair("Age", user.age)
        profilePairs += Pair("Location", user.location)
        profilePairs += Pair("About", user.about)
        return profilePairs
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var user: UserToBeStoredDTO? = null
        val userJson = LocalStorage.getFromLocalStorage(this, "user")
        if (userJson != null) {
            user = Gson().fromJson(userJson, UserToBeStoredDTO::class.java)
            authenticated = true
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (authenticated) {
            loggedInFragments(user!!)
        } else {
            notLoggedInFragments()
        }
    }
    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}