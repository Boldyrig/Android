package com.gmail.fuskerr63.android.library.fragment.contacts;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactsComponentContainer;
import com.gmail.fuskerr63.android.library.presenter.contacts.ContactListPresenter;
import com.gmail.fuskerr63.android.library.recyclerview.ContactAdapter;
import com.gmail.fuskerr63.android.library.recyclerview.ContactDecorator;
import com.gmail.fuskerr63.android.library.view.ContactListView;
import com.gmail.fuskerr63.library.R;
import com.gmail.fuskerr63.java.Contact;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {
    private View.OnClickListener targetElement;
    private ContactAdapter contactAdapter;

    private final String TAG = "TAG";

    @InjectPresenter
    ContactListPresenter contactPresenter;

    @Inject
    Provider<ContactListPresenter> presenterProvider;

    @ProvidePresenter
    ContactListPresenter provideContactPresenter() {
        return presenterProvider.get();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof View.OnClickListener) {
            targetElement = (View.OnClickListener) context;
        }
        Application app = getActivity().getApplication();
        if(app instanceof ContactApplicationContainer) {
            AppContainer appComponent = ((ContactApplicationContainer) app).getAppComponent();
            ContactsComponentContainer contactsComponent = appComponent.plusContactsComponent();
            contactsComponent.inject(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText(R.string.contact_list_title);
        contactAdapter = new ContactAdapter(targetElement);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(contactAdapter);
        recyclerView.addItemDecoration(new ContactDecorator((int) pxFromDp(10)));

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contactAdapter = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
    public void updateList(final List<Contact> contacts) {
        if(contactAdapter != null) {
            contactAdapter.setContacts(contacts);
        }
    }

    @Override
    public void loadingStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        getView().findViewById(R.id.progress_bar_list).setVisibility(status);
    }

    private float pxFromDp(int dp) {
        return dp * getContext().getApplicationContext()
                .getResources()
                .getDisplayMetrics()
                .density;
    }

    public static ContactListFragment newInstance() {
        ContactListFragment contactList = new ContactListFragment();
        return contactList;
    }

    public interface onMenuItemClickContacts {
        void onMenuItemClickContacts();
    }
}
