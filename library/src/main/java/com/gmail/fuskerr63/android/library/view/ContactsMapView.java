package com.gmail.fuskerr63.android.library.view;

import com.gmail.fuskerr63.java.entity.ContactLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import io.reactivex.annotations.Nullable;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactsMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void printMarkers(@Nullable List<ContactLocation> contactLocations);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setProgressStatus(boolean show);
    @StateStrategyType(SingleStateStrategy.class)
    void clearDirection();
    @StateStrategyType(SingleStateStrategy.class)
    void printDirection(@Nullable List<LatLng> points, @Nullable List<LatLng> bounds);
    @StateStrategyType(SingleStateStrategy.class)
    void showErrorToast(@Nullable String message);
}
