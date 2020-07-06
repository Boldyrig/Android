package com.gmail.fuskerr63.android.library.fragment.contact;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.gmail.fuskerr63.android.library.di.interfaces.AppContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactApplicationContainer;
import com.gmail.fuskerr63.android.library.di.interfaces.ContactComponentContainer;
import com.gmail.fuskerr63.android.library.presenter.contact.ContactDetailsPresenter;
import com.gmail.fuskerr63.android.library.view.ContactDetailsView;
import com.gmail.fuskerr63.library.R;
import com.gmail.fuskerr63.java.Contact;

import java.net.URI;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import io.reactivex.annotations.Nullable;
import moxy.MvpAppCompatFragment;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class ContactDetailsFragment extends MvpAppCompatFragment implements ContactDetailsView {
    private OnClickButtonListener targetElement;
    private OnMenuItemClickDetails menuItemClickListener;

    @InjectPresenter
    ContactDetailsPresenter detailsPresenter;

    @Inject
    Provider<ContactDetailsPresenter> presenterProvider;

    @ProvidePresenter
    ContactDetailsPresenter provideDetailsPresenter() {
        return presenterProvider.get();
    }

    private String name;

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
        if (item.getItemId() == R.id.app_bar_map_details) {
            menuItemClickListener.onMenuItemClickDetails(getArguments().getInt("ID"), name);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnClickButtonListener) {
            targetElement = (OnClickButtonListener) context;
        }
        if(context instanceof OnMenuItemClickDetails) {
            menuItemClickListener = (OnMenuItemClickDetails) context;
        }
        Application app = getActivity().getApplication();
        if(app instanceof ContactApplicationContainer) {
            AppContainer appContainer = ((ContactApplicationContainer) app).getAppComponent();
            ContactComponentContainer contactComponent = appContainer.plusContactComponent();
            contactComponent.inject(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        targetElement = null;
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
        detailsPresenter.showDetails(getArguments().getInt("ID"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void updateDetails(final Contact contact) {
        name = contact.getName();
        final String ACTION = "com.gmail.fuskerr63.action.notification";
        View view = getView();
        if(view != null) {
            URI image = contact.getImage();
            if(image != null) {
                ((ImageView) view.findViewById(R.id.image)).setImageURI(Uri.parse(image.toString()));
            }
            ((TextView) view.findViewById(R.id.name)).setText(contact.getName());
            ((TextView) view.findViewById(R.id.number1_contact)).setText(contact.getNumber());
            ((TextView) view.findViewById(R.id.number2_contact)).setText(contact.getNumber2());
            ((TextView) view.findViewById(R.id.email1_contact)).setText(contact.getEmail());
            ((TextView) view.findViewById(R.id.email2_contact)).setText(contact.getEmail2());
            ((TextView) view.findViewById(R.id.address_contact)).setText(contact.getAddress());
            Button button = (Button) view.findViewById(R.id.birthday_button);
            Calendar birthday = contact.getBirthday();
            if(birthday != null) {
                ((TextView) view.findViewById(R.id.birthday_contact)).setText(birthday.get(Calendar.DATE) + " " + birthday.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + birthday.get(Calendar.YEAR));
                button.setVisibility(View.VISIBLE);
            }
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

    public interface OnClickButtonListener {
        void onClickButton(View v, Contact contact);
    }

    public interface OnMenuItemClickDetails {
        void onMenuItemClickDetails(int id, String name);
    }
}
