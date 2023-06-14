package com.example.mobile.adapters

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R


class UserAdapter(private val userList: List<UserToBeStoredDTO>, private val onClick: (UserToBeStoredDTO) -> Unit) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.usernameTextView)
        val phoneTextView: TextView= view.findViewById(R.id.phoneTextView)
        val callButton: TextView = view.findViewById(R.id.callButton)



        fun bind(user: UserToBeStoredDTO) {
            nameTextView.text = user.username
            phoneTextView.text = user.phone
            itemView.setOnClickListener { onClick(user) }


            callButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${user.phone}")
                itemView.context.startActivity(intent)
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
