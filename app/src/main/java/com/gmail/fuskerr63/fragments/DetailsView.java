package com.gmail.fuskerr63.fragments;

import com.gmail.fuskerr63.repository.Contact;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface DetailsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void updateDetails(Contact contact);
}
