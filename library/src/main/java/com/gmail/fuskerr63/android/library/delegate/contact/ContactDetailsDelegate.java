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
    @Nullable
    private final View view;

    public ContactDetailsDelegate(@Nullable View view) {
        this.view = view;
    }

    public void showDetails(@Nullable Contact contact) {
        if (view != null && contact != null) {
            URI image = contact.getImage();
            if (image == null || image.toString().equals("")) {
                ((ImageView) view.findViewById(R.id.image)).setImageResource(R.mipmap.android_icon);
            } else {
                ((ImageView) view.findViewById(R.id.image)).setImageURI(Uri.parse(image.toString()));
            }
            ((TextView) view.findViewById(R.id.name)).setText(contact.getContactInfo().getName());
            ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getContactInfo().getNumber());
            ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getContactInfo().getNumber2());
            ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getContactInfo().getEmail());
            ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getContactInfo().getEmail2());
            ((TextView) view.findViewById(R.id.address_contact)).setText(contact.getAddress());
            Calendar birthday = contact.getBirthday();
            if (birthday != null && contact.getBirthday().get(Calendar.YEAR) != 1) {
                String birthdayText = birthday.get(Calendar.DATE) + " "
                        + birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " "
                        + birthday.get(Calendar.YEAR);
                ((TextView) view.findViewById(R.id.birthday_contact)).setText(birthdayText);
            }
        }
    }
}
