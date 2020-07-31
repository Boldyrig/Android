package com.gmail.fuskerr63.android.library.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.net.URI;
import java.util.List;

public class ContactAdapter extends ListAdapter<Contact, ContactViewHolder> {
    @Nullable
    private final OnItemClickListener onClickListener;

    public ContactAdapter(@Nullable OnItemClickListener onClickListener) {
        super(DIFF_CALLBACK);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.contact, parent, false);
        return new ContactViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public void setContacts(@Nullable List<Contact> contacts) {
        submitList(contacts);
    }

    private static final DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            final String oldNumber = oldItem.getContactInfo().getNumber();
            final String newNumber = newItem.getContactInfo().getNumber();
            final String oldName = oldItem.getContactInfo().getName();
            final String newName = newItem.getContactInfo().getName();
            final URI oldImage = oldItem.getImage();
            final URI newImage = newItem.getImage();
            final boolean oldImageIsNull = oldImage.toString().equals("");
            final boolean newImageIsNull = newImage.toString().equals("");
            return oldNumber.equals(newNumber)
                    && oldName.equals(newName)
                    && !oldImageIsNull ? oldImage.equals(newImage) : newImageIsNull;
        }
    };
}
