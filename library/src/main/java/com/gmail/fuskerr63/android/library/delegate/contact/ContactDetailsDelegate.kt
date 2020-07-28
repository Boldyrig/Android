package com.gmail.fuskerr63.android.library.delegate.contact

import android.net.Uri
import android.view.View
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.library.R
import kotlinx.android.synthetic.main.fragment_contact_details.view.address_contact
import kotlinx.android.synthetic.main.fragment_contact_details.view.birthday_contact
import kotlinx.android.synthetic.main.fragment_contact_details.view.email1_contact
import kotlinx.android.synthetic.main.fragment_contact_details.view.email2_contact
import kotlinx.android.synthetic.main.fragment_contact_details.view.image
import kotlinx.android.synthetic.main.fragment_contact_details.view.name
import kotlinx.android.synthetic.main.fragment_contact_details.view.number1_contact
import kotlinx.android.synthetic.main.fragment_contact_details.view.number2_contact
import java.util.Calendar
import java.util.Locale

class ContactDetailsDelegate(private val view: View?) {
    fun showDetails(contact: Contact?) {
        if (view != null && contact != null) {
            with(view) {
                if (contact.image.toString() != "") {
                    image.setImageURI(Uri.parse(contact.image.toString()))
                } else {
                    image.setImageResource(R.mipmap.android_icon)
                }
                name.text = contact.contactInfo.name
                number1_contact.text = contact.contactInfo.number
                number2_contact.text = contact.contactInfo.number2
                email1_contact.text = contact.contactInfo.email
                email2_contact.text = contact.contactInfo.email2
                address_contact.text = contact.address
                if (contact.birthday.get(Calendar.YEAR) != 1) {
                    val day = contact.birthday.get(Calendar.DATE)
                    val month = contact.birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    val year = contact.birthday.get(Calendar.YEAR)
                    val birthdayText = "$day $month $year"
                    birthday_contact.text = birthdayText
                }
            }
        }
    }
}
