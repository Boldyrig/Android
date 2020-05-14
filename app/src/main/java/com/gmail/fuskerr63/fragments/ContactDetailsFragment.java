package com.gmail.fuskerr63.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.MainActivity;
import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.service.Contact;
import com.gmail.fuskerr63.service.ContactService;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Locale;

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
            detailTask = new DetailTask(getView(), getArguments().getInt("ID"), targetElement, getContext());
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
        private WeakReference<OnClickButtonListener> weakTargetElement;
        private WeakReference<Context> weakContext;
        private int id;

        public DetailTask(View view, int id, OnClickButtonListener targetElement, Context context) {
            weakView = new WeakReference<View>(view);
            weakTargetElement = new WeakReference<OnClickButtonListener>(targetElement);
            weakContext = new WeakReference<Context>(context);
            this.id = id;
        }

        @Override
        protected Contact doInBackground(ContactService.ServiceInterface... contactServices) {
            return contactServices[0].getContactById(id);
        }

        @Override
        protected void onPostExecute(final Contact contact) {
            super.onPostExecute(contact);
            final String ACTION = "com.gmail.fuskerr63.action.notification";
            View view = weakView.get();
            if(view != null) {
                ((ImageView) view.findViewById(R.id.image)).setImageResource(contact.getImage());
                ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
                ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
                ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
                ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
                ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
                Calendar birthday = contact.getBirthday();
                ((TextView) view.findViewById(R.id.birthday_contact)).setText(birthday.get(Calendar.DATE) + " " + birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + birthday.get(Calendar.YEAR));
                Button button = (Button) view.findViewById(R.id.birthday_button);
                Context context = weakContext.get();
                if(context != null) {
                    Boolean alarmIsUp = (PendingIntent.getBroadcast(context, 0, new Intent(ACTION), PendingIntent.FLAG_NO_CREATE) != null);
                    if (alarmIsUp) {
                        button.setText(R.string.cancel_notification);
                    } else {
                        button.setText(R.string.send_notification);
                    }
                }
                button.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OnClickButtonListener targetElement = weakTargetElement.get();
                        if(targetElement != null) {
                            targetElement.onClickButton(v, id, contact);
                        }
                    }
                });
            }
        }
    }

    public interface OnClickButtonListener {
        public void onClickButton(View v, int id, Contact contact);
    }
}
