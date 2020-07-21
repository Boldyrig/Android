package com.gmail.fuskerr63.android.library.fragment.contact;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.fuskerr63.android.library.delegate.contact.ContactDetailsDelegate;
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactComponentContainer;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenterJava;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.util.Calendar;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.annotations.Nullable;
import io.reactivex.annotations.NonNull;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class ContactDetailsFragment extends MvpAppCompatFragment implements ContactDetailsView {
    private OnMenuItemClickDetails menuItemClickListener;
    private ContactDetailsDelegate contactDetailsDelegate;

    private String name;
    private String notificatinCancel;
    private String notificationSend;

    @InjectPresenter
    ContactDetailsPresenterJava detailsPresenter;

    @Inject
    Provider<ContactDetailsPresenterJava> presenterProvider;

    @ProvidePresenter
    ContactDetailsPresenterJava provideDetailsPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_map_details) {
            menuItemClickListener.onMenuItemClickDetails(Objects.requireNonNull(getArguments()).getInt("ID"), name);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuItemClickDetails) {
            menuItemClickListener = (OnMenuItemClickDetails) context;
        }
        Application app = Objects.requireNonNull(getActivity()).getApplication();
        if (app instanceof ContactApplicationContainer) {
            AppContainer appContainer = ((ContactApplicationContainer) app).getAppComponent();
            ContactComponentContainer contactComponent = appContainer.plusContactComponent();
            contactComponent.inject(this);
        }
        notificatinCancel = context.getString(R.string.cancel_notification);
        notificationSend = context.getString(R.string.send_notification);
    }

    @Override
    public @NonNull
    View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ((TextView) Objects.requireNonNull(getActivity())
                .findViewById(R.id.title))
                .setText(R.string.contact_details_title);
        contactDetailsDelegate = new ContactDetailsDelegate(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsPresenter.showDetails(
                Objects.requireNonNull(getArguments()).getInt("ID"),
                notificatinCancel,
                notificationSend
        );
    }

    @Override
    public void updateDetails(@Nullable final Contact contact) {
        if (contact != null) {
            name = contact.getContactInfo().getName();
        }
        if (contactDetailsDelegate != null) {
            contactDetailsDelegate.showDetails(contact);
        }
        if (contact != null
                && contact.getBirthday() != null
                && contact.getBirthday().get(Calendar.YEAR) != 1
                && getView() != null) {
            Button button = getView().findViewById(R.id.birthday_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> detailsPresenter.onClickBirthday(
                    contact,
                    notificatinCancel,
                    notificationSend
            ));
        }
    }

    @Override
    public void loadingStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        Objects.requireNonNull(getView()).findViewById(R.id.progress_bar_details).setVisibility(status);
    }

    @Override
    public void setTextButton(@Nullable String text) {
        ((Button) Objects.requireNonNull(getView()).findViewById(R.id.birthday_button)).setText(text);
    }

    public static @NonNull ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        contactDetails.setArguments(bundle);
        return contactDetails;
    }

    public interface OnMenuItemClickDetails {
        void onMenuItemClickDetails(int id, @Nullable String name);
    }
}
