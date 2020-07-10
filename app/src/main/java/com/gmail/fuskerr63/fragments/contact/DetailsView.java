package com.gmail.fuskerr63.fragments.contact;

import com.gmail.fuskerr63.repository.Contact;

import moxy.MvpView;
import moxy.viewstate.strategy.AddToEndSingleStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface DetailsView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void updateDetails(Contact contact);
    @StateStrategyType(AddToEndSingleStrategy.class)
    void loadingStatus(boolean show);
}
