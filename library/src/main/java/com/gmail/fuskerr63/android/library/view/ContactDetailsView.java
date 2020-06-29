package com.gmail.fuskerr63.android.library.view;

import com.gmail.fuskerr63.java.Contact;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactDetailsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void updateDetails(Contact contact);
    @StateStrategyType(SingleStateStrategy.class)
    void loadingStatus(boolean show);
}