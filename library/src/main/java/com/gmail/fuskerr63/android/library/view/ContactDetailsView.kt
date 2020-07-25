package com.gmail.fuskerr63.android.library.view

import com.gmail.fuskerr63.java.entity.Contact
import moxy.MvpView
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(SingleStateStrategy::class)
interface ContactDetailsView : MvpView {
    fun updateDetails(contact: Contact?)
    fun loadingStatus(show: Boolean)
    fun setTextButton(text: String?)
}
