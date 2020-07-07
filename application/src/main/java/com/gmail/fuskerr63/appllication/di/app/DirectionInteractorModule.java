package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.android.library.network.interactor.DirectionInteractor;
import com.gmail.fuskerr63.android.library.network.interactor.DirectionModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DirectionInteractorModule {
    @Singleton
    @Provides
    public DirectionInteractor provideDirectionRetrofit(DirectionRetrofit directionRetrofit) {
        return new DirectionModel(directionRetrofit);
    }
}
