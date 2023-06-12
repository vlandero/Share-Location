package com.example.mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R

class UserCardAdapter(
    private val context: Context,
    private var users: ArrayList<UserToBeStoredDTO>
) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Here you can bind the data to the views in the layout
        // For example, if you have a TextView with id user_name in the layout:
        // val userName: TextView = view.findViewById(R.id.user_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Here you can bind the data to the views in the ViewHolder
        // For example:
        // holder.userName.text = users[position].name
    }

    override fun getItemCount() = users.size

    fun updateData(newUsers: ArrayList<UserToBeStoredDTO>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
}
