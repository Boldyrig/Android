package com.gmail.fuskerr63.di.app;

import android.content.Context;

import androidx.room.Room;

import com.gmail.fuskerr63.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private AppDatabase db;

    private final String DATABASE_NAME = "user_address";

    public DatabaseModule(Context applicationContext) {
        db = Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    public AppDatabase provideDatabase() {
        return db;
    }
}
