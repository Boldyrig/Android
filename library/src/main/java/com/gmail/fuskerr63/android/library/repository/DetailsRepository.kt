package com.gmail.fuskerr63.android.library.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactInfo
import com.gmail.fuskerr63.java.repository.ContactDetailsRepository
import kotlinx.coroutines.flow.Flow
import java.net.URI
import java.util.*

class DetailsRepository constructor(
        private val contentResolver: ContentResolver?
) : ContactDetailsRepository {
    val PROJECTION: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    )
    val DB_STRING: String = " = ?"
    override fun getContactById(id: Int): Flow<Contact> {
        val cursorContact: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                ContactsContract.Contacts._ID + DB_STRING,
                arrayOf(id.toString()),
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        return loadContactFromCursor(id, cursorContact, contentResolver);
    }

    fun loadContactFromCursor(
            id: Int,
            cursor: Cursor?,
            contentResolver: ContentResolver?) : Flow<Contact> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 1)
        val contact: Contact = Contact(
                -1,
                URI.create(""),
                ContactInfo()
        )
    }
}