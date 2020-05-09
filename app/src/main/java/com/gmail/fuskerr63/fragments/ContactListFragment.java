package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListFragment extends ListFragment {
    private View.OnClickListener targetElement;
    private ContactService.ServiceInterface contactService;
    private ContactTask contactTask;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            serviceConnected();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof View.OnClickListener) {
            targetElement = (View.OnClickListener) context;
        }
        if(context instanceof ContactService.ServiceInterface) {
            contactService = (ContactService.ServiceInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
        contactService = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText("Contact List");
        return view;
    }

    public void serviceConnected() {
        if(contactService != null) {
            contactTask = new ContactTask(this);
            contactTask.execute(new ContactService.ServiceInterface[]{ contactService });
        }
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
        return contactList;
    }

    private static class ContactTask extends AsyncTask<ContactService.ServiceInterface, Void, Contact[]> {
        private WeakReference<ListFragment> weakFragment;

        public ContactTask(ListFragment fragment) {
            weakFragment = new WeakReference<ListFragment>(fragment);
        }

        @Override
        protected Contact[] doInBackground(ContactService.ServiceInterface... contactServices) {
            return contactServices[0].getContacts();
        }

        @Override
        protected void onPostExecute(Contact[] contacts) {
            super.onPostExecute(contacts);
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
            SimpleAdapter sAdapter = new SimpleAdapter(weakFragment.get().getContext(), arrayListContacts, R.layout.contact, from, to);
            weakFragment.get().setListAdapter(sAdapter);
        }
    }
}