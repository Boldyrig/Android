package com.gmail.fuskerr63.android.library.viewmodel.dispatchers

import kotlinx.coroutines.Dispatchers

class ViewModelDispatcherProvider : ViewModelDispatcher {
    override fun getMainDispatcher() = Dispatchers.Main

    override fun getIODispatcher() = Dispatchers.IO
}
