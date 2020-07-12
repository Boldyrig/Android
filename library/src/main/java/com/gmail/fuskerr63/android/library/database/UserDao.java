package com.gmail.fuskerr63.android.library.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

@SuppressWarnings("unused")
@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    @NonNull
    Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE contact_id = :contactId")
    @NonNull
    Single<User> getUserByContactId(int contactId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @NonNull
    Completable insert(@Nullable User user);
}
