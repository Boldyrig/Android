package com.gmail.fuskerr63.android.library.di.interfaces;

import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment;

import io.reactivex.annotations.NonNull;

@SuppressWarnings({"EmptyMethod", "unused"})
public interface ContactComponentContainer {
    void inject(@NonNull ContactDetailsFragment contactDetailsFragment);
}
