package com.gmail.fuskerr63.android.library.di.interfaces

import androidx.fragment.app.Fragment
import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment
import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.android.library.viewmodel.factory.ContactViewModelFactory

interface ViewModelComponentContainer {
    fun inject(fragment: Fragment)

    val viewModel: ContactViewModel
    val factory: ViewModelComponentFactory

    interface Factory : ViewModelComponentFactory {
        override fun create(
            target: ContactDetailsFragment,
            id: Int) : ViewModelComponentContainer
    }
}