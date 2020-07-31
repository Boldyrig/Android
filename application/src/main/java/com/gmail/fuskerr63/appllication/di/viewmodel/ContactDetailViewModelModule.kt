package com.gmail.fuskerr63.appllication.di.viewmodel

import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.android.library.viewmodel.dispatchers.ViewModelDispatcher
import com.gmail.fuskerr63.appllication.di.scope.ViewModelScope
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import dagger.Module
import dagger.Provides

@Module
class ContactDetailViewModelModule {

    @ViewModelScope
    @Provides
    fun provideViewModel(
            id: String,
            contactInteractor: ContactInteractor,
            databaseInteractor: DatabaseInteractor,
            notificationInteractor: NotificationInteractor,
            viewModelDispatcher: ViewModelDispatcher
    ) = ContactViewModel(
            id = id,
            contactInteractor = contactInteractor,
            databaseInteractor = databaseInteractor,
            notificationInteractor = notificationInteractor,
            viewModelDispatcher = viewModelDispatcher
    )
}
