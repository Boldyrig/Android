package com.gmail.fuskerr63.java.interactor

import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.repository.LocationRepository

class DatabaseModel(private val locationRepository: LocationRepository) : DatabaseInteractor {
    override fun getAll() = locationRepository.getAll()

    override fun getFlowUserById(contactId: Int) = locationRepository.getFlowUserById(contactId)

    override fun getSingleUserById(contactId: Int) = locationRepository.getSingleUserById(contactId)

    override fun insert(contactLocation: ContactLocation) = locationRepository.insert(contactLocation)
}
