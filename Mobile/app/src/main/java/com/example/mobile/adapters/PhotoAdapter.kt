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

class PhotoAdapter(private val photos: MutableList<String>, private val deleteCallback: (Int) -> Unit, private val deleteButton: ImageView): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    @SuppressLint("NotifyDataSetChanged")
    inner class ViewHolder(view: View, deleteButton: ImageView) : RecyclerView.ViewHolder(view) {
        var pic: ImageView = itemView.findViewById(R.id.photoImageView)
        init {
            itemView.setOnClickListener {
                println("PhotoAdapter.ViewHolder.onClick")
            }
            deleteButton.setOnClickListener {
                // add callback
                print(adapterPosition)
                deleteCallback(adapterPosition)
                // notify dupa callback, altfel daca se sterge inainte de callback nu mai avem elementul
//                notifyDataSetChanged()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_photo_item, parent, false)
        return ViewHolder(view, deleteButton)
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

}
