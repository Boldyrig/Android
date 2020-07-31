package com.gmail.fuskerr63.android.library.instrumentedTest

import com.gmail.fuskerr63.android.library.viewmodel.dispatchers.ViewModelDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class ViewModelDispatcherTest : ViewModelDispatcher {
    override fun getMainDispatcher() = TestCoroutineDispatcher()

    override fun getIODispatcher() = TestCoroutineDispatcher()
}
