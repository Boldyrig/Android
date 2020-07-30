package com.gmail.fuskerr63.android.library.di.interfaces

interface ViewModelComponentFactory {
    fun create(
        id: Int
    ) : ViewModelComponentContainer
}
