package com.gmail.fuskerr63.fragments;

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

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.presenter.ContactListPresenter;
import com.gmail.fuskerr63.recyclerview.ContactAdapter;
import com.gmail.fuskerr63.recyclerview.ContactDecorator;
import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {
    private View.OnClickListener targetElement;
    private ContactAdapter contactAdapter;

    private final String TAG = "TAG";

    @InjectPresenter
    ContactListPresenter contactPresenter;

    @ProvidePresenter
    ContactListPresenter provideContactPresenter() {
        return new ContactListPresenter(getContext().getContentResolver());
    }

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof View.OnClickListener) {
            targetElement = (View.OnClickListener) context;
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

        Observable<String> observable = Observable.create((ObservableOnSubscribe<String>) emitter -> {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    emitter.onNext(query);
                    searchView.clearFocus();
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    emitter.onNext(newText);
                    return false;
                }
            });
        });

        observable.map(text -> text.trim())
                .subscribeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter(text -> !text.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(text -> contactPresenter.updateList(text), error -> Log.d(TAG, error.getMessage()));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void updateList(final ArrayList<Contact> contacts) {
        if(contactAdapter != null) {
            contactAdapter.setContacts(contacts);
        }
    }

    @Override
    public void showLoading() {
        getView().findViewById(R.id.progress_bar_list).setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        getView().findViewById(R.id.progress_bar_list).setVisibility(View.GONE);
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
}