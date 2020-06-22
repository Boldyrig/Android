package com.gmail.fuskerr63.di.contact;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.di.scope.ContactDetailsScope;
import com.gmail.fuskerr63.presenter.DetailsPresenter;
import com.gmail.fuskerr63.repository.Repository;

import dagger.Module;
import dagger.Provides;

@Module
public class ContactModule {
    private int id;

    public ContactModule(int id) {
        this.id = id;
    }

    @ContactDetailsScope
    @Provides
    DetailsPresenter provideDetailsPresenter(Repository repository, AppDatabase db) {
        return new DetailsPresenter(repository, id, db);
    }
}
