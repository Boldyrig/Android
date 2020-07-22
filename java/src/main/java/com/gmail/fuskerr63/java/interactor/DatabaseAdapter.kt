package com.gmail.fuskerr63.java.interactor

import com.gmail.fuskerr63.java.entity.ContactLocation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DatabaseAdapter(
        private val databaseInteractor: DatabaseInteractor
) {
    fun getUserById(id: Int): Flow<ContactLocation> {
        return flow {
            lateinit var contactLocation: ContactLocation;
            databaseInteractor.getUserByContactId(id)
                    .subscribe { location -> contactLocation = location }
            emit(contactLocation)
        }
    }
}