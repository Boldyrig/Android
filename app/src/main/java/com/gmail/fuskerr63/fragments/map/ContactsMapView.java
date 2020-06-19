package com.gmail.fuskerr63.fragments.map;

import com.gmail.fuskerr63.database.User;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactsMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void printMarkers(List<User> users);
}
