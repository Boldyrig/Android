package com.gmail.fuskerr63.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;

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

    public void setContacts(ArrayList<Contact> contacts) {
        submitList(contacts);
    }

    public static final DiffUtil.ItemCallback<Contact> DIFF_CALLBACK = new DiffUtil.ItemCallback<Contact>() {
        @Override
        public boolean areItemsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Contact oldItem, @NonNull Contact newItem) {
            return oldItem.getNumber().equals(newItem.getNumber()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getImage().equals(newItem.getImage());
        }
    };
}
