package com.gmail.fuskerr63.fragments.map;

import com.gmail.fuskerr63.database.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void moveTo(LatLng position);
}
