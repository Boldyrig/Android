package com.gmail.fuskerr63.appllication.di.viewmodel

import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentContainer
import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentFactory
import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.appllication.di.scope.ViewModelScope
import dagger.BindsInstance
import dagger.Subcomponent

@ViewModelScope
@Subcomponent(modules = [ContactDetailViewModelModule::class])
interface ViewModelComponent : ViewModelComponentContainer {

    override fun getViewModel(): ContactViewModel

    @Subcomponent.Factory
    interface Factory : ViewModelComponentFactory {
        override fun create(@BindsInstance id: Int): ViewModelComponent
    }
}
