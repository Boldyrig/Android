package com.gmail.fuskerr63.androidlesson;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gmail.fuskerr63.fragments.ContactDetailsFragment;
import com.gmail.fuskerr63.fragments.ContactListFragment;
import com.gmail.fuskerr63.presenter.ContactPresenter;
import com.gmail.fuskerr63.repository.Contact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

public class MainActivity extends MvpAppCompatActivity implements
        MainView,
        ContactDetailsFragment.OnClickButtonListener,
        ContactDetailsFragment.OnLoadListener,
        ContactListFragment.OnListItemClickListener,
        ContactListFragment.OnLoadListener{
    private AlarmManager alarmManager;
    @InjectPresenter
    ContactPresenter contactPresenter;

    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";
    private final String ACTION = "com.gmail.fuskerr63.action.notification";
    private final String CONTACT_LIST_FRAGMENT_TAG = "CONTACT_LIST_FRAGMENT_TAG";
    private final String CONTACT_DETAILS_FRAGMENT_TAG = "CONTACT_DETAILS_FRAGMENT_TAG";
    private final int PERMISSIONS_REQUEST = 5050;

    @ProvidePresenter
    ContactPresenter provideContactPresenter() {
        return new ContactPresenter(getContentResolver(), getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // добавление toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_CONTACTS }, PERMISSIONS_REQUEST);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alarmManager = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            default: {
                Toast.makeText(getApplicationContext(), "Application closed", Toast.LENGTH_SHORT);
                finish();
            }
        }
    }

    @Override
    public void onListItemClick(View view) {
        contactPresenter.onListItemClick(view.getId());
    }

    @Override
    public void onClickButton(View view, Contact contact) {
        int id = contact.getId();
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_TEXT, getString(R.string.notification_text) + " " + contact.getName());
        intent.putExtra(EXTRA_ID, id);
        Boolean alarmIsUp = (PendingIntent.getBroadcast(this, id, new Intent(ACTION), PendingIntent.FLAG_NO_CREATE) != null);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if(alarmIsUp) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            ((Button) view).setText(R.string.send_notification);
        } else {
            Calendar birthday = contact.getBirthday();
            Calendar nextBirthday = new GregorianCalendar();
            nextBirthday.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)); // текущий год
            nextBirthday.set(Calendar.MONTH, birthday.get(Calendar.MONTH)); // месяц рождения
            nextBirthday.set(Calendar.DATE, birthday.get(Calendar.DATE)); // день рождения
            // обнулить время
            nextBirthday.set(Calendar.HOUR, 0);
            nextBirthday.set(Calendar.MINUTE, 0);
            nextBirthday.set(Calendar.SECOND, 0);
            if(System.currentTimeMillis() > nextBirthday.getTimeInMillis()) {
                nextBirthday.add(Calendar.YEAR, 1); // если дата уже была в этом году, то добавляем год
            }
            alarmManager.set(AlarmManager.RTC, nextBirthday.getTimeInMillis(), pendingIntent);
            ((Button) view).setText(R.string.cancel_notification);
        }
    }

    @Override
    public void showList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ContactListFragment contactListFragment = (ContactListFragment) manager.findFragmentByTag(CONTACT_LIST_FRAGMENT_TAG);
                if(contactListFragment == null) {
                    contactListFragment = ContactListFragment.newInstance();
                    transaction.replace(R.id.fragment_container, contactListFragment, CONTACT_LIST_FRAGMENT_TAG);
                    transaction.commit();
                }
            }
        });
    }

    public void updateList(final ArrayList<Contact> contacts) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                ContactListFragment contactListFragment = (ContactListFragment) manager.findFragmentByTag(CONTACT_LIST_FRAGMENT_TAG);
                if(contactListFragment != null) {
                    contactListFragment.updateList(contacts);
                }
            }
        });
    }

    public void updateDetails(final Contact contact) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FragmentManager manager = getSupportFragmentManager();
                ContactDetailsFragment contactDetailsFragment = (ContactDetailsFragment) manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG);
                if(contactDetailsFragment != null) {
                    contactDetailsFragment.updateDetails(contact);
                }
            }
        });
    }

    @Override
    public void showDetails(int id) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContactDetailsFragment contactDetailsFragment = (ContactDetailsFragment) manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG);
        if(contactDetailsFragment == null) {
            contactDetailsFragment = ContactDetailsFragment.newInstance(id);
            transaction.replace(R.id.fragment_container, contactDetailsFragment, CONTACT_DETAILS_FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onLoadListFragment() {
        contactPresenter.onLoadListFragment();
    }

    @Override
    public void onLoadDetailsFragment(int id) {
        contactPresenter.onLoadDetailsFragment(id);
    }
}
