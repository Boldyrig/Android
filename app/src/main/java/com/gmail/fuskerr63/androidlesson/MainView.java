package com.gmail.fuskerr63.androidlesson;

import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;

import moxy.MvpView;

import moxy.viewstate.strategy.OneExecutionStateStrategy;
import moxy.viewstate.strategy.SingleStateStrategy;
import moxy.viewstate.strategy.StateStrategyType;

public interface MainView extends MvpView {
    @StateStrategyType(SingleStateStrategy.class)
    void showList();
    @StateStrategyType(SingleStateStrategy.class)
    void updateList(ArrayList<Contact> contacts);
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showDetails(int id);
    @StateStrategyType(SingleStateStrategy.class)
    void updateDetails(Contact contact);
}
