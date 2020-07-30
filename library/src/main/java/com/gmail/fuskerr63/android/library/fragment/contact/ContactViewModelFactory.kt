package com.gmail.fuskerr63.android.library.fragment.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer

@Suppress("UNCHECKED_CAST")
class ContactViewModelFactory(
    private val id: String,
    private val container: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        container.viewModelComponentFactory().create(id).getViewModel() as T
}
