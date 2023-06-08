package com.example.mobile.fragments

import ApiCall
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import com.example.mobile.adapters.PhotoAdapter
import com.example.mobile.adapters.ProfilePropertyAdapter
import com.example.mobile.helpers.Alerts
import com.example.mobile.helpers.Images
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson
import java.io.Serializable


private const val PROPERTYLIST = "propertyList"
private const val PHOTOS = "photos"

class Profile : Fragment() {

    private var propertyList: MutableList<Pair<String, String>> = mutableListOf()
    private var photos : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            propertyList = it.getSerializable(PROPERTYLIST) as? MutableList<Pair<String, String>> ?: mutableListOf()
            photos = it.getSerializable(PHOTOS) as? MutableList<String> ?: mutableListOf()
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
    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }
    @SuppressLint("NotifyDataSetChanged", "IntentReset")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val photoAdapter = PhotoAdapter(photos){index ->
            println("Index: $index")
            photos.removeAt(index)
        }

        // Get reference to the RecyclerView from the layout
        val recyclerView = view.findViewById<RecyclerView>(R.id.profileproperties)
        val button = view.findViewById<TextView>(R.id.savebutton)

        button.setOnClickListener {
            println(propertyList)
            // save in local storage si in api
            val userFromLocalStorage: UserToBeStoredDTO? = null
            val userFromLocalStorageString: String? = LocalStorage.getFromLocalStorage(requireActivity(), "user")
            if(userFromLocalStorageString != null) {
                val userFromLocalStorage: UserToBeStoredDTO = Gson().fromJson(userFromLocalStorageString, UserToBeStoredDTO::class.java)
                println(userFromLocalStorage)
            }
            else{
                Alerts.alert(requireActivity(), "Error", "Internal error");
                println("Internal error")
                return@setOnClickListener
            }

            val apiCall = ApiCall()
            val newUser = UserToBeStoredDTO(
                id=userFromLocalStorage!!.id,
                username = propertyList[0].second,
                name = propertyList[1].second,
                age = propertyList[2].second,
                location = propertyList[3].second,
                about = propertyList[4].second,
                email = userFromLocalStorage!!.email,
                photos = ArrayList(photos),
                phone = userFromLocalStorage!!.phone
            )
//            apiCall.modifyUserAsync(newUser){ result, error ->
//                if(error != null){
//                    Alerts.alert(requireActivity(), "Error", error.message.toString())
//                    return@modifyUserAsync
//                }
//                LocalStorage.storeInLocalStorage(requireActivity(), "user", Gson().toJson(newUser))
//                Alerts.alert(requireActivity(), "Success", "Profile updated successfully")
//            } // TODO de decomentat cand se face login
            println("Save button clicked")
        }

        val adapter = ProfilePropertyAdapter(propertyList){
            propertyList = it;
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val viewPager2 = view.findViewById<ViewPager2>(R.id.photoViewPager)
        viewPager2.adapter = photoAdapter
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val addPhotoButton = view.findViewById<ImageView>(R.id.add_picture)
        addPhotoButton.setOnClickListener {
            println("clicked add photo button")
//            openGallery()
            photos.add(Images.img1) // TODO de facut upload aici
            photoAdapter!!.notifyDataSetChanged()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Profile.GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            println("OK")

            // Use the base64Image as needed
        }
    }


    companion object {
        const val GALLERY_REQUEST_CODE = 123
        @JvmStatic
        fun newInstance(userprops: MutableList<Pair<String, String>>, photos: MutableList<String>) =
            Profile().apply {
                arguments = Bundle().apply {
                    putSerializable(PROPERTYLIST, userprops as Serializable)
                    putSerializable(PHOTOS, photos as Serializable)
                }
            }
    }
}