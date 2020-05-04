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
    private View viewDetail;
    private IBinder binder;
    private Contact contact;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contactService = ((ContactService.ContactBinder) binder).getService();
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
        viewDetail = view;
        new DetailsTask().execute();
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewDetail = null;
    }

    public static ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        contactDetails.setArguments(bundle);
        return contactDetails;
    }

    public void setBinder(IBinder binder) {
        if(binder != null) {
            this.binder = binder;
        }
    }

    class DetailsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            int id = getArguments().getInt("ID");
            contact = contactService.getContactById(id);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ((TextView) getActivity().findViewById(R.id.title)).setText("Contact Details");
            ((ImageView) viewDetail.findViewById(R.id.image)).setImageResource(contact.getImage());
            ((TextView) viewDetail.findViewById(R.id.name)).setText(contact.getName());
            ((TextView) viewDetail.findViewById(R.id.number1_contact)).setText(contact.getNumber());
            ((TextView) viewDetail.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
            ((TextView) viewDetail.findViewById(R.id.email1_contact)).setText(contact.getEmail());
            ((TextView) viewDetail.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
        }
    }
}
