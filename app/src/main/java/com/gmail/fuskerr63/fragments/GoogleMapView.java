package com.gmail.fuskerr63.fragments;

import com.gmail.fuskerr63.database.User;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface GoogleMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showCurrentLocation();
    @StateStrategyType(SingleStateStrategy.class)
    void printMarkers(List<User> users);
}
