package com.gmail.fuskerr63.android.library.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.gmail.fuskerr63.library.R;
import com.gmail.fuskerr63.java.Contact;

import java.net.URI;
import java.util.List;

public class ContactAdapter extends ListAdapter<Contact, ContactViewHolder> {
    private View.OnClickListener onClickListener;


    public ContactAdapter(View.OnClickListener onClickListener) {
        super(DIFF_CALLBACK);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.contact, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        if(onClickListener != null){
            contactViewHolder.itemView.setOnClickListener(onClickListener);
        }
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setContacts(List<Contact> contacts) {
        submitList(contacts);
    }

    public static final DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            final String oldNumber = oldItem.getNumber();
            final String newNumber = newItem.getNumber();
            final String oldName = oldItem.getName();
            final String newName = newItem.getName();
            final URI oldImage = oldItem.getImage();
            final URI newImage = newItem.getImage();
            final boolean oldImageIsNull = oldImage == null;
            final boolean newImageIsNull = newImage == null;
            return oldNumber.equals(newNumber) &&
                    oldName.equals(newName) &&
                    !oldImageIsNull ? oldImage.equals(newImage) : newImageIsNull ? true : false;
        }
    };
}

