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
import kotlinx.coroutines.Job
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
    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    @FlowPreview
    fun showDetails(id: Int, notificationCancel: String?, notificationSend: String?) {
        if (id != -1) {
            try {
                launch {
                    contactInteractor.getContactById(id)
                        .flatMapMerge { contact: Contact ->
                            databaseModel.getFlowUserById(contact.id)
                                .map { value: ContactLocation ->
                                    Contact(
                                        contact.id,
                                        contact.image,
                                        contact.contactInfo,
                                        contact.birthday,
                                        value.address
                                    )
                                }
                        }
                        .flowOn(Dispatchers.IO)
                        .collect { contact: Contact? ->
                            viewState?.updateDetails(contact)
                            if (notificationInteractor.getNotificationStatusForContact(contact).isAlarmUp) {
                                viewState?.setTextButton(notificationCancel)
                            } else {
                                viewState?.setTextButton(notificationSend)
                            }
                        }
                }
            } catch (error: Exception) {
                Log.d("TAG", error.message ?: "")
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
