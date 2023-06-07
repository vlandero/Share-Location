package com.example.mobile.adapters

import android.annotation.SuppressLint
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobile.R

class PhotoAdapter(private val photos: MutableList<String>, private val callback: (Int) -> Unit): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var pic: ImageView = itemView.findViewById(R.id.photoImageView)
        var delete: ImageView = itemView.findViewById(R.id.deletephoto)
        init {
            itemView.setOnClickListener {
                println("PhotoAdapter.ViewHolder.onClick")
                println(pic)
            }
            delete.setOnClickListener {
                // delete photo
                val deletedPhoto = photos.removeAt(adapterPosition)
                println("Adapter position: $adapterPosition")
                notifyDataSetChanged()
                // add callback
                callback(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_photo_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = photos[position]
        val photoBytes = Base64.decode(photo, Base64.DEFAULT)
        Glide.with(holder.itemView.context)
            .asBitmap()
            .load(photoBytes)
            .placeholder(R.drawable.baseline_broken_image_24) // adaugam un placeholder in caz ca nu se incarca imaginea
            .into(holder.pic)
    }

    override fun getItemCount(): Int = photos.size
    interface OnPhotoDeleteListener {
        fun onPhotoDeleted(photo: String, pos: Int)
    }

}
