package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import androidx.room.Room;

import com.gmail.fuskerr63.android.library.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class DatabaseModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public AppDatabase provideAppDatabase(@Nullable Context applicationContext) {
        String databaseName = "user_address";
        return Room.databaseBuilder(applicationContext, AppDatabase.class, databaseName).build();
    }
}
