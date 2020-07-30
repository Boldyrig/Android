package com.gmail.fuskerr63.android.library.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Single<List<User>>

    @Query("SELECT * FROM user WHERE contactId = :contactId")
    fun getFlowUserById(contactId: String): Flow<User?>

    @Query("SELECT * FROM user WHERE contactId = :contactId")
    fun getSingleUserById(contactId: String): Single<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User): Completable
}
