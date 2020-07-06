package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import androidx.room.Room;

import com.gmail.fuskerr63.android.library.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private final String DATABASE_NAME = "user_address";

    @Singleton
    @Provides
    public AppDatabase provideDatabase(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, DATABASE_NAME).build();
    }
}
