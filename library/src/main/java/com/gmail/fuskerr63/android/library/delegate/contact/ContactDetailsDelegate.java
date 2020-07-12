package com.gmail.fuskerr63.android.library.delegate.contact;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.net.URI;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.annotations.Nullable;

public class ContactDetailsDelegate {
    private final transient View view;

    public ContactDetailsDelegate(@Nullable View view) {
        this.view = view;
    }

    public void showDetails(@Nullable Contact contact) {
        if (view != null && contact != null) {
            URI image = contact.getImage();
            if (!image.toString().equals("")) {
                ((ImageView) view.findViewById(R.id.image)).setImageURI(Uri.parse(image.toString()));
            }
            ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
            ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
            ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
            ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
            ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
            ((TextView) view.findViewById(R.id.address_contact)).setText(contact.getAddress());
            if (contact.getBirthday().get(Calendar.YEAR) != 0) {
                Calendar birthday = contact.getBirthday();
                String birthdayText = birthday.get(Calendar.DATE) + " "
                        + birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " "
                        + birthday.get(Calendar.YEAR);
                ((TextView) view.findViewById(R.id.birthday_contact)).setText(birthdayText);
            }
        }
    }
}
