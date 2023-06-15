package com.example.mobile.helpers
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity

// Inside your Fragment or Activity

public class Alerts {

    // folosim functia alert pentru a afisa un mesaj
    companion object {
        fun alert(activity: FragmentActivity, title: String, message: String) {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }


}