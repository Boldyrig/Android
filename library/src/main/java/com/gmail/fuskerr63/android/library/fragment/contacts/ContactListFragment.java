package com.gmail.fuskerr63.android.library.fragment.contacts;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.fuskerr63.android.library.delegate.contacts.ContactListDelegate;
import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactsComponentContainer;
import com.gmail.fuskerr63.android.library.presenter.contacts.ContactListPresenter;
import com.gmail.fuskerr63.android.library.view.ContactListView;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;


public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {
    private OnMenuItemClickContacts onMenuItemClickListener;
    private ContactListDelegate contactListDelegate;

    private static final int DP_10 = 10;

    @InjectPresenter
    ContactListPresenter contactPresenter;

    @Inject
    Provider<ContactListPresenter> presenterProvider;

    @ProvidePresenter
    ContactListPresenter provideContactPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMenuItemClickContacts) {
            onMenuItemClickListener = (OnMenuItemClickContacts) context;
        }
        Application app = Objects.requireNonNull(getActivity()).getApplication();
        if (app instanceof ContactApplicationContainer) {
            AppContainer appComponent = ((ContactApplicationContainer) app).getAppComponent();
            ContactsComponentContainer contactsComponent = appComponent.plusContactsComponent();
            contactsComponent.inject(this);
        }
    }

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ((TextView) Objects.requireNonNull(getActivity())
                .findViewById(R.id.title))
                .setText(R.string.contact_list_title);
        contactListDelegate = new ContactListDelegate(view);
        contactListDelegate.onCreateView(getContext(), pxFromDp(DP_10));
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                contactPresenter.updateList(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.app_bar_map) {
            onMenuItemClickListener.onMenuItemClickContacts();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateList(@Nullable final List<Contact> contacts) {
        contactListDelegate.updateList(contacts);
    }

    @Override
    public void loadingStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        Objects.requireNonNull(getView()).findViewById(R.id.progress_bar_list).setVisibility(status);
    }

    private float pxFromDp(int dp) {
        return dp * Objects.requireNonNull(getContext()).getApplicationContext()
                .getResources()
                .getDisplayMetrics()
                .density;
    }

    @NonNull
    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    public interface OnMenuItemClickContacts {
        void onMenuItemClickContacts();
    }
}
