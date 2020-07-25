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

    override fun getContactById(id: Int) =
        loadContactFromCursor(
            id,
            createCursor(
                projection,
                ContactsContract.Contacts._ID + DB_STRING,
                arrayOf(id.toString())
            )
        )

    private fun loadContactFromCursor(id: Int, cursor: Cursor?) =
        flow {
            cursor?.use {
                if (it.count > 0) {
                    it.moveToFirst()
                    var image = URI.create(it.getString(it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)))
                    val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    lateinit var numbers: List<String>

                    val hasNumber = it.getInt(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    if (hasNumber > 0) {
                        numbers = loadNumbersFromCursor(
                            createCursor(
                                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + DB_STRING,
                                arrayOf(id.toString())
                            )
                        )
                    }
                    val emails = loadEmailFromCursor(
                        createCursor(
                            arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + DB_STRING,
                            arrayOf(id.toString())
                        )
                    )
                    val birthday = loadBirthdayFromCursor(
                        createCursor(
                            arrayOf(ContactsContract.CommonDataKinds.Event.START_DATE),
                            ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                    ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                            arrayOf(id.toString())
                        )
                    )

                    val number1 = if (numbers.isNotEmpty()) numbers[0] else ""
                    val number2 = if (numbers.count() > 1) numbers[1] else ""

                    val email1 = if (emails.isNotEmpty()) emails[0] else ""
                    val email2 = if (emails.count() > 1) emails[1] else ""

                    val contact = Contact(
                        id,
                        image,
                        ContactInfo(name, number1, number2, email1, email2),
                        birthday,
                        ""
                    )
                    emit(contact)
                }
            }
        }

    private fun createCursor(fields: Array<String>, selection: String, args: Array<String>) =
        contentResolver?.query(
            ContactsContract.Data.CONTENT_URI,
            fields,
            selection,
            args,
            null
        )

    private fun loadNumbersFromCursor(cursorPhone: Cursor?) = mutableListOf<String>().apply {
        cursorPhone.use {
            it?.moveToFirst()
            while (it != null && !it.isAfterLast) {
                add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)))
                it.moveToNext()
            }
        }
    }

    private fun loadEmailFromCursor(cursorEmail: Cursor?) =
            mutableListOf<String>().apply {
                cursorEmail.use {
                    it?.moveToFirst()
                    while (it != null &&!it.isAfterLast) {
                        add(it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS)))
                        it.moveToNext()
                    }
                }
            }

    private fun loadBirthdayFromCursor(cursorBirthday: Cursor?) =
        GregorianCalendar().apply {
            set(Calendar.YEAR, 1)
            cursorBirthday.use {
                val format = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
                it?.moveToFirst()
                val dateString = it?.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE))
                try {
                    val date = format.parse(dateString ?: "")
                    time = date ?: Date()
                } catch (e: ParseException) {
                    set(Calendar.YEAR, 1)
                }
            }
        }
}
