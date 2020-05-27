package com.gmail.fuskerr63.presenter;

import android.content.ContentResolver;

import com.gmail.fuskerr63.fragments.ContactListView;
import com.gmail.fuskerr63.repository.Contact;
import com.gmail.fuskerr63.repository.Repository;

import java.util.ArrayList;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ContactListPresenter extends MvpPresenter<ContactListView> {
    Repository repository;

    private final String EXTRA_ID = "ID";

    public ContactListPresenter(ContentResolver contentResolver) {
        repository = new Repository(contentResolver);
        repository.getContacts(listResultListener);
    }

    private ListResultListener listResultListener = new ListResultListener() {
        @Override
        public void onComplete(ArrayList<Contact> contacts) {
            getViewState().updateList(contacts);
        }
    };

    public interface ListResultListener {
        void onComplete(ArrayList<Contact> contacts);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository = null;
    }
}
