package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.service.Contact;
import com.gmail.fuskerr63.service.ContactService;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class ContactDetailsFragment extends Fragment {
    private OnClickButtonListener targetElement;
    private ContactService.ServiceInterface contactService;
    private DetailTask detailTask;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnClickButtonListener) {
            targetElement = (OnClickButtonListener) context;
        }
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
        ((TextView) getActivity().findViewById(R.id.title)).setText(R.string.contact_details_title);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState == null) {
            serviceConnected();
        }
    }

    public void serviceConnected() {
        if(contactService != null) {
            detailTask = new DetailTask(getView(), getArguments().getLong("ID"), targetElement);
            detailTask.execute(new ContactService.ServiceInterface[]{ contactService });
        }
    }

    public static ContactDetailsFragment newInstance(long id) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("ID", id);
        contactDetails.setArguments(bundle);
        return contactDetails;
    }

    private static class DetailTask extends AsyncTask<ContactService.ServiceInterface, Void, Contact> {
        private WeakReference<View> weakView;
        private OnClickButtonListener targetElement;
        private long id;

        public DetailTask(View view, long id, OnClickButtonListener targetElement) {
            weakView = new WeakReference<View>(view);
            this.id = id;
            this.targetElement = targetElement;
        }

        @Override
        protected Contact doInBackground(ContactService.ServiceInterface... contactServices) {
            return contactServices[0].getContactById((int) id);
        }

        @Override
        protected void onPostExecute(final Contact contact) {
            super.onPostExecute(contact);
            View view = weakView.get();
            if(view != null) {
                ((ImageView) view.findViewById(R.id.image)).setImageResource(contact.getImage());
                ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
                ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
                ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
                ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
                ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
                Calendar birthday = contact.getBirthday();
                ((DatePicker) view.findViewById(R.id.birthday_contact)).init(birthday.get(Calendar.YEAR), birthday.get(Calendar.MONTH), birthday.get(Calendar.DATE), null);
                Button button = (Button) view.findViewById(R.id.birthday_button);
                if (contact.getAllowNotification()) {
                    button.setText(R.string.cancel_notification);
                } else {
                    button.setText(R.string.send_notification);
                }
                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        targetElement.onClickButton(v, id, contact);
                    }
                });
            }
        }
    }

    public interface OnClickButtonListener {
        public void onClickButton(View v, long id, Contact contact);
    }
}
