package com.gmail.fuskerr63.java.repository

import com.gmail.fuskerr63.java.entity.Contact
import kotlinx.coroutines.flow.Flow

interface ContactDetailsRepository {
    fun getContactById(id: String): Flow<Contact>
}
