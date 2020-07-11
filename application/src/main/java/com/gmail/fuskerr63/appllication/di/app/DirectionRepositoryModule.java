package com.gmail.fuskerr63.appllication.di.app;

import com.gmail.fuskerr63.android.library.network.DirectionRetrofit;
import com.gmail.fuskerr63.android.library.repository.DirectionRepositoryImpl;
import com.gmail.fuskerr63.java.repository.DirectionRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.annotations.Nullable;

@Module
public class DirectionRepositoryModule {
    @SuppressWarnings("unused")
    @Nullable
    @Singleton
    @Provides
    public DirectionRepository provideDirectionRepository(@Nullable DirectionRetrofit directionRetrofit) {
        return new DirectionRepositoryImpl(directionRetrofit);
    }
}
