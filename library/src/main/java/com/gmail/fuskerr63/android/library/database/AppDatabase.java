package com.gmail.fuskerr63.android.library.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import io.reactivex.annotations.NonNull;

@SuppressWarnings("unused")
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    @NonNull
    public abstract UserDao userDao();
}
