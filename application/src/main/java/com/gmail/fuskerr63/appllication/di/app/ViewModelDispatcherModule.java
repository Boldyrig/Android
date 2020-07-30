package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.viewmodel.dispatchers.ViewModelDispatcher;
import com.gmail.fuskerr63.android.library.viewmodel.dispatchers.ViewModelDispatcherProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.NonNull;

@Module
public class ViewModelDispatcherModule {
    @NonNull
    @Singleton
    @Provides
    public ViewModelDispatcher provideViewModelDispatcher() {
        return new ViewModelDispatcherProvider();
    }
}
