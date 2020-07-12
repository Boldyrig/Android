package com.gmail.fuskerr63.android.library.delegate.contacts;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.fuskerr63.android.library.recyclerview.ContactAdapter;
import com.gmail.fuskerr63.android.library.recyclerview.ContactDecorator;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.util.List;

import io.reactivex.annotations.Nullable;

public class ContactListDelegate {
    private final transient View view;
    private transient ContactAdapter contactAdapter;

    public ContactListDelegate(@Nullable View view) {
        this.view = view;
    }

    public void onCreateView(@Nullable Context context, float px) {
        if (context instanceof View.OnClickListener) {
            contactAdapter = new ContactAdapter((View.OnClickListener) context);
            RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(contactAdapter);
            recyclerView.addItemDecoration(new ContactDecorator((int) px));
        }
    }

    public void updateList(@Nullable final List<Contact> contacts) {
        if (contactAdapter != null) {
            contactAdapter.setContacts(contacts);
        }
    }
}
