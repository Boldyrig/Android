package com.gmail.fuskerr63.fragments;

import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactListView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void updateList(ArrayList<Contact> contacts);
    @StateStrategyType(SingleStateStrategy.class)
    void loadingStatus(boolean show);
}
