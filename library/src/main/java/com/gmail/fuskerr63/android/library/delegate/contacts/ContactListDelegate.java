package com.gmail.fuskerr63.android.library.delegate.contacts;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.fuskerr63.android.library.recyclerview.ContactAdapter;
import com.gmail.fuskerr63.android.library.recyclerview.ContactDecorator;
import com.gmail.fuskerr63.android.library.recyclerview.OnClickListener;
import com.gmail.fuskerr63.android.library.recyclerview.OnItemClickListener;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.util.List;

import io.reactivex.annotations.Nullable;

public class ContactListDelegate implements OnItemClickListener {
    @Nullable
    private final View view;
    private final OnClickListener onClickListener;
    private ContactAdapter contactAdapter;
    private List<Contact> contacts;

    public ContactListDelegate(@Nullable View view, @Nullable OnClickListener onClickListener) {
        this.view = view;
        this.onClickListener = onClickListener;
    }

    public void onCreateView(@Nullable Context context, float px) {
        contactAdapter = new ContactAdapter(this);
        if (view != null) {
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(contactAdapter);
            recyclerView.addItemDecoration(new ContactDecorator((int) px));
        }
    }

    public void updateList(@Nullable final List<Contact> contacts) {
        this.contacts = contacts;
        if (contactAdapter != null) {
            contactAdapter.setContacts(contacts);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (onClickListener != null) {
            onClickListener.onClick(contacts.get(position).getId());
        }
    }
}
