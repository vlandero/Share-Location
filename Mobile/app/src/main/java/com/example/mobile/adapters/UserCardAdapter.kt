package com.example.mobile.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
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
        R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5, R.color.color6, R.color.color7, R.color.color8, R.color.color9, R.color.color10, R.color.color11, R.color.color12, R.color.color13, R.color.color14, R.color.color15, R.color.color16, R.color.color17
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvLocation: TextView = view.findViewById(R.id.tvLocation)
        val tvAbout: TextView = view.findViewById(R.id.tvAbout)
        val tvAge: TextView = view.findViewById(R.id.tvAge)
        val cardImage: ImageView = view.findViewById(R.id.card_image)
        val cardContent: FrameLayout = view.findViewById(R.id.card_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_card, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setam username si location momentan sa se vada pe card
        holder.tvUsername.text = users[position].username
        holder.tvLocation.text = users[position].location
        holder.tvAbout.text = users[position].about
        holder.tvAge.text = users[position].age
        holder.tvAbout.setOnTouchListener(OnTouchListener { v, event -> // Disallow the touch request for parent scroll on touch of child view
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })
        if(users[position].photos.isNotEmpty()) {
            val decodedImage = decodeImage(users[position].photos[0])
            holder.cardImage.setImageBitmap(decodedImage)
            holder.cardImage.visibility = View.VISIBLE // make sure the ImageView is visible
            holder.cardContent.setBackgroundColor(Color.TRANSPARENT) // reset the background color
            var currentPhotoIndex = 0
            // schimbam pozele la click
            holder.itemView.setOnClickListener {
                currentPhotoIndex++
                if(currentPhotoIndex >= users[position].photos.size) {
                    currentPhotoIndex = 0
                }
                holder.cardImage.setImageBitmap(decodeImage(users[position].photos[currentPhotoIndex]))
            }
        } else {
            holder.cardImage.visibility = View.GONE
            holder.cardContent.setBackgroundColor(
                ContextCompat.getColor(context, colors[Random.nextInt(colors.size)])
            )
        }
    }

    override fun getItemCount() = users.size

    fun updateData(newUsers: ArrayList<UserToBeStoredDTO>) {
        users.clear()
        users.addAll(newUsers)
        println("Users in card adapter: $users")
        notifyDataSetChanged()
    }

    private fun decodeImage(encodedImage: String): Bitmap {
        val decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
