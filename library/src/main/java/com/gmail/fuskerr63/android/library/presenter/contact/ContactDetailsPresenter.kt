package com.gmail.fuskerr63.android.library.presenter.contact

import android.util.Log
import androidx.multidex.BuildConfig
import com.gmail.fuskerr63.android.library.view.ContactDetailsView
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactInfo
import com.gmail.fuskerr63.java.entity.ContactLocation
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import moxy.MvpPresenter
import javax.inject.Inject

class ContactDetailsPresenter @Inject constructor(
        private val contactInteractor: ContactInteractor?,
        private val databaseInteractor: DatabaseInteractor?,
        private val notificationInteractor: NotificationInteractor?
) : MvpPresenter<ContactDetailsView?>() {
    private val disposable = CompositeDisposable()
    fun showDetails(id: Int, notificatinCancel: String, notificationSend: String) {
        if (contactInteractor != null) {
            disposable.add(contactInteractor.getContactById(id)
                    .subscribeOn(Schedulers.io())
                    .flatMap { contact: Contact ->
                        databaseInteractor!!.getUserByContactId(contact.id)
                                .map { user: ContactLocation ->
                                    val contactInfo = ContactInfo(
                                            contact.contactInfo.name,
                                            contact.contactInfo.number,
                                            contact.contactInfo.number2,
                                            contact.contactInfo.email,
                                            contact.contactInfo.email2
                                    )
                                    Contact(
                                            contact.id,
                                            contact.image,
                                            contactInfo,
                                            contact.birthday,
                                            user.address
                                    )
                                }
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { viewState!!.loadingStatus(true) }
                    .doFinally { viewState!!.loadingStatus(false) }
                    .subscribe(
                            { contact: Contact? ->
                                viewState!!.updateDetails(contact)
                                if (notificationInteractor!!.getNotificationStatusForContact(contact).isAlarmUp) {
                                    viewState!!.setTextButton(notificatinCancel)
                                } else {
                                    viewState!!.setTextButton(notificationSend)
                                }
                            }
                    ) { error: Throwable ->
                        if (BuildConfig.DEBUG) {
                            Log.d("TAG", error.message)
                        }
                    })
        }
    }

    fun onClickBirthday(
            contact: Contact?,
            notificationCancel: String?,
            notificationSend: String?) {
        if (notificationInteractor != null
                && notificationInteractor.toggleNotificationForContact(contact).isAlarmUp) {
            viewState!!.setTextButton(notificationCancel)
        } else {
            viewState!!.setTextButton(notificationSend)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

}