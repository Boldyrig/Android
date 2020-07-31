package com.gmail.fuskerr63.java.interactor

import com.gmail.fuskerr63.java.entity.ContactLocation
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface DatabaseInteractor {
    fun getAll(): Single<List<ContactLocation>>
    fun getFlowUserById(contactId: String): Flow<ContactLocation?>
    fun getSingleUserById(contactId: String): Single<ContactLocation>
    fun insert(contactLocation: ContactLocation): Completable
}
