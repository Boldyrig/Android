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
    private transient final View view;
    private transient final ImageView image;
    private transient final TextView name;
    private transient final TextView number;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        image = itemView.findViewById(R.id.image);
        name = itemView.findViewById(R.id.name);
        number = itemView.findViewById(R.id.number);
    }

    public void bind(@Nullable Contact contact) {
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
