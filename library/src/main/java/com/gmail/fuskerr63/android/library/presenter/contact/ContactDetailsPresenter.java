package com.gmail.fuskerr63.android.library.presenter.contact;

<<<<<<< HEAD:app/src/main/java/com/gmail/fuskerr63/presenter/DetailsPresenter.java
import android.util.Log;

import com.gmail.fuskerr63.database.AppDatabase;
import com.gmail.fuskerr63.fragments.contact.DetailsView;
import com.gmail.fuskerr63.repository.Contact;
import com.gmail.fuskerr63.repository.Repository;
=======
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.interactor.ContactInteractor;
>>>>>>> clean-architecture:library/src/main/java/com/gmail/fuskerr63/android/library/presenter/contact/ContactDetailsPresenter.java

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import moxy.MvpPresenter;

<<<<<<< HEAD:app/src/main/java/com/gmail/fuskerr63/presenter/DetailsPresenter.java
public class DetailsPresenter extends MvpPresenter<DetailsView> {
    Repository repository;
    AppDatabase db;
=======
public class ContactDetailsPresenter extends MvpPresenter<ContactDetailsView> {
    private ContactInteractor interactor;
>>>>>>> clean-architecture:library/src/main/java/com/gmail/fuskerr63/android/library/presenter/contact/ContactDetailsPresenter.java

    private final CompositeDisposable disposable = new CompositeDisposable();

    @Inject
<<<<<<< HEAD:app/src/main/java/com/gmail/fuskerr63/presenter/DetailsPresenter.java
    public DetailsPresenter(Repository repository, int id, AppDatabase db) {
        this.repository = repository;
        this.db = db;
        disposable.add(repository.getContactById(id)
=======
    public ContactDetailsPresenter(ContactInteractor interactor) {
        this.interactor = interactor;
    }

    public void showDetails(int id) {
        disposable.add(interactor.getContactById(id)
>>>>>>> clean-architecture:library/src/main/java/com/gmail/fuskerr63/android/library/presenter/contact/ContactDetailsPresenter.java
                .subscribeOn(Schedulers.io())
                .flatMap(contact -> db.userDao().getUserByContactId(contact.getId())
                        .map(user -> {
                            Contact newContact = new Contact(
                                    contact.getId(),
                                    contact.getImage(),
                                    contact.getName(),
                                    contact.getNumber(),
                                    contact.getNumber2(),
                                    contact.getEmail(),
                                    contact.getEmail2(),
                                    contact.getBirthday(),
                                    user.getAddress()
                            );
                            return newContact;
                        }))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> getViewState().loadingStatus(true))
                .doFinally(() -> getViewState().loadingStatus(false))
                .subscribe(contact -> getViewState().updateDetails(contact)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interactor = null;
        disposable.dispose();
    }
}