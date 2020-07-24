package com.gmail.fuskerr63.android.library.presenter.contact

import com.gmail.fuskerr63.android.library.view.ContactDetailsView
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moxy.MvpPresenter
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ContactDetailsPresenter @Inject constructor(
    private val contactInteractor: ContactInteractor,
    private val databaseModel: DatabaseInteractor,
    private val notificationInteractor: NotificationInteractor
) : MvpPresenter<ContactDetailsView?>(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    fun showDetails(id: Int, notificationCancel: String?, notificationSend: String?) {
        if (contactInteractor != null && id != -1) {
            launch {
                try {
                    contactInteractor.getContactById(id)
                        .map { contact: Contact ->
                            databaseModel.getFlowUserById(contact.id)
                                    .flatMapMerge {
                                        flowOf(Contact(
                                                contact.id,
                                                contact.image,
                                                contact.contactInfo,
                                                contact.birthday,
                                                it.address
                                        ))
                                    }}.single()
                        .flowOn(Dispatchers.IO)
                        .collect { contact: Contact? ->
                            viewState?.updateDetails(contact)
                            if (notificationInteractor.getNotificationStatusForContact(contact).isAlarmUp) {
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
        notificationSend: String?
    ) {
        if (notificationInteractor.toggleNotificationForContact(contact).isAlarmUp) {
            viewState?.setTextButton(notificationCancel)
        } else {
            viewState?.setTextButton(notificationSend)
        }
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
