package com.gmail.fuskerr63.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.fuskerr63.androidlesson.R;

public class ContactListFragment extends Fragment {
    private View.OnClickListener targetElement = null;

    public ContactListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        targetElement = (View.OnClickListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        View contact = view.findViewById(R.id.contact1);
        if(targetElement != null) {
            contact.setOnClickListener(targetElement);
        }
        TextView title = getActivity().findViewById(R.id.title);
        title.setText("Contact List");
        return view;
    }

    public static Fragment newInstance() {
        Fragment contactList = new ContactListFragment();
        return contactList;
    }
}
