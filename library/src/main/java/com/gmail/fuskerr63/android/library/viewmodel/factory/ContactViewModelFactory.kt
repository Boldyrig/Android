package com.gmail.fuskerr63.android.library.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor

class ContactViewModelFactory(
    val id: Int,
    val contactInteractor: ContactInteractor,
    val databaseInteractor: DatabaseInteractor,
    val notificationInteractor: NotificationInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            Int::class.java,
            ContactInteractor::class.java,
            DatabaseInteractor::class.java,
            NotificationInteractor::class.java
        ).newInstance(id, contactInteractor, databaseInteractor, notificationInteractor)
    }
}
