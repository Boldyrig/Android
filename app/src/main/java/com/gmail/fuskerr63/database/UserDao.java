package com.gmail.fuskerr63.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    Single<List<User>> getAll();

    @Query("SELECT * FROM user WHERE contact_id = :contactId")
    Single<User> getUserByContactId(int contactId);

    @Query("INSERT INTO user VALUES(:contactId, :latitude, :longitude)")
    Completable insert(int contactId, double latitude, double longitude);

    @Delete
    void delete(User user);
}
