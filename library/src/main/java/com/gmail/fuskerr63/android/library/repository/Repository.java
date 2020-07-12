package com.gmail.fuskerr63.android.library.repository;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.entity.ContactInfo;
import com.gmail.fuskerr63.java.repository.ContactRepository;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.net.URI;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class Repository implements ContactRepository {
    private final transient WeakReference<ContentResolver> weakContentResolver;
    private transient ContentResolver contentResolver;
    private static final String SELECTION_SYMBOL = " = ?";

    private final transient String[] projection = {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.PHOTO_URI,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

    @SuppressWarnings("unused")
    public Repository(@Nullable ContentResolver contentResolver) {
        weakContentResolver = new WeakReference<>(contentResolver);
    }

    @NonNull
    @Override
    public Single<List<Contact>> getContacts(@NonNull final String selector) {
        return Single.fromCallable(() -> loadContacts(selector));
    }

    @SuppressWarnings("unused")
    @NonNull
    @Override
    public Single<Contact> getContactById(final int id) {
        return Single.fromCallable(() -> loadContactById(id));
    }

    @Nullable
    private Contact loadContactFromCursor(@Nullable Cursor cursorContact, int id) {
        if (cursorContact == null) {
            return null;
        }
        try {
            if (cursorContact.getCount() > 0) {
                cursorContact.moveToFirst();
                int displayName = Objects.requireNonNull(cursorContact)
                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cursorContact.getString(displayName);
                // собираем картинку
                int photoUri = cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
                String imageString = cursorContact.getString(photoUri);
                URI image;
                if (imageString != null) {
                    image = URI.create(imageString);
                } else {
                    image = URI.create("");
                }
                // собираем номера
                List<String> numbers = new ArrayList<>();
                Cursor cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + SELECTION_SYMBOL,
                        new String[]{String.valueOf(id)},
                        null
                );
                if (cursorPhone != null) {
                    numbers = loadNumbersFromCursor(cursorPhone);
                }
                // собираем почты
                Cursor cursorEmail = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + SELECTION_SYMBOL,
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

                String number1 = numbers.isEmpty() ? "" : numbers.get(0);
                String number2 = numbers.size() < 2 ? "" : numbers.get(1);

                String email1 = emails.isEmpty() ? "" : emails.get(0);
                String email2 = emails.size() < 2 ? "" : emails.get(1);

                ContactInfo contactInfo = new ContactInfo(name, number1, number2, email1, email2);
                return new Contact(id, image, contactInfo, birthday);
            }
        } finally {
            cursorContact.close();
        }
        return null;
    }

    @Nullable
    private List<Contact> loadContactsFromCursor(@Nullable Cursor cursorContact) {
        if (cursorContact == null) {
            return null;
        }
        try {
            if (cursorContact.getCount() > 0) {
                List<Contact> contacts = new ArrayList<>();
                cursorContact.moveToFirst();
                while (!cursorContact.isAfterLast()) {
                    int displayName = Objects.requireNonNull(cursorContact)
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String name = cursorContact.getString(displayName);
                    // собираем картинку
                    int photoUri = cursorContact.getColumnIndex(ContactsContract.Contacts.PHOTO_URI);
                    String imageString = cursorContact.getString(photoUri);
                    URI image;
                    if (imageString != null) {
                        image = URI.create(imageString);
                    } else {
                        image = URI.create("");
                    }
                    int hasPhoneNumer = cursorContact.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    int hasNumber = cursorContact.getInt(hasPhoneNumer);
                    int id = cursorContact.getColumnIndex(ContactsContract.Contacts._ID);
                    int idContact = cursorContact.getInt(id);
                    List<String> numbers;
                    if (hasNumber > 0) {
                        Cursor cursorPhone = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + SELECTION_SYMBOL,
                                new String[]{String.valueOf(idContact)},
                                null
                        );
                        numbers = loadNumbersFromCursor(cursorPhone);
                    } else {
                        numbers = new ArrayList<>();
                    }
                    String number = !numbers.isEmpty() ? numbers.get(0) : null;
                    Contact contact = new Contact(idContact, image, name, number);
                    contacts.add(contact);
                    cursorContact.moveToNext();
                }
                return contacts;
            }
        } finally {
            Objects.requireNonNull(cursorContact).close();
        }
        return null;
    }

    @NonNull
    private List<String> loadNumbersFromCursor(@Nullable Cursor cursorPhone) {
        if (cursorPhone == null) {
            return new ArrayList<>();
        }
        List<String> numbers = new ArrayList<>();
        try {
            if (cursorPhone.getCount() > 0) {
                cursorPhone.moveToFirst();
                while (!cursorPhone.isAfterLast()) {
                    int number = Objects.requireNonNull(cursorPhone)
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    numbers.add(cursorPhone.getString(number));
                    cursorPhone.moveToNext();
                }
            }
        } finally {
            Objects.requireNonNull(cursorPhone).close();
        }
        return numbers;
    }

    @NonNull
    private List<String> loadEmailsFromCursor(@Nullable Cursor cursorEmail) {
        if (cursorEmail == null) {
            return new ArrayList<>();
        }
        try {
            if (cursorEmail.getCount() > 0) {
                cursorEmail.moveToFirst();
                List<String> emails = new ArrayList<>();
                while (!cursorEmail.isAfterLast()) {
                    int address = Objects.requireNonNull(cursorEmail)
                            .getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                    emails.add(cursorEmail.getString(address));
                    cursorEmail.moveToNext();
                }
                return emails;
            }
        } finally {
            Objects.requireNonNull(cursorEmail).close();
        }
        return new ArrayList<>();
    }

    @Nullable
    private Calendar loadBirthdayFromCursor(@Nullable Cursor cursorBirthday) {
        if (cursorBirthday == null) {
            return null;
        }
        try {
            if (cursorBirthday.getCount() > 0) {
                cursorBirthday.moveToFirst();
                List<Calendar> birthday = new ArrayList<>();
                while (!cursorBirthday.isAfterLast()) {
                    int startDate = Objects.requireNonNull(cursorBirthday)
                            .getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE);
                    String date = cursorBirthday.getString(startDate);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
                    Calendar calendar = new GregorianCalendar();
                    try {
                        calendar.setTime(Objects.requireNonNull(format.parse(date)));
                    } catch (ParseException e) {
                        calendar.setTimeInMillis(0);
                    }
                    birthday.add(calendar);
                }
                return birthday.get(0);
            }
        } finally {
            Objects.requireNonNull(cursorBirthday).close();
        }
        return null;
    }

    @Nullable
    private List<Contact> loadContacts(@Nullable final String selector) {
        contentResolver = weakContentResolver.get();
        if (contentResolver == null) {
            return null;
        }
        String selection;
        if (selector != null && !selector.equals("")) {
            selection = ContactsContract.Contacts.DISPLAY_NAME + " LIKE '%" + selector + "%'";
        } else {
            selection = "";
        }
        Cursor cursorContact = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                selection,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        return loadContactsFromCursor(cursorContact);
    }

    @Nullable
    private Contact loadContactById(final int id) {
        contentResolver = weakContentResolver.get();
        if (contentResolver == null) {
            return null;
        }
        Cursor cursorContact = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                ContactsContract.Contacts._ID + SELECTION_SYMBOL,
                new String[] {String.valueOf(id)},
                ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        return loadContactFromCursor(cursorContact, id);
    }
}
