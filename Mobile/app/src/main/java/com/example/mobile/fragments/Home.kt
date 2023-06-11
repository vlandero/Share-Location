package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.DTOs.UserRegisterRequestDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiButton = view.findViewById<android.widget.Button>(R.id.button2);
        val apiCall = ApiCall()
        val gson = Gson()
        // example call with callbacks for register function
        apiButton.setOnClickListener {
            println("Button clicked")
            val testDto = UserRegisterRequestDTO("test", "test", "19", "test1", "test", "test",  arrayListOf<String>("test","test"), "test", "test")
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
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}