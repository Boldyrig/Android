package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.java.interactor.DirectionInteractor;
import com.gmail.fuskerr63.java.interactor.DirectionModel;
import com.gmail.fuskerr63.java.repository.DirectionRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class DirectionInteractorModule {

    @Nullable
    @Singleton
    @Provides
    public DirectionInteractor provideDirectionRetrofit(@Nullable DirectionRepository directionRepository) {
        return new DirectionModel(directionRepository);
    }
}
