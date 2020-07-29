package com.gmail.fuskerr63.appllication.di.viewmodel

import androidx.fragment.app.Fragment
import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentContainer
import com.gmail.fuskerr63.android.library.di.interfaces.ViewModelComponentFactory
import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment
import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.appllication.di.scope.ViewModelScope
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@ViewModelScope
@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent : ViewModelComponentContainer {
    override fun inject(fragment: Fragment)

    override fun getViewModel(): ContactViewModel

    @Subcomponent.Factory
    interface Factory : ViewModelComponentFactory{
        override fun create(
            @BindsInstance target: ContactDetailsFragment,
            @BindsInstance id: Int): ViewModelComponent
    }
}
