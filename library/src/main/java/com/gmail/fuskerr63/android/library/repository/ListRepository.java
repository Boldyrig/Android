package com.gmail.fuskerr63.android.library.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.repository.ContactListRepository;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
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

    @NonNull
    @Override
    public Single<Contact> getContactById(final int id) {
        return Single.fromCallable(() -> loadContactById(id));
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

    private Contact loadContactById(final int id) {
        ContentResolver contentResolver = weakContentResolver.get();
        if (contentResolver == null) {
            return null;
        }
        Cursor cursorContact = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                PROJECTION,
                ContactsContract.Contacts._ID + DB_STRING,
                new String[] {String.valueOf(id)},
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        return loadContactFromCursor(cursorContact, id, contentResolver);
    }

    private Contact loadContactFromCursor(Cursor cursorContact, int id, ContentResolver contentResolver) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1);
        Contact contact = new Contact(
                -1,
                URI.create(""),
                new ContactInfo("", "", "", "", ""),
                calendar,
                ""
        ); // пустой контакт
        try {
            int displayName = cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int photoUri = cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
            int hasPhoneNumber = cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            if (cursorContact.getCount() > 0) {
                cursorContact.moveToFirst();
                URI image = URI.create("");
                String name = cursorContact.getString(displayName);
                List<String> numbers = new ArrayList<>();
                // собираем картинку
                String imageString = cursorContact.getString(photoUri);
                if (imageString != null) {
                    image = URI.create(imageString);
                }
                // собираем номера
                int hasNumber = cursorContact.getInt(hasPhoneNumber);
                if (hasNumber > 0) {
                    Cursor cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + DB_STRING,
                            new String[]{String.valueOf(id)},
                            null
                    );
                    numbers = loadNumbersFromCursor(cursorPhone);
                }
                // собираем почты
                Cursor cursorEmail = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + DB_STRING,
                        new String[]{String.valueOf(id)},
                        null
                );
                List<String> emails = loadEmailsFromCursor(cursorEmail);
                // собираем день рождения
                Cursor cursorBirthday = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Event.START_DATE},
                        ContactsContract.Data.CONTACT_ID + " = ? AND "
                                + ContactsContract.CommonDataKinds.Event.TYPE + " = "
                                + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                        new String[]{String.valueOf(id)},
                        null
                );
                Calendar birthday = loadBirthdayFromCursor(cursorBirthday);

                String number1 = !numbers.isEmpty() ? numbers.get(0) : "";
                String number2 = numbers.size() > 1 ? numbers.get(1) : "";

                String email1 = !emails.isEmpty() ? emails.get(0) : "";
                String email2 = emails.size() > 1 ? emails.get(1) : "";
                contact = new Contact(
                        id,
                        image,
                        new ContactInfo(name, number1, number2, email1, email2),
                        birthday,
                        "");
            }
        } finally {
            cursorContact.close();
        }
        return contact;
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

    private List<String> loadEmailsFromCursor(Cursor cursorEmail) {
        List<String> emails = new ArrayList<>();
        try {
            int address = cursorEmail.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
            if (cursorEmail.getCount() > 0) {
                cursorEmail.moveToFirst();
                while (!cursorEmail.isAfterLast()) {
                    emails.add(cursorEmail.getString(address));
                    cursorEmail.moveToNext();
                }
            }
        } finally {
            cursorEmail.close();
        }
        return emails;
    }

    private Calendar loadBirthdayFromCursor(Cursor cursorBirthday) {
        Calendar birthday = new GregorianCalendar();
        birthday.set(Calendar.YEAR, 1);
        try {
            int startDate = cursorBirthday.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
            if (cursorBirthday.getCount() > 0) {
                cursorBirthday.moveToFirst();
                while (!cursorBirthday.isAfterLast()) {
                    String date = cursorBirthday.getString(startDate);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                    Calendar calendar = new GregorianCalendar();
                    try {
                        calendar.setTime(format.parse(date));
                    } catch (ParseException e) {
                        calendar.setTimeInMillis(0);
                    }
                    birthday = calendar;
                    cursorBirthday.moveToNext();
                }
            }
        } finally {
            cursorBirthday.close();
        }
        return birthday;
    }
}
