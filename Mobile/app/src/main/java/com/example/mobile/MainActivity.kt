package com.example.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.databinding.ActivityMainBinding
import com.example.mobile.fragments.Chat
import com.example.mobile.fragments.Explore
import com.example.mobile.fragments.Home
import com.example.mobile.fragments.Login
import com.example.mobile.fragments.Profile
import com.example.mobile.fragments.Register
import com.example.mobile.helpers.Alerts
import com.example.mobile.helpers.Images
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var authenticated = false

    private fun notLoggedInFragments () {
        binding.bottomNavigationViewNotLoggedIn.visibility = View.VISIBLE
        binding.bottomNavigationViewLoggedIn.visibility = View.GONE
        replaceFragment(Home())
        binding.bottomNavigationViewNotLoggedIn.setOnItemSelectedListener {
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

    private fun getProfilePairs(user: UserToBeStoredDTO) : MutableList<Pair<String, String>>{
        val profilePairs = emptyList<Pair<String,String>>().toMutableList()
        profilePairs += Pair("Name", user.name)
        profilePairs += Pair("Email", user.email)
        profilePairs += Pair("Location", user.location)
        profilePairs += Pair("About", "fasdffdadsgsa fd afdg sfd gsdf gsfdger gfdg daf")
        return profilePairs
    }

    private fun loggedInFragments (user: UserToBeStoredDTO?) {
        if(user == null) {
            Alerts.alert(this, "Error", "User not found")
            return
        }
        val profilePairs = getProfilePairs(user)
        val pictures = user.photos
        binding.bottomNavigationViewNotLoggedIn.visibility = View.GONE
        binding.bottomNavigationViewLoggedIn.visibility = View.VISIBLE
        replaceFragment(Explore())
        binding.bottomNavigationViewLoggedIn.setOnItemSelectedListener {
            when(it.itemId){
                R.id.explore_button -> replaceFragment(Explore())
                R.id.profile_button -> replaceFragment(Profile.newInstance(profilePairs, pictures))
                R.id.chat_button -> replaceFragment(Chat())

                else ->{

                }
            }
            true
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // de test, de comentat si de decomentat
//        val testDto = UserRegisterRequestDTO("test", "test", "test1", "test", "test", arrayListOf<String>(Images.img1, Images.img1), "test gmwnrk aflk asdhfasliuhgfas rgbasrhalufisd gfjbgsbgwlebgew gbfdsijghsdrigjes sfduighsdrioughwrlg sdfghwrgliweruhges werhgesbgsle", "test")
//        LocalStorage.storeInLocalStorage(this, "user", Gson().toJson(testDto))
//        LocalStorage.removeFromLocalStorage(this, "user")
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

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}