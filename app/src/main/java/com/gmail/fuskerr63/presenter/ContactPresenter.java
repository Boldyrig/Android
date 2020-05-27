package com.gmail.fuskerr63.presenter;

import android.content.ContentResolver;
import android.content.Intent;

import com.gmail.fuskerr63.androidlesson.MainView;
import com.gmail.fuskerr63.repository.Contact;
import com.gmail.fuskerr63.repository.Repository;

import java.util.ArrayList;

import moxy.InjectViewState;
import moxy.MvpPresenter;

@InjectViewState
public class ContactPresenter extends MvpPresenter<MainView> {
    Repository repository;

    private final String EXTRA_ID = "ID";

    public ContactPresenter(ContentResolver contentResolver, Intent intent) {
        repository = new Repository(contentResolver);
        if(intent != null && intent.hasExtra(EXTRA_ID)) {
            int id = intent.getExtras().getInt(EXTRA_ID);
            onIntent(id);
        } else {
            getViewState().showList();
        }
    }

    public void onLoadListFragment() {
        if(repository != null) {
            repository.getContacts(listResultListener);
        }
    }

    public void onLoadDetailsFragment(int id) {
        if(repository != null) {
            repository.getContactById(id, detailsResultListener);
        }
    }

    public void onListItemClick(int id) {
        getViewState().showDetails(id);
    }

    public void onIntent(int id) {
        getViewState().showDetails(id);
    }

    private ListResultListener listResultListener = new ListResultListener() {
        @Override
        public void onComplete(ArrayList<Contact> contacts) {
            getViewState().updateList(contacts);
        }
    };

    private DetailsResultListener detailsResultListener = new DetailsResultListener() {
        @Override
        public void onComplete(Contact contact) {
            getViewState().updateDetails(contact);
        }
    };

    public interface ListResultListener {
        void onComplete(ArrayList<Contact> contacts);
    }

    public interface DetailsResultListener {
        void onComplete(Contact contact);
    }
}
