package com.gmail.fuskerr63.java.repository

import com.gmail.fuskerr63.java.entity.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


interface ContactDetailsRepository {
    fun getContactById(id: Int) : Flow<Contact>
}