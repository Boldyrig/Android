package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.java.repository.ContactDetailsRepository;
import com.gmail.fuskerr63.java.repository.ContactListRepository;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import kotlinx.coroutines.flow.Flow;


public class ContactModel implements ContactInteractor {
    @Nullable
    private final ContactListRepository listRepository;

    @Nullable
    private final ContactDetailsRepository detailsRepository;

    public ContactModel(
            @Nullable ContactListRepository listRepository,
            @Nullable ContactDetailsRepository detailsRepository) {
        this.listRepository = listRepository;
        this.detailsRepository = detailsRepository;
    }

    @Nullable
    @Override
    public Single<List<Contact>> getContacts(@NonNull String selector) {
        if (listRepository != null) {
            return listRepository.getContacts(selector);
        }
        return null;
    }

    @Override
    public Flow<Contact> getContactById(int id) {
        return detailsRepository.getContactById(id);
    }
}
