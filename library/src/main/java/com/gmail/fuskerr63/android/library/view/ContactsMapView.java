package com.gmail.fuskerr63.android.library.view;

import com.gmail.fuskerr63.android.library.database.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactsMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void printMarkers(List<User> users);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setProgressStatus(boolean show);
    @StateStrategyType(SingleStateStrategy.class)
    void clearDirection();
    @StateStrategyType(SingleStateStrategy.class)
    void printDirection(List<LatLng> points, List<LatLng> bounds);
    @StateStrategyType(SingleStateStrategy.class)
    void showErrorToast(String message);
}
