package com.gmail.fuskerr63.android.library.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import io.reactivex.annotations.Nullable;

@SuppressWarnings("unused")
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    @Nullable public abstract UserDao userDao();
}
