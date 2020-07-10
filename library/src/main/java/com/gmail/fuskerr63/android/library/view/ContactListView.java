package com.gmail.fuskerr63.android.library.view;

import com.gmail.fuskerr63.java.entity.Contact;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface ContactListView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void updateList(List<Contact> contacts);
    @StateStrategyType(SingleStateStrategy.class)
    void loadingStatus(boolean show);
}