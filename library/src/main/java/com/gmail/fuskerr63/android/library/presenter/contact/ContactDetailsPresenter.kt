package com.gmail.fuskerr63.android.library.presenter.contact

import com.gmail.fuskerr63.android.library.view.ContactDetailsView
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseAdapter
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import java.lang.Exception
import javax.inject.Inject

class ContactDetailsPresenter @Inject constructor(
        private val contactInteractor: ContactInteractor?,
        private val databaseAdapter: DatabaseAdapter?,
        private val notificationInteractor: NotificationInteractor?
) : MvpPresenter<ContactDetailsView?>() {
    fun showDetails(id: Int, notificationCancel: String, notificationSend: String) {
        if (contactInteractor != null) {
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    contactInteractor.getContactById(id)
                            .map { contact: Contact ->
                                    var newContact = Contact(
                                            contact.id,
                                            contact.image,
                                            contact.contactInfo,
                                            contact.birthday,
                                            contact.address
                                    )
                                    databaseAdapter!!.getUserById(contact.id)
                                        .collect { location: ContactLocation ->
                                            newContact = Contact(
                                                    contact.id,
                                                    contact.image,
                                                    contact.contactInfo,
                                                    contact.birthday,
                                                    location.address
                                            )
                                        }
                                    newContact
                            }
                            .flowOn(Dispatchers.IO)
                            .collect { contact: Contact? ->
                                viewState?.updateDetails(contact)
                                if (notificationInteractor!!.getNotificationStatusForContact(contact).isAlarmUp) {
                                    viewState?.setTextButton(notificationCancel)
                                } else {
                                    viewState?.setTextButton(notificationSend)
                                }
                            }
                } catch (error: Exception) {
                    viewState?.showMessageToast(error.message)
                }
            }
        }
    }

    fun onClickBirthday(
            contact: Contact?,
            notificationCancel: String?,
            notificationSend: String?) {
        if (notificationInteractor != null
                && notificationInteractor.toggleNotificationForContact(contact).isAlarmUp) {
            viewState?.setTextButton(notificationCancel)
        } else {
            viewState?.setTextButton(notificationSend)
        }
    }
}