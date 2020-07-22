package com.gmail.fuskerr63.android.library.delegate.contact

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.library.R
import java.util.*

class ContactDetailsDelegate(private val view: View?) {
    fun showDetails(contact: Contact?) {
        if (view != null && contact != null) {
            if (contact.image.toString() != "") {
                view.findViewById<ImageView>(R.id.image).setImageURI(Uri.parse(contact.image.toString()))
            } else {
                view.findViewById<ImageView>(R.id.image).setImageResource(R.mipmap.android_icon)
            }
            view.findViewById<TextView>(R.id.name).text = contact.contactInfo.name
            view.findViewById<TextView>(R.id.number1_contact).text = contact.contactInfo.number
            view.findViewById<TextView>(R.id.number2_contact).text = contact.contactInfo.number2
            view.findViewById<TextView>(R.id.email1_contact).text = contact.contactInfo.email
            view.findViewById<TextView>(R.id.email2_contact).text = contact.contactInfo.email2
            view.findViewById<TextView>(R.id.address_contact).text = contact.address
            if (contact.birthday.get(Calendar.YEAR) != 1) {
                val day = contact.birthday.get(Calendar.DATE)
                val month = contact.birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                val year = contact.birthday.get(Calendar.YEAR)
                val birthdayText = "$day $month $year"
                view.findViewById<TextView>(R.id.birthday_contact).text = birthdayText
            }
        }
    }
}