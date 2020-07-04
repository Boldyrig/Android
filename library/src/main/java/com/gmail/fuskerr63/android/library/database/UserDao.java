package com.gmail.fuskerr63.android.library.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE contact_id = :contactId")
    Single<User> getUserByContactId(int contactId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(User user);

    @Delete
    void delete(User user);
}
