package com.example.mobile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.adapters.ProfilePropertyAdapter
import java.io.Serializable

private const val PROPERTYLIST = "propertyList"

class Profile : Fragment() {
    // TODO: Rename and change types of parameters
    private var propertyList = listOf(
        Pair("Property 1", "Value 1"),
        Pair("Property 2", "Value 2"),
        Pair("Property 3", "Value 3")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = activity?.intent
        arguments?.let {
            propertyList = it.getSerializable(PROPERTYLIST) as? List<Pair<String, String>> ?: emptyList()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        println("Profile.onCreateView")
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get reference to the RecyclerView from the layout
        val recyclerView = view.findViewById<RecyclerView>(R.id.profileproperties)
        val button = view.findViewById<TextView>(R.id.savebutton)

        button.setOnClickListener {
            println(propertyList)
            println("Save button clicked")
        }

        val adapter = ProfilePropertyAdapter(propertyList){
            propertyList = it;
        }
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: List<Pair<String, String>>) =
            Profile().apply {
                arguments = Bundle().apply {
                    putSerializable(PROPERTYLIST, param1 as Serializable)
                }
            }
    }
}