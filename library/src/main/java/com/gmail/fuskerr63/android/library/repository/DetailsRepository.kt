package com.gmail.fuskerr63.android.library.repository

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactInfo
import com.gmail.fuskerr63.java.repository.ContactDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.URI
import java.text.ParseException
import java.text.SimpleDateFormat
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
        val cursorContact: Cursor? = contentResolver?.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                ContactsContract.Contacts._ID + DB_STRING,
                arrayOf(id.toString()),
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        return loadContactFromCursor(id, cursorContact);
    }

    fun loadContactFromCursor(
            id: Int,
            cursor: Cursor?): Flow<Contact> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 1)
        var contact: Contact = Contact(
                -1,
                URI.create(""),
                ContactInfo("", "", "", "", ""),
                calendar,
                ""
        )
        return flow {
            try {
                if (cursor != null) {
                    val displayName: Int = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    val photoUri: Int = cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                    val hasPhoneNumber: Int = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    if (cursor.count > 0) {
                        cursor.moveToFirst();
                        var image = URI.create("")
                        val name = cursor.getString(displayName)
                        var numbers = arrayOf<String>()

                        val imageString = cursor.getString(photoUri)
                        if (imageString != null) {
                            image = URI.create(imageString)
                        }

                        val hasNumber = cursor.getInt(hasPhoneNumber)
                        if (hasNumber > 0) {
                            val cursorPhone = contentResolver?.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    arrayOf<String>(ContactsContract.CommonDataKinds.Phone.NUMBER),
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + DB_STRING,
                                    arrayOf(id.toString()),
                                    null
                            )
                            numbers = loadNumbersFromCursor(cursorPhone)
                        }

                        val cursorEmail = contentResolver?.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + DB_STRING,
                                arrayOf(id.toString()),
                                null
                        )
                        val emails = loadEmailFromCursor(cursorEmail)

                        val cursorBirthday = contentResolver?.query(
                                ContactsContract.Data.CONTENT_URI,
                                arrayOf(ContactsContract.CommonDataKinds.Event.START_DATE),
                                ContactsContract.Data.CONTACT_ID + " = ? AND "
                                        + ContactsContract.CommonDataKinds.Event.TYPE + " = "
                                        + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                                arrayOf(id.toString()),
                                null
                        )
                        val birthday = loadBirthdayFromCursor(cursorBirthday)

                        val number1 = if (!numbers.isEmpty()) numbers[0] else ""
                        val number2 = if (numbers.count() > 0) numbers[1] else ""

                        val email1 = if (!emails.isEmpty()) emails[0] else ""
                        val email2 = if (emails.count() > 0) emails[1] else ""

                        contact = Contact(
                                id,
                                image,
                                ContactInfo(name, number1, number2, email1, email2),
                                birthday,
                                ""
                        )
                    }
                }
            } finally {
                cursor?.close()
            }
            emit(contact);
        }
    }

    fun loadNumbersFromCursor(cursorPhone: Cursor?): Array<String> {
        val numbers = emptyArray<String>()
        try {
            if (cursorPhone != null) {
                val number = cursorPhone?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var i = 0
                if (cursorPhone.count > 0) {
                    cursorPhone.moveToFirst()
                    while (!cursorPhone.isAfterLast) {
                        numbers[i++] = cursorPhone.getString(number)
                        cursorPhone.moveToNext()
                    }
                }
            }
        } finally {
            cursorPhone?.close()
        }
        return numbers
    }

    fun loadEmailFromCursor(cursorEmail: Cursor?): Array<String> {
        val emails = emptyArray<String>()
        try {
            if (cursorEmail != null) {
                val address = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                if (cursorEmail.count > 0) {
                    cursorEmail.moveToFirst()
                    var i = 0
                    while (!cursorEmail.isAfterLast) {
                        emails[i++] = cursorEmail.getString(address)
                        cursorEmail.moveToNext()
                    }
                }
            }
        } finally {
            cursorEmail?.close()
        }
        return emails
    }

    fun loadBirthdayFromCursor(cursorBirthday: Cursor?): Calendar {
        var birthday = GregorianCalendar()
        birthday.set(Calendar.YEAR, 1)
        try {
            if (cursorBirthday != null) {
                val startDate = cursorBirthday.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
                val format = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
                if (cursorBirthday.count > 0) {
                    cursorBirthday.moveToFirst()
                    while (!cursorBirthday.isAfterLast) {
                        val date = cursorBirthday.getString(startDate)
                        try {
                            birthday.time = format.parse(date)
                        } catch (e: ParseException) {
                            birthday.set(Calendar.YEAR, 1)
                        }
                        cursorBirthday.moveToNext()
                    }
                }
            }
        } finally {
            cursorBirthday?.close()
        }
        return birthday
    }
}