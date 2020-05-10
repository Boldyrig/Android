package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.service.Contact;
import com.gmail.fuskerr63.service.ContactService;

import java.lang.ref.WeakReference;

public class ContactDetailsFragment extends Fragment {
    private ContactService.ServiceInterface contactService;
    private DetailTask detailTask;

    public ContactDetailsFragment() {
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
        if(context instanceof ContactService.ServiceInterface){
            contactService = (ContactService.ServiceInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        contactService = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText("Contact Details");
        return view;
    }

    public void serviceConnected() {
        if(contactService != null) {
            detailTask = new DetailTask(getView(), getArguments().getInt("ID"));
            detailTask.execute(new ContactService.ServiceInterface[]{ contactService });
        }
    }

    public static ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        contactDetails.setArguments(bundle);
        return contactDetails;
    }

    private static class DetailTask extends AsyncTask<ContactService.ServiceInterface, Void, Contact> {
        private WeakReference<View> weakView;
        private int id;

        public DetailTask(View view, int id) {
            weakView = new WeakReference<View>(view);
            this.id = id;
        }

        @Override
        protected Contact doInBackground(ContactService.ServiceInterface... contactServices) {
            return contactServices[0].getContactById(id);
        }

        @Override
        protected void onPostExecute(Contact contact) {
            super.onPostExecute(contact);
            View view = weakView.get();
            if(view != null) {
                ((ImageView) view.findViewById(R.id.image)).setImageResource(contact.getImage());
                ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
                ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
                ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
                ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
                ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
            }
        }
    }
}
