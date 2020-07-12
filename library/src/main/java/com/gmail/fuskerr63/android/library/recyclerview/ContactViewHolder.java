package com.gmail.fuskerr63.android.library.recyclerview;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.net.URI;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private final transient View view;
    private final transient ImageView image;
    private final transient TextView name;
    private final transient TextView number;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        number = itemView.findViewById(R.id.number);
    }

    public void bind(@Nullable Contact contact) {
        if (contact != null) {
            view.setId(contact.getId());
            URI imageUri = contact.getImage();
            if (imageUri == null) {
                image.setImageResource(R.mipmap.android_icon); // дефолтная картинка
            } else {
                image.setImageURI(Uri.parse(imageUri.toString()));
            }
            name.setText(contact.getName());
            number.setText(contact.getNumber());
        }
    }
}
