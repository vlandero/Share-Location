package com.example.mobile.fragments

import ApiCall
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import com.example.mobile.adapters.UserAdapter
import com.example.mobile.helpers.Alerts
import com.google.gson.Gson
import kotlinx.coroutines.*

private const val ARG_USER = "ARG_USER"

class Chat : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
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
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var userFromLocalStorage = paramUser
        val apiCall = ApiCall()
        val mutuallyConnectedUsers = mutableListOf<UserToBeStoredDTO>()
        // fetchuim toti userii cu care e conectat userul logat
        GlobalScope.launch(Dispatchers.IO) {
            apiCall.getConnectedUsersAsync(userFromLocalStorage!!.id) { result, e ->
                GlobalScope.launch(Dispatchers.Main) {
                    if (e != null) {
                        println("Full exception details: $e")
                        Alerts.alert(requireActivity(), "Error", "Internal error: ${e.message}");
                        println("Internal error: $e")
                    } else {
                        val connectedUsers = Gson().fromJson(result, Array<UserToBeStoredDTO>::class.java).toList()
                        println("Users the logged in user (${userFromLocalStorage.id}) is connected with: $connectedUsers")

                        if (connectedUsers.isEmpty()) {
                            Alerts.alert(requireActivity(), "Info", "You have no connections yet")
                        } else {
                            connectedUsers.forEach { user ->
                                apiCall.getConnectedUsersAsync(user.id) { result2, e2 ->
                                    if (e2 != null) {
                                        println("Full exception details: $e2")
                                    } else {
                                        val connectedUsersOfUser = Gson().fromJson(result2, Array<UserToBeStoredDTO>::class.java).toList()
                                        println("Users connected with ${user.id}: $connectedUsersOfUser")
                                        if (connectedUsersOfUser.any { it.id == userFromLocalStorage.id }) {
                                            println("User ${user.id} is connected with ${userFromLocalStorage.id}")
                                            mutuallyConnectedUsers.add(user)
                                        }
                                        println("Mutual connection with: $mutuallyConnectedUsers")
                                        GlobalScope.launch(Dispatchers.Main) {
                                            viewManager = LinearLayoutManager(context)
                                            viewAdapter = UserAdapter(mutuallyConnectedUsers) {}

                                            recyclerView =
                                                view.findViewById<RecyclerView>(R.id.recyclerView).apply {
                                                    setHasFixedSize(true)
                                                    layoutManager = viewManager
                                                    adapter = viewAdapter
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(user: UserToBeStoredDTO) =
            Chat().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_USER, user)
                }
            }
    }
}
