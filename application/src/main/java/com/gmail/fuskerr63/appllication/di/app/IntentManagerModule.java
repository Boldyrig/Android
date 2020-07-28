package com.gmail.fuskerr63.appllication.di.app;

import android.content.Context;

import com.gmail.fuskerr63.android.library.MainActivity;
import com.gmail.fuskerr63.android.library.birthday.IntentManager;
import com.gmail.fuskerr63.android.library.receiver.ContactReceiver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class IntentManagerModule {
    @Nullable
    private final Class<MainActivity> mainActivityClass;

    @Nullable
    private final Class<ContactReceiver> receiverClass;

    public IntentManagerModule(
            @Nullable Class<MainActivity> mainActivityClass,
            @Nullable Class<ContactReceiver> receiverClass
    ) {
        this.mainActivityClass = mainActivityClass;
        this.receiverClass = receiverClass;
    }


    @Nullable
    @Singleton
    @Provides
    public IntentManager provideBirthdayNorification(@Nullable Context context) {
        return new IntentManager(context, mainActivityClass, receiverClass);
    }
}
