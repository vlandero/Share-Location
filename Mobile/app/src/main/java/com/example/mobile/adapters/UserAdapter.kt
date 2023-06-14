package com.example.mobile.adapters

import ApiCall
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.DTOs.ManyToManyDTO
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import com.example.mobile.helpers.Alerts
import com.example.mobile.helpers.LocalStorage
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserAdapter(private var userList: MutableList<UserToBeStoredDTO>, private val onClick: (UserToBeStoredDTO) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.usernameTextView)
        val phoneTextView: TextView= view.findViewById(R.id.phoneTextView)
        val callButton: TextView = view.findViewById(R.id.callButton)
        val deleteButton: TextView = view.findViewById(R.id.deleteButton)



        @SuppressLint("NotifyDataSetChanged")
        fun bind(user: UserToBeStoredDTO) {
            nameTextView.text = user.username
            phoneTextView.text = user.phone
            itemView.setOnClickListener { onClick(user) }


            callButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${user.phone}")
                itemView.context.startActivity(intent)
            }

            deleteButton.setOnClickListener {
                //delete connection from database
                //get id from current user
                var userFromLocalStorage: UserToBeStoredDTO? = null
                val userFromLocalStorageString: String? = LocalStorage.getFromLocalStorage(itemView.context as FragmentActivity, "user")
                if(userFromLocalStorageString != null) {
                    userFromLocalStorage = Gson().fromJson(userFromLocalStorageString, UserToBeStoredDTO::class.java)
                } else {
                    println("Internal error")
                    return@setOnClickListener
                }
                val m_to_m= ManyToManyDTO(userFromLocalStorage.id, user.id)
                if(userFromLocalStorageString != null) {
                    val userFromLocalStorage = Gson().fromJson(userFromLocalStorageString, UserToBeStoredDTO::class.java)
                    val apiCall = ApiCall()
                    GlobalScope.launch(Dispatchers.IO) { // Switch to background thread for network request
                        apiCall.deleteConnectionAsync(m_to_m) { result, e ->
                            GlobalScope.launch(Dispatchers.Main) {
                                if (e != null) {
                                    println("Full exception details: $e")
                                } else {
                                    println("Connection deleted")
                                    userList.remove(user)
                                    notifyDataSetChanged()
                                }
                            }
                        }
                    }
                } else {
                    println("Internal error")
                    return@setOnClickListener
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }
}
