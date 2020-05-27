package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.presenter.ContactListPresenter;
import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactListFragment extends MvpAppCompatFragment implements ContactListView {
    private ListView.OnItemClickListener targetElement;
    private Handler handler;
    private ListView listView;
    @InjectPresenter
    ContactListPresenter contactPresenter;

    @ProvidePresenter
    ContactListPresenter provideContactPresenter() {
        return new ContactListPresenter(getContext().getContentResolver());
    }

    private final String CONTACT_LIST_FRAGMENT_TAG = "CONTACT_LIST_FRAGMENT_TAG";

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ListView.OnItemClickListener) {
            targetElement = (ListView.OnItemClickListener) context;
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
        listView = view.findViewById(R.id.contact_list);
        handler = new Handler(Looper.getMainLooper());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        listView = null;
        handler = null;
    }

    @Override
    public void updateList(final ArrayList<Contact> contacts) {
        ArrayAdapter<Contact> arrayAdapter = new ArrayAdapter<Contact>(getContext(), R.layout.contact, contacts) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.contact, parent, false);
                }
                TextView name = (TextView) convertView.findViewById(R.id.name);
                TextView number = (TextView) convertView.findViewById(R.id.number);
                ImageView image = (ImageView) convertView.findViewById(R.id.image);

                Contact curContact = contacts.get(position);

                convertView.setId(curContact.getId());

                name.setText(curContact.getName());
                number.setText(curContact.getNumber());
                Uri imageUri = curContact.getImage();
                if(imageUri == null) {
                    image.setImageResource(R.drawable.android_icon);
                } else {
                    image.setImageURI(imageUri);
                }
                return convertView;
            }
        };
        if(listView != null) {
            listView.setOnItemClickListener(targetElement);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(arrayAdapter);
                }
            });
        }
    }

    public static ContactListFragment newInstance() {
        ContactListFragment contactList = new ContactListFragment();
        return contactList;
    }
}