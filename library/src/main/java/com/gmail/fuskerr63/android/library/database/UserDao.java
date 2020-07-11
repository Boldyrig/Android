package com.gmail.fuskerr63.android.library.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;

@SuppressWarnings("unused")
@Dao
public interface UserDao {
    @Nullable
    @Query("SELECT * FROM user")
    Single<List<User>> getAll();

    @Nullable
    @Query("SELECT * FROM user WHERE contact_id = :contactId")
    Single<User> getUserByContactId(int contactId);

    @Nullable
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(@Nullable User user);
}
