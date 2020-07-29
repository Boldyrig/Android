package com.gmail.fuskerr63.android.library.di.interfaces

import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment
import com.gmail.fuskerr63.android.library.viewmodel.factory.ContactViewModelFactory

interface ViewModelComponentFactory {
    fun create(
        target: ContactDetailsFragment,
        id: Int
    ) : ViewModelComponentContainer
}