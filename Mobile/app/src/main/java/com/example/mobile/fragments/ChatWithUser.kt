package com.example.mobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R

class ChatWithUser : Fragment() {
    private lateinit var selectedUser: UserToBeStoredDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedUser = it.getSerializable(ARG_USER) as UserToBeStoredDTO
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_with_user, container, false)
    }

    companion object {
        private const val ARG_USER = "user"

        @JvmStatic
        fun newInstance(user: UserToBeStoredDTO) =
            ChatWithUser().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                }
            }
    }
}