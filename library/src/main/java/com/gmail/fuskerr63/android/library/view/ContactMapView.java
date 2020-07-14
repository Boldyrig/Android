package com.gmail.fuskerr63.android.library.view;

import com.google.android.gms.maps.model.LatLng;

import io.reactivex.annotations.NonNull;
import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactMapView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void moveTo(@NonNull LatLng latLng);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void replaceMarker(@NonNull LatLng latLng, @NonNull String title);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setProgressStatus(boolean show);
}
