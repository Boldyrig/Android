package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.service.Contact;
import com.gmail.fuskerr63.service.ContactService;

public class ContactDetailsFragment extends Fragment {
    private ContactService contactService;
    private Contact contact;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IBinder binder = getArguments().getBinder("BINDER");
        contactService = ((ContactService.ContactBinder) binder).getService();
        int id = getArguments().getInt("ID");
        contact = contactService.getContactById(id);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contact = null;
        contactService = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText("Contact Details");
        ((ImageView) view.findViewById(R.id.image)).setImageResource(contact.getImage());
        ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
        ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
        ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
        ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
        ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
        return view;
    }

    public static ContactDetailsFragment newInstance(int id, IBinder binder) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        bundle.putBinder("BINDER", binder);
        contactDetails.setArguments(bundle);
        return contactDetails;
    }
}
