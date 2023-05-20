package com.example.mobile.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R

class ProfilePropertyAdapter(private var propertyList: List<Pair<String, String>>, private var callback: (List<Pair<String, String>>) -> Unit) :
    RecyclerView.Adapter<ProfilePropertyAdapter.ProfilePropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePropertyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_profile_property, parent, false)
        return ProfilePropertyViewHolder(view)
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ProfilePropertyViewHolder, position: Int) {
        val property = propertyList[position]
        holder.itemView.apply {
            println("ProfilePropertyAdapter.onBindViewHolder")
            val propertyNameTextView = this.findViewById<TextView>(R.id.propname)
            val propertyValueTextView = this.findViewById<TextView>(R.id.propvalue)
            val editPropertyValueEditText = this.findViewById<EditText>(R.id.editpropvalue)
            val editImageImageView = this.findViewById<ImageView>(R.id.editimage)
            val doneImageImageView = this.findViewById<ImageView>(R.id.doneimage)

            // Set initial text for propertyNameTextView and propertyValueTextView
            propertyNameTextView.text = property.first
            propertyValueTextView.text = property.second

            // Set initial visibility
            propertyNameTextView.visibility = View.VISIBLE
            propertyValueTextView.visibility = View.VISIBLE
            editPropertyValueEditText.visibility = View.GONE
            editImageImageView.visibility = View.VISIBLE
            doneImageImageView.visibility = View.GONE

            // Set click listener on editImageImageView
            editImageImageView.setOnClickListener {
                // Toggle visibility of views
                propertyNameTextView.visibility = View.GONE
                propertyValueTextView.visibility = View.GONE
                editPropertyValueEditText.visibility = View.VISIBLE
                editImageImageView.visibility = View.GONE
                doneImageImageView.visibility = View.VISIBLE

                // Set initial text for editPropertyValueEditText
                editPropertyValueEditText.setText(property.second)
            }

            // Set click listener on doneImageImageView
            doneImageImageView.setOnClickListener {
                // Toggle visibility of views
                propertyNameTextView.visibility = View.VISIBLE
                propertyValueTextView.visibility = View.VISIBLE
                editPropertyValueEditText.visibility = View.GONE
                editImageImageView.visibility = View.VISIBLE
                doneImageImageView.visibility = View.GONE

                // Update the property value with the text from editPropertyValueEditText
                val propertyValue = editPropertyValueEditText.text.toString()
                propertyValueTextView.text = propertyValue
                val updatedPropertyList = propertyList.mapIndexed { index, pair ->
                    if (index == position) {
                        Pair(pair.first, propertyValue)
                    } else {
                        pair
                    }
                }
                propertyList = updatedPropertyList
                callback(updatedPropertyList)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return propertyList.size
    }

    class ProfilePropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
