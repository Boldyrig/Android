package com.gmail.fuskerr63.appllication;

import android.app.Application;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.ContactInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.DatabaseModule;
import com.gmail.fuskerr63.appllication.di.app.RepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.RetrofitModule;

public class ContactApplication extends Application implements ContactApplicationContainer {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        appComponent = DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule(getApplicationContext()))
                .retrofitModule(new RetrofitModule())
                .databaseModule(new DatabaseModule(getApplicationContext()))
                .contactModelModule(new ContactInteractorModule())
                .build();
    }

    @Override
    public AppComponent getAppComponent() {
        if(appComponent == null) {
            initDependencies();
        }
        return appComponent;
    }
}