package com.gmail.fuskerr63.fragments.contact;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gmail.fuskerr63.androidlesson.R;
import com.gmail.fuskerr63.app.ContactApplication;
import com.gmail.fuskerr63.di.app.AppComponent;
import com.gmail.fuskerr63.di.contact.ContactComponent;
import com.gmail.fuskerr63.di.contact.ContactModule;
import com.gmail.fuskerr63.presenter.DetailsPresenter;
import com.gmail.fuskerr63.repository.Contact;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactDetailsFragment extends MvpAppCompatFragment implements DetailsView {
    private OnClickButtonListener targetElement;
    private onMenuItemClickDetails menuItemClickListener;

    private final String EXTRA_ID = "ID";

    @InjectPresenter
    DetailsPresenter detailsPresenter;

    @Inject
    Provider<DetailsPresenter> presenterProvider;

    @ProvidePresenter
    DetailsPresenter provideDetailsPresenter() {
        return presenterProvider.get();
    }

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_map_details:
                menuItemClickListener.onMenuItemClickDetails(getArguments().getInt(EXTRA_ID));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnClickButtonListener) {
            targetElement = (OnClickButtonListener) context;
        }
        if(context instanceof onMenuItemClickDetails) {
            menuItemClickListener = (onMenuItemClickDetails) context;
        }
        AppComponent appComponent = ((ContactApplication) getActivity().getApplication()).getAppComponent();
        ContactComponent contactComponent = appComponent.plusContactComponent(new ContactModule(getArguments().getInt("ID")));
        contactComponent.inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
        menuItemClickListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        ((TextView) getActivity().findViewById(R.id.title)).setText(R.string.contact_details_title);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void updateDetails(final Contact contact) {
        final String ACTION = "com.gmail.fuskerr63.action.notification";
        View view = getView();
        if(view != null) {
            ((ImageView) view.findViewById(R.id.image)).setImageURI(contact.getImage());
            ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
            ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
            ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
            ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
            ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
            Calendar birthday = contact.getBirthday();
            if(birthday != null) {
                ((TextView) view.findViewById(R.id.birthday_contact)).setText(birthday.get(Calendar.DATE) + " " + birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + birthday.get(Calendar.YEAR));
            }
            Button button = (Button) view.findViewById(R.id.birthday_button);
            Context context = getContext();
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
                    if(targetElement != null) {
                        targetElement.onClickButton(v, contact);
                    }
                }
            });
        }
    }

    @Override
    public void loadingStatus(boolean show) {
        int status = show ? View.VISIBLE : View.GONE;
        getView().findViewById(R.id.progress_bar_details).setVisibility(status);
    }

    public static ContactDetailsFragment newInstance(int id) {
        ContactDetailsFragment contactDetails = new ContactDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        contactDetails.setArguments(bundle);
        return contactDetails;
    }

    public interface OnClickButtonListener { public void onClickButton(View v, Contact contact); }

    public interface onMenuItemClickDetails { public void onMenuItemClickDetails(int id); }
}
