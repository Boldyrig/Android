package com.gmail.fuskerr63.android.library.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import com.gmail.fuskerr63.java.interactor.NotificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ContactViewModel(
    private val contactInteractor: ContactInteractor,
    private val databaseInteractor: DatabaseInteractor,
    private val notificationInteractor: NotificationInteractor
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    private val contact = MutableLiveData<Contact>()
    private val birthdayStatus = MutableLiveData<NotificationStatus>()
    private val loadingStatus = MutableLiveData<Boolean>(false)

    @FlowPreview
    fun getContact(id: Int): LiveData<Contact> =
        contact.also {
            loadingStatus.value = true
            loadContact(id)
        }

    fun getBirthdayStatus() = birthdayStatus

    fun getLoadingStatus() = loadingStatus

    @FlowPreview
    private fun loadContact(id: Int) {
        if (id != -1) {
            launch {
                contactInteractor.getContactById(id)
                    .flatMapMerge { contact: Contact ->
                        databaseInteractor.getFlowUserById(id)
                            .map { contactLocation: ContactLocation? ->
                                createContactWithLocation(contact, contactLocation)
                            }
                    }
                    .flowOn(Dispatchers.IO)
                    .collect { newContact: Contact? ->
                        contact.value = newContact
                        birthdayStatus.value = notificationInteractor.getNotificationStatusForContact(newContact)
                        loadingStatus.value = false
                    }
            }
        }
    }

    private fun createContactWithLocation(contact: Contact, location: ContactLocation?) =
        with(contact) {
            Contact(
                id,
                image,
                contactInfo,
                birthday,
                location?.address ?: ""
            )
        }

    fun onClickBirthday(contact: Contact) {
        birthdayStatus.value = notificationInteractor.toggleNotificationForContact(contact)
    }
}
