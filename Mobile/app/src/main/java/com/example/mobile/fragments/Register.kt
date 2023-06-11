package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
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
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */

///testttt
class Register : Fragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var usernameRegister: EditText
    private lateinit var passwordRegister: EditText
    private lateinit var confirmpasswordRegister: EditText
    private lateinit var emailRegister: EditText
    private lateinit var nameRegister: EditText
    private lateinit var phoneRegister: EditText
    private lateinit var photosRegister: LinearLayout
    private lateinit var aboutRegister: EditText
    private lateinit var locationRegister: EditText
    private lateinit var registerButton: Button
    //-------------upload image------------------
    private lateinit var image_view: ImageView
    private lateinit var button_upload: Button


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
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)



        usernameRegister = view.findViewById(R.id.usernameRegister)
        passwordRegister = view.findViewById(R.id.passwordRegister)
        confirmpasswordRegister = view.findViewById(R.id.confirmpasswordRegister)
        emailRegister = view.findViewById(R.id.emailRegister)
        nameRegister = view.findViewById(R.id.nameRegister)
        phoneRegister = view.findViewById(R.id.phoneRegister)
        photosRegister = view.findViewById(R.id.photosRegister)
        aboutRegister = view.findViewById(R.id.aboutRegister)
        locationRegister = view.findViewById(R.id.locationRegister)
        registerButton = view.findViewById(R.id.registerButton)
        //-------------upload image------------------
        image_view = view.findViewById(R.id.image_view)
        button_upload = view.findViewById(R.id.button_upload)

        registerButton.setOnClickListener {

            val username = usernameRegister.text.toString()
            val password = passwordRegister.text.toString()
            val confirmpassword = confirmpasswordRegister.text.toString()
            val email = emailRegister.text.toString()
            val name = nameRegister.text.toString()
            val phone = phoneRegister.text.toString()
            //val photos = photosRegister.text.toString()
            val about = aboutRegister.text.toString()
            val location = locationRegister.text.toString()


            //-----------------validation------------------
            if (username.isEmpty()) {
                usernameRegister.error = "Username required"
                usernameRegister.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                passwordRegister.error = "Password required"
                passwordRegister.requestFocus()
                return@setOnClickListener
            }
            if (confirmpassword.isEmpty()) {
                confirmpasswordRegister.error = "Confirm Password required"
                confirmpasswordRegister.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                emailRegister.error = "Email required"
                emailRegister.requestFocus()
                return@setOnClickListener
            }
            if (name.isEmpty()) {
                nameRegister.error = "Name required"
                nameRegister.requestFocus()
                return@setOnClickListener
            }
            if (phone.isEmpty()) {
                phoneRegister.error = "Phone required"
                phoneRegister.requestFocus()
                return@setOnClickListener
            }
            if (about.isEmpty()) {
                aboutRegister.error = "About required"
                aboutRegister.requestFocus()
                return@setOnClickListener
            }
            if (location.isEmpty()) {
                locationRegister.error = "Location required"
                locationRegister.requestFocus()
                return@setOnClickListener
            }
            if (password != confirmpassword) {
                confirmpasswordRegister.error = "Password and Confirm Password must be the same"
                confirmpasswordRegister.requestFocus()
                return@setOnClickListener
            }
            //-----------------validation------------------



            val apiCall = ApiCall()
            val photos = arrayListOf<String>("test1", "test2", "test3")
            val userToBeStoredDTO = UserToBeStoredDTO("test", "test", "test", "test", "test", "test", photos, "test", "test")
            val userRegisterRequestDTO = UserRegisterRequestDTO(userToBeStoredDTO, "test", "test", "test", "test", "test", photos, "test", "test")
            val gson = Gson()
            val json = gson.toJson(userRegisterRequestDTO)
            apiCall.registerUserAsync(userRegisterRequestDTO)
            }




        }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}