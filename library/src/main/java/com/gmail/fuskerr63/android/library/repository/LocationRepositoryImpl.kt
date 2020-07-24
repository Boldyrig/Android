package com.gmail.fuskerr63.android.library.repository

import com.gmail.fuskerr63.android.library.database.AppDatabase
import com.gmail.fuskerr63.android.library.database.User
import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.entity.Position
import com.gmail.fuskerr63.java.repository.LocationRepository
import io.reactivex.Single
import kotlinx.coroutines.flow.map

class LocationRepositoryImpl(private val appDatabase: AppDatabase) : LocationRepository {
    override fun getAll(): Single<List<ContactLocation>> =
        appDatabase.userDao().getAll()
            .map { users: List<User> ->
                val contactLocations = mutableListOf<ContactLocation>()
                for (user: User in users) {
                    contactLocations.add(
                        with(user) {
                            ContactLocation(
                                contactId,
                                name,
                                Position(latitude, longitude),
                                address
                            )
                        }
                    )
                }
                contactLocations
            }

    override fun getFlowUserById(contactId: Int) =
        appDatabase.userDao().getFlowUserById(contactId)
            .map { user: User ->
                with(user) {
                    ContactLocation(
                        contactId,
                        name,
                        Position(latitude, longitude),
                        address
                    )
                }
            }

    override fun getSingleUserById(contactId: Int) =
        appDatabase.userDao().getSingleUserById(contactId)
            .map { user: User ->
                with(user) {
                    ContactLocation(
                        contactId,
                        name,
                        Position(latitude, longitude),
                        address
                    )
                }
            }

    override fun insert(contactLocation: ContactLocation) =
        appDatabase.userDao().insert(
            with(contactLocation) {
                User(
                    id,
                    name,
                    position.latitude,
                    position.longitude,
                    address
                )
            }
        )
}
