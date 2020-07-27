package com.gmail.fuskerr63.android.library.presenter.contact

import android.util.Log
import com.gmail.fuskerr63.android.library.view.ContactDetailsView
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ContactDetailsPresenter @Inject constructor(
    private val contactInteractor: ContactInteractor,
    private val databaseModel: DatabaseInteractor,
    private val notificationInteractor: NotificationInteractor
) : MvpPresenter<ContactDetailsView?>(), CoroutineScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main

    @FlowPreview
    fun showDetails(id: Int) {
        if (id != -1) {
            @Suppress("TooGenericExceptionCaught")
            try {
                launch {
                    contactInteractor.getContactById(id)
                        .flatMapMerge { contact: Contact ->
                            databaseModel.getFlowUserById(contact.id)
                                .map { value: ContactLocation? ->
                                    createContactWithLocation(contact, value)
                                }
                        }
                        .flowOn(Dispatchers.IO)
                        .collect { contact: Contact? ->
                            viewState?.updateDetails(contact)
                            viewState?.setTextButton(notificationInteractor.getNotificationStatusForContact(contact))
                        }
                }
            } catch (error: Exception) {
                Log.d("TAG", error.message ?: "")
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
    fun onClickBirthday(contact: Contact?) {
        viewState?.setTextButton(notificationInteractor.toggleNotificationForContact(contact))
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
