package com.gmail.fuskerr63.appllication.di.viewmodel

import androidx.lifecycle.ViewModelProviders
import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment
import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.android.library.viewmodel.factory.ContactViewModelFactory
import com.gmail.fuskerr63.appllication.di.scope.ViewModelScope
import com.gmail.fuskerr63.java.interactor.ContactInteractor
import com.gmail.fuskerr63.java.interactor.DatabaseInteractor
import com.gmail.fuskerr63.java.interactor.NotificationInteractor
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ViewModelModule {
    @ViewModelScope
    @Provides
    fun provideViewModel(
        target: ContactDetailsFragment,
        factory: ContactViewModelFactory
    ) = ViewModelProviders.of(target, factory)[ContactViewModel::class.java]

    @ViewModelScope
    @Provides
    fun provideContactViewModel(
            id: Int,
            contactInteractor: ContactInteractor,
            databaseInteractor: DatabaseInteractor,
            notificationInteractor: NotificationInteractor) =
        ContactViewModelFactory(
            id,
            contactInteractor,
            databaseInteractor,
            notificationInteractor
        )
}