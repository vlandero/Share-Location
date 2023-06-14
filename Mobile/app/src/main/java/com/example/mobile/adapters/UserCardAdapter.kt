package com.example.mobile.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.DTOs.UserToBeStoredDTO
import com.example.mobile.R
import kotlin.random.Random

class UserCardAdapter(
    private val context: Context,
    private var users: ArrayList<UserToBeStoredDTO>
) : RecyclerView.Adapter<UserCardAdapter.ViewHolder>() {

    private val colors = arrayOf(
        R.color.color1, R.color.color2, R.color.color3, // Add more colors
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val cardContent: FrameLayout = view.findViewById(R.id.card_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvUsername.text = users[position].username
        holder.cardContent.setBackgroundColor(
            ContextCompat.getColor(context, colors[Random.nextInt(colors.size)])
        )
    }

    override fun getItemCount() = users.size

    fun updateData(newUsers: ArrayList<UserToBeStoredDTO>) {
        users.clear()
        users.addAll(newUsers)
        println("Users in card adapter: $users")
        notifyDataSetChanged()
    }
}
