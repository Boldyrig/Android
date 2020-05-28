package com.gmail.fuskerr63.presenter;

import android.content.ContentResolver;

import com.gmail.fuskerr63.fragments.DetailsView;
import com.gmail.fuskerr63.repository.Contact;
import com.gmail.fuskerr63.repository.Repository;

import moxy.MvpPresenter;

public class DetailsPresenter extends MvpPresenter<DetailsView> {
    Repository repository;

    private final String EXTRA_ID = "ID";

    public DetailsPresenter(ContentResolver contentResolver, int id) {
        repository = new Repository(contentResolver);
        repository.getContactById(id, detailsResultListener);
    }

    private DetailsResultListener detailsResultListener = new DetailsResultListener() {
        @Override
        public void onComplete(Contact contact) {
            getViewState().updateDetails(contact);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        repository = null;
    }

    public interface DetailsResultListener {
        void onComplete(Contact contact);
    }
}
