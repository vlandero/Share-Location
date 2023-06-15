package com.example.mobile.fragments

import ApiCall
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.example.mobile.MainActivity
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.Serializable


private const val PROPERTYLIST = "propertyList"
private const val PHOTOS = "photos"

class Profile : Fragment() {
    private var propertyList: MutableList<Pair<String, String>> = mutableListOf()
    private var photos : MutableList<String> = mutableListOf()
    private lateinit var photoAdapter: PhotoAdapter

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
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val chooserIntent = Intent.createChooser(galleryIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        startActivityForResult(chooserIntent, GALLERY_REQUEST_CODE)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton = view.findViewById<Button>(R.id.logoutbutton)

        // Set an onClickListener to handle logout
        logoutButton.setOnClickListener {
            LocalStorage.removeFromLocalStorage(requireActivity(), "user")
            (requireActivity() as MainActivity).apply {
                notLoggedInFragments()
            }
        }


        photoAdapter = PhotoAdapter(photos){index ->
            println("Index: $index")
            photos.removeAt(index)
        }

        // Get reference to the RecyclerView from the layout
        val recyclerView = view.findViewById<RecyclerView>(R.id.profileproperties)
        val button = view.findViewById<TextView>(R.id.savebutton)

        button.setOnClickListener {
            println(propertyList)
            // save in local storage si in api
            var userFromLocalStorage: UserToBeStoredDTO? = null
            val userFromLocalStorageString: String? = LocalStorage.getFromLocalStorage(requireActivity(), "user")
            if(userFromLocalStorageString != null) {
                userFromLocalStorage = Gson().fromJson(userFromLocalStorageString, UserToBeStoredDTO::class.java)
                println("Right now logged in as: $userFromLocalStorage")
            }
            else{
                Alerts.alert(requireActivity(), "Error", "Internal error");
                println("Internal error")
                return@setOnClickListener
            }

            val apiCall = ApiCall()
            val newUser = UserToBeStoredDTO(
                id = userFromLocalStorage!!.id,
                username = propertyList[0].second,
                name = propertyList[1].second,
                age = propertyList[2].second,
                location = propertyList[3].second,
                about = propertyList[4].second,
                email = userFromLocalStorage!!.email,
                photos = ArrayList(photos),
                phone = userFromLocalStorage!!.phone
            )
            apiCall.modifyUserAsync(newUser){ result, error ->
                if(error != null){
                    activity?.runOnUiThread {
                        Alerts.alert(requireActivity(), "Error", error.message.toString())
                    }
                    return@modifyUserAsync
                }
                LocalStorage.storeInLocalStorage(requireActivity(), "user", Gson().toJson(newUser))
                activity?.runOnUiThread {
                    Alerts.alert(requireActivity(), "Success", "Profile updated successfully")
                }
            }
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
            println("Clicked add photo button")
            openGallery()

        }
    }
    private fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return base64Image
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    if (data?.data != null) {
                        // Image selected from gallery
                        val selectedImageUri = data.data
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                        val base64Image = convertBitmapToBase64(selectedImageBitmap)
                        photos.add(base64Image)
                        photoAdapter.notifyDataSetChanged()
                    } else if (data?.extras?.get("data") != null) {
                        // Image captured from camera
                        val selectedImageBitmap = data.extras?.get("data") as Bitmap
                        val base64Image = convertBitmapToBase64(selectedImageBitmap)
                        photos.add(base64Image)
                        photoAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.CAMERA) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
                        openGallery()
                    }
                } else {
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    companion object {
        const val GALLERY_REQUEST_CODE = 200
        const val CAMERA_REQUEST_CODE = 1
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


