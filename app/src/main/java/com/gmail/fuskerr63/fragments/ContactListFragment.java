package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.service.Contact;
import com.gmail.fuskerr63.service.ContactService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactListFragment extends ListFragment {
    private View.OnClickListener targetElement;
    private ContactService contactService;
    private IBinder binder;
    private Contact[] contacts;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        targetElement = (View.OnClickListener) context;
        contactService = ((ContactService.ContactBinder) binder).getService();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
        contactService = null;
        contacts = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText("Contact List");
        new ContactTask().execute();
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        v.setId((int) id);
        if(targetElement != null) {
            targetElement.onClick(v);
        }
    }

    public static ContactListFragment newInstance() {
        ContactListFragment contactList = new ContactListFragment();
        Bundle bundle = new Bundle();
        contactList.setArguments(bundle);
        return contactList;
    }

    public void setBinder(IBinder binder) {
        if(binder != null) {
            this.binder = binder;
        }
    }

    class ContactTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            contacts = contactService.getContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            final String[] from = new String[]{ "image", "name", "number"};
            final int[] to = { R.id.image, R.id.name, R.id.number };
            ArrayList<Map<String, Object>> arrayListContacts = new ArrayList<>();
            if(contacts != null) {
                for (Contact contact : contacts) {
                    Map<String, Object> mapContact = new HashMap<>();
                    mapContact.put("image", contact.getImage());
                    mapContact.put("name", contact.getName());
                    mapContact.put("number", contact.getNumber());
                    arrayListContacts.add(mapContact);
                }
            }
            SimpleAdapter sAdapter = new SimpleAdapter(getActivity(), arrayListContacts, R.layout.contact, from, to);
            setListAdapter(sAdapter);

        }
    }
}
