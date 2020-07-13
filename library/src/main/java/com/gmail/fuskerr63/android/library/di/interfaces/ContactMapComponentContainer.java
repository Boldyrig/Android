package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.fragment.map.ContactMapFragment;

import io.reactivex.annotations.NonNull;

public interface ContactMapComponentContainer {
    void inject( @NonNull ContactMapFragment contactMapFragment);
}
