package com.example.mobile.helpers

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class LocalStorage {
    companion object{
        fun storeInLocalStorage(fragment: FragmentActivity, key: String, value: String) {
            val sharedPref = fragment.getPreferences(AppCompatActivity.MODE_PRIVATE) ?: return
            with (sharedPref.edit()) {
                putString(key, value)
                commit()
            }
        }

        fun getFromLocalStorage(fragment: FragmentActivity, key: String): String? {
            val sharedPref = fragment.getPreferences(AppCompatActivity.MODE_PRIVATE)
            return sharedPref.getString(key, null)
        }

        fun removeFromLocalStorage(fragment: FragmentActivity, key: String) {
            val sharedPref = fragment.getPreferences(AppCompatActivity.MODE_PRIVATE)
            with (sharedPref.edit()) {
                remove(key)
                commit()
            }
        }
    }
}