package com.gmail.fuskerr63.android.library.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.multidex.BuildConfig
import com.gmail.fuskerr63.android.library.viewmodel.dispatchers.ViewModelDispatcher
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import com.gmail.fuskerr63.java.interactor.NotificationStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ContactViewModel(
    private val id: String,
    private val contactInteractor: ContactInteractor,
    private val databaseInteractor: DatabaseInteractor,
    private val notificationInteractor: NotificationInteractor,
    private val viewModelDispatcher: ViewModelDispatcher
) : ViewModel(), CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + viewModelDispatcher.getMainDispatcher()

    @FlowPreview
    private val contact by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<Contact>()
    }
    private val birthdayStatus by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<NotificationStatus>()
    }
    private val loadingStatus by lazy(LazyThreadSafetyMode.NONE) {
        MutableLiveData<Boolean>(false)
    }

    @FlowPreview
    fun getContact(): LiveData<Contact> = run {
        loadContact()
        contact
    }

    fun getBirthdayStatus(): LiveData<NotificationStatus> = birthdayStatus

    fun getLoadingStatus(): LiveData<Boolean> = loadingStatus

    @FlowPreview
    private fun loadContact() {
        try {
            launch {
                contactInteractor.getContactById(id)
                    .flatMapMerge { contact: Contact ->
                        loadContactLocation(contact)
                    }
                    .flowOn(viewModelDispatcher.getIODispatcher())
                    .collect { newContact: Contact? ->
                        contact.value = newContact
                        birthdayStatus.value = notificationInteractor.getNotificationStatusForContact(newContact)
                        loadingStatus.value = false
                    }
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.d("TAG", e.message)
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

    private fun loadContactLocation(contact: Contact) =
        databaseInteractor.getFlowUserById(id)
            .map { contactLocation: ContactLocation? ->
                createContactWithLocation(
                    contact = contact,
                    location = contactLocation
                )
            }

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    fun onClickBirthday() {
        birthdayStatus.value = notificationInteractor.toggleNotificationForContact(contact.value)
    }
}
