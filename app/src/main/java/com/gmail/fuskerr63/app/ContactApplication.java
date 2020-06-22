package com.gmail.fuskerr63.app;

import android.app.Application;

import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.di.app.DatabaseModule;
import com.gmail.fuskerr63.di.app.RepositoryModule;
import com.gmail.fuskerr63.di.app.RetrofitModule;

public class ContactApplication extends Application {
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
                .build();
    }

    public AppComponent getAppComponent() {
        if(appComponent == null) {
            initDependencies();
        }
        return appComponent;
    }
}
