package com.gmail.fuskerr63.android.library.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.repository.ContactListRepository;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.net.URI;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ListRepository implements ContactListRepository {
    @NonNull
    private final WeakReference<ContentResolver> weakContentResolver;

    private static final String DB_STRING = " = ?";

    private static final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    public ListRepository(@NonNull ContentResolver contentResolver) {
        weakContentResolver = new WeakReference(contentResolver);
    }

    @NonNull
    @Override
    public Single<List<Contact>> getContacts(@Nullable final String selector) {
        return Single.fromCallable(() -> loadContacts(selector));
    };

    private List<Contact> loadContacts(@Nullable final String selector) {
        ContentResolver contentResolver = weakContentResolver.get();
        if (contentResolver == null) {
            return null;
        }
        String selection = null;
        if (selector != null && !selector.equals("")) {
            selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE \'%" + selector + "%\'";
        }
        Cursor cursorContact = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                selection,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        return loadContactsFromCursor(cursorContact, contentResolver);
    }

    private List<Contact> loadContactsFromCursor(Cursor cursorContact, ContentResolver contentResolver) {
        List<Contact> contacts = new ArrayList<>();
        try {
            int displayName = cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int photoUri = cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
            int id = cursorContact.getColumnIndex(ContactsContract.Contacts._ID);
            int hasPhoneNumer = cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

            if (cursorContact.getCount() > 0) {
                cursorContact.moveToFirst();
                while (!cursorContact.isAfterLast()) {
                    URI image = URI.create("");
                    String name = cursorContact.getString(displayName);
                    List<String> numbers = new ArrayList<>();
                    // собираем картинку
                    String imageString = cursorContact.getString(photoUri);
                    if (imageString != null) {
                        image = URI.create(imageString);
                    }
                    // берем id контакта
                    int idContact = cursorContact.getInt(id);
                    // собираем номера
                    int hasNumber = cursorContact.getInt(hasPhoneNumer);
                    if (hasNumber > 0) {
                        Cursor cursorPhone = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + DB_STRING,
                                new String[] {String.valueOf(idContact)},
                                null
                        );
                        numbers = loadNumbersFromCursor(cursorPhone);
                    }
                    String number = !numbers.isEmpty() ? numbers.get(0) : "";
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, 1);
                    Contact contact = new Contact(
                            idContact,
                            image,
                            new ContactInfo(
                                    name,
                                    number,
                                    "",
                                    "",
                                    ""),
                            calendar,
                            ""
                            );
                    contacts.add(contact);
                    cursorContact.moveToNext();
                }
            }
        } finally {
            cursorContact.close();
        }
        return contacts;
    }

    private List<String> loadNumbersFromCursor(Cursor cursorPhone) {
        List<String> numbers = new ArrayList<>();
        try {
            int number = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            if (cursorPhone.getCount() > 0) {
                cursorPhone.moveToFirst();
                while (!cursorPhone.isAfterLast()) {
                    numbers.add(cursorPhone.getString(number));
                    cursorPhone.moveToNext();
                }
            }
        } finally {
            cursorPhone.close();
        }
        return numbers;
    }
}
