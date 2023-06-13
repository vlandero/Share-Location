package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USER = "ARG_USER"

/**
 * A simple [Fragment] subclass.
 * Use the [Explore.newInstance] factory method to
 * create an instance of this fragment.
 */
class Explore : Fragment() {
    // TODO: Rename and change types of parameters
    private var paramUser: UserToBeStoredDTO? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramUser = it.getSerializable(ARG_USER) as UserToBeStoredDTO
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var userArray = ArrayList<UserToBeStoredDTO>()
        val gson = Gson()
        val usersType = object : TypeToken<List<UserToBeStoredDTO>>() {}.type

        paramUser?.username?.let { username ->
            ApiCall().getFeedAsync(username) { result, error ->
                if (error != null) {
                    println("Error: ${error.message}")
                } else {
                    result?.let {
                        userArray = gson.fromJson(it, usersType)
                        println("User array for feed: " + userArray)
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Explore.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(user: UserToBeStoredDTO) =
            Explore().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user as Serializable)
                }
            }
    }
}