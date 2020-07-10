package com.gmail.fuskerr63.appllication;

import android.app.Application;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.ContactInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.ContextModule;
import com.gmail.fuskerr63.appllication.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.appllication.di.app.DatabaseInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.DatabaseModule;
import com.gmail.fuskerr63.appllication.di.app.DirectionInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.DirectionRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.DirectionRetrofitModule;
import com.gmail.fuskerr63.appllication.di.app.GeoCodeInteractorModule;
import com.gmail.fuskerr63.appllication.di.app.GeoCodeRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.GeoCodeRetrofitModule;
import com.gmail.fuskerr63.appllication.di.app.LocationRepositoryModule;
import com.gmail.fuskerr63.appllication.di.app.RepositoryModule;

public class ContactApplication extends Application implements ContactApplicationContainer {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDependencies();
    }

    private void initDependencies() {
        appComponent = DaggerAppComponent.builder()
                .repositoryModule(new RepositoryModule())
                .geoCodeRetrofitModule(new GeoCodeRetrofitModule())
                .geoCodeInteractorModule(new GeoCodeInteractorModule())
                .geoCodeRepositoryModule(new GeoCodeRepositoryModule())
                .directionRetrofitModule(new DirectionRetrofitModule())
                .directionInteractorModule(new DirectionInteractorModule())
                .directionRepositoryModule(new DirectionRepositoryModule())
                .locationRepositoryModule(new LocationRepositoryModule())
                .databaseModule(new DatabaseModule())
                .databaseInteractorModule(new DatabaseInteractorModule())
                .contactInteractorModule(new ContactInteractorModule())
                .contextModule(new ContextModule(getApplicationContext()))
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