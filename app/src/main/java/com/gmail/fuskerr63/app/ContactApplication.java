package com.gmail.fuskerr63.app;

import android.app.Application;

import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.di.app.RepositoryModule;

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
                .build();
        appComponent.inject(this);
    }

    public AppComponent getAppComponent() {
        if(appComponent == null) {
            initDependencies();
        }
        return appComponent;
    }
}
