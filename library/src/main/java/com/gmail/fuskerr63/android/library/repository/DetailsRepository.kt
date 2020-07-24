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
import kotlin.collections.ArrayList

class DetailsRepository(private val contentResolver: ContentResolver?) : ContactDetailsRepository {
    val projection: Array<String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.PHOTO_URI,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

    companion object {
        const val DB_STRING: String = " = ?"
    }

    override fun getContactById(id: Int): Flow<Contact> {
        val cursorContact: Cursor? = contentResolver?.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                ContactsContract.Contacts._ID + DB_STRING,
                arrayOf(id.toString()),
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )
        return loadContactFromCursor(id, cursorContact)
    }

    private fun loadContactFromCursor(
        id: Int,
        cursor: Cursor?
    ): Flow<Contact> {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, 1)
        var contact = Contact(
            -1,
            URI.create(""),
            ContactInfo("", "", "", "", ""),
            calendar,
            ""
        )
        return flow {
            cursor?.use {
                val displayName: Int = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val photoUri: Int = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
                val hasPhoneNumber: Int = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                if (it.count > 0) {
                    it.moveToFirst()
                    var image = URI.create("")
                    val name = it.getString(displayName)
                    lateinit var numbers: List<String>

                    val imageString = it.getString(photoUri)
                    if (imageString != null) {
                        image = URI.create(imageString)
                    }

                    val hasNumber = it.getInt(hasPhoneNumber)
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
                        ContactsContract.Data.CONTACT_ID + " = ? AND " +
                            ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                        arrayOf(id.toString()),
                        null
                    )
                    val birthday = loadBirthdayFromCursor(cursorBirthday)

                    val number1 = if (numbers.isNotEmpty()) numbers[0] else ""
                    val number2 = if (numbers.count() > 1) numbers[1] else ""

                    val email1 = if (emails.isNotEmpty()) emails[0] else ""
                    val email2 = if (emails.count() > 1) emails[1] else ""

                    contact = Contact(
                        id,
                        image,
                        ContactInfo(name, number1, number2, email1, email2),
                        birthday,
                        ""
                    )
                }
            }
            emit(contact)
        }
    }

    private fun loadNumbersFromCursor(cursorPhone: Cursor?) =
            mutableListOf<String>().apply {
                cursorPhone.use { cursorPhone ->
                    if (cursorPhone != null) {
                        val number = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        if (cursorPhone.count > 0) {
                            cursorPhone.moveToFirst()
                            while (!cursorPhone.isAfterLast) {
                                add(cursorPhone.getString(number))
                                cursorPhone.moveToNext()
                            }
                        }
                    }
            }
    }

    private fun loadEmailFromCursor(cursorEmail: Cursor?): ArrayList<String> {
        val emails = ArrayList<String>()
        cursorEmail.use {
            if (cursorEmail != null) {
                val address = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)
                if (cursorEmail.count > 0) {
                    cursorEmail.moveToFirst()
                    while (!cursorEmail.isAfterLast) {
                        emails.add(cursorEmail.getString(address))
                        cursorEmail.moveToNext()
                    }
                }
            }
        }
        return emails
    }

    private fun loadBirthdayFromCursor(cursorBirthday: Cursor?): Calendar {
        val birthday = GregorianCalendar()
        birthday.set(Calendar.YEAR, 1)
        cursorBirthday.use {
            if (it != null) {
                val startDate = it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
                val format = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
                if (it.count > 0) {
                    it.moveToFirst()
                    while (!it.isAfterLast) {
                        val date = it.getString(startDate)
                        try {
                            val time = format.parse(date)
                            if (time != null) {
                                birthday.time = time
                            }
                        } catch (e: ParseException) {
                            birthday.set(Calendar.YEAR, 1)
                        }
                        it.moveToNext()
                    }
                }
            }
        }
        return birthday
    }
}
