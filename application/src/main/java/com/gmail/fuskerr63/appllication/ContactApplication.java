package com.gmail.fuskerr63.appllication;

import android.app.Application;

<<<<<<< HEAD:app/src/main/java/com/gmail/fuskerr63/app/ContactApplication.java
import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.di.app.DatabaseModule;
import com.gmail.fuskerr63.di.app.RepositoryModule;
import com.gmail.fuskerr63.di.app.RetrofitModule;
=======
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.appllication.di.app.AppComponent;
import com.gmail.fuskerr63.appllication.di.app.ContactModelModule;
import com.gmail.fuskerr63.appllication.di.app.DaggerAppComponent;
import com.gmail.fuskerr63.appllication.di.app.RepositoryModule;
>>>>>>> clean-architecture:application/src/main/java/com/gmail/fuskerr63/appllication/ContactApplication.java

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
<<<<<<< HEAD:app/src/main/java/com/gmail/fuskerr63/app/ContactApplication.java
                .retrofitModule(new RetrofitModule())
                .databaseModule(new DatabaseModule(getApplicationContext()))
=======
                .contactModelModule(new ContactModelModule())
>>>>>>> clean-architecture:application/src/main/java/com/gmail/fuskerr63/appllication/ContactApplication.java
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