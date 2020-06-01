package com.gmail.fuskerr63.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;

import com.gmail.fuskerr63.presenter.ContactListPresenter;
import com.gmail.fuskerr63.presenter.DetailsPresenter;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Repository {
    WeakReference<ContentResolver> weakContentResolver;

    public Repository(ContentResolver contentResolver) {
        weakContentResolver = new WeakReference(contentResolver);
    }

    public void getContacts(final ContactListPresenter.ListResultListener listResultListener, @Nullable final String selector) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = weakContentResolver.get();
                if(contentResolver == null) return;
                String selection = null;
                if(selector != null && !selector.equals("")) {
                    selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE \'%" + selector + "%\'";
                }
                Cursor cursorContact = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[] {
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.PHOTO_URI,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                        },
                        selection,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME + " ASC");
                ArrayList<Contact> contacts = new ArrayList();
                try {
                    int displayName = cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    int photoUri = cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
                    int id = cursorContact.getColumnIndex(ContactsContract.Contacts._ID);
                    int hasPhoneNumer = cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

                    if(cursorContact.getCount() > 0) {
                        cursorContact.moveToFirst();
                        while(!cursorContact.isAfterLast()) {
                            Uri image = null;
                            String name = cursorContact.getString(displayName);
                            ArrayList<String> numbers = new ArrayList<String>();
                            // собираем картинку
                            String imageString = cursorContact.getString(photoUri);
                            if(imageString != null) {
                                image = Uri.parse(imageString);
                            }
                            // берем id контакта
                            int idContact = cursorContact.getInt(id);
                            // собираем номера
                            int hasNumber = cursorContact.getInt(hasPhoneNumer);
                            if(hasNumber > 0) {
                                Cursor cursorPhone = contentResolver.query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER },
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                        new String[] { String.valueOf(idContact) },
                                        null
                                );
                                try {
                                    int number = cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                                    if(cursorPhone.getCount() > 0) {
                                        cursorPhone.moveToFirst();
                                        while(!cursorPhone.isAfterLast()) {
                                            numbers.add(cursorPhone.getString(number));
                                            cursorPhone.moveToNext();
                                        }
                                    }
                                } finally {
                                    cursorPhone.close();
                                }
                            }
                            String number = numbers.size() > 0 ? numbers.get(0) : null;
                            Contact contact = new Contact(idContact, image, name, number);
                            contacts.add(contact);
                            cursorContact.moveToNext();
                        }
                    }
                } finally {
                    cursorContact.close();
                }
                listResultListener.onComplete(contacts);
            }
        }).start();
    }

    public void getContactById(final int id, final DetailsPresenter.DetailsResultListener resultListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver contentResolver = weakContentResolver.get();
                if(contentResolver == null) return;
                Cursor cursorContact = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        new String[] {
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.PHOTO_URI,
                                ContactsContract.Contacts.DISPLAY_NAME,
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                        },
                        ContactsContract.Contacts._ID + " = ?",
                        new String[] { String.valueOf(id) },
                        ContactsContract.Contacts.DISPLAY_NAME + " ASC");
                Contact contact = null;
                try {
                    int displayName = cursorContact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    int photoUri = cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
                    int hasPhoneNumber = cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);

                    if (cursorContact.getCount() > 0) {
                        cursorContact.moveToFirst();
                        Uri image = null;
                        String name = cursorContact.getString(displayName);
                        ArrayList<String> numbers = new ArrayList<String>();
                        ArrayList<String> emails = new ArrayList<String>();
                        Calendar birthday = null;
                        // собираем картинку
                        String imageString = cursorContact.getString(photoUri);
                        if (imageString != null) {
                            image = Uri.parse(imageString);
                        }
                        // собираем номера
                        int hasNumber = cursorContact.getInt(hasPhoneNumber);
                        if (hasNumber > 0) {
                            Cursor cursorPhone = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{String.valueOf(id)},
                                    null
                            );
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
                        }
                        // собираем почты
                        Cursor cursorEmail = contentResolver.query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                new String[]{String.valueOf(id)},
                                null
                        );
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
                        // собираем день рождения
                        Cursor cursorBirthday = contentResolver.query(
                                ContactsContract.Data.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Event.START_DATE},
                                ContactsContract.Data.CONTACT_ID + " = ? AND " +
                                        ContactsContract.CommonDataKinds.Event.TYPE + " = " + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                                new String[]{String.valueOf(id)},
                                null
                        );
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
                        String number1 = numbers.size() > 0 ? numbers.get(0) : null;
                        String number2 = numbers.size() > 1 ? numbers.get(1) : null;

                        String email1 = emails.size() > 0 ? emails.get(0) : null;
                        String email2 = emails.size() > 1 ? emails.get(1) : null;
                        contact = new Contact(id, image, name, number1, number2, email1, email2, birthday);
                    }
                } finally {
                    cursorContact.close();
                }
                resultListener.onComplete(contact);
            }
        }).start();
    }
}
