package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;

public class ContactListFragment extends ListFragment {
    private OnListItemClickListener targetElement;
    private OnLoadListener loadListener;

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnListItemClickListener) {
            targetElement = (OnListItemClickListener) context;
        }
        if(context instanceof OnLoadListener) {
            loadListener = (OnLoadListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
        loadListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText(R.string.contact_list_title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadListener.onLoadListFragment();
    }

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
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if(targetElement != null) {
            targetElement.onListItemClick(v);
        }
    }

    public static ContactListFragment newInstance() {
        ContactListFragment contactList = new ContactListFragment();
        return contactList;
    }

    public interface OnListItemClickListener {
        public void onListItemClick(View view);
    }

    public interface OnLoadListener {
        public void onLoadListFragment();
    }
}