package com.gmail.fuskerr63.android.library.recyclerview;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.net.URI;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private ImageView image;
    private TextView name;
    private TextView number;

    public ContactViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        image = (ImageView) itemView.findViewById(R.id.image);
        name = (TextView) itemView.findViewById(R.id.name);
        number = (TextView) itemView.findViewById(R.id.number);
    }

    public void bind(Contact contact) {
        view.setId(contact.getId());
        URI imageUri = contact.getImage();
        if(imageUri == null) {
            image.setImageResource(R.drawable.android_icon); // дефолтная картинка
        } else {
            image.setImageURI(Uri.parse(imageUri.toString()));
        }
        name.setText(contact.getName());
        number.setText(contact.getNumber());
    }
}
