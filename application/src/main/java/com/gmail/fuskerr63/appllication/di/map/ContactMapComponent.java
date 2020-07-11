package com.gmail.fuskerr63.appllication.di.map;

import com.gmail.fuskerr63.android.library.di.interfaces.ContactMapComponentContainer;
import com.gmail.fuskerr63.android.library.fragment.map.ContactMapFragment;
import com.gmail.fuskerr63.appllication.di.scope.ContactMapScope;

import dagger.Subcomponent;
import io.reactivex.annotations.Nullable;

@ContactMapScope
@Subcomponent(modules = {ContactMapModule.class})
public interface ContactMapComponent extends ContactMapComponentContainer {
    @SuppressWarnings("unused")
    @Override
    void inject(@Nullable ContactMapFragment contactMapFragment);
}
