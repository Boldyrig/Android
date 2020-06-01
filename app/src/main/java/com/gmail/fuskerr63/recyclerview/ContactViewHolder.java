package com.gmail.fuskerr63.recyclerview;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.repository.Contact;

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
        Uri imageUri = contact.getImage();
        if(imageUri == null) {
            image.setImageResource(R.drawable.android_icon); // дефолтная картинка
        } else {
            image.setImageURI(imageUri);
        }
        name.setText(contact.getName());
        number.setText(contact.getNumber());
    }
}
