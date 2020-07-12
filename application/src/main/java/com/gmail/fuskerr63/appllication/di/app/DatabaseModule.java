package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import androidx.room.Room;

import com.gmail.fuskerr63.android.library.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class DatabaseModule {
    @SuppressWarnings("unused")
    @NonNull
    @Singleton
    @Provides
    public AppDatabase provideAppDatabase(@NonNull Context applicationContext) {
        String databaseName = "user_address";
        return Room.databaseBuilder(applicationContext, AppDatabase.class, databaseName).build();
    }
}
