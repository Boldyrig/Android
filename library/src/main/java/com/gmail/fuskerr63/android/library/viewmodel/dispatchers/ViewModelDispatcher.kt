package com.gmail.fuskerr63.android.library.viewmodel.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface ViewModelDispatcher {
    fun getMainDispatcher(): CoroutineDispatcher
    fun getIODispatcher(): CoroutineDispatcher
}
