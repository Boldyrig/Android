package com.gmail.fuskerr63.android.library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gmail.fuskerr63.android.library.fragment.contact.ContactDetailsFragment;
import com.gmail.fuskerr63.android.library.fragment.contacts.ContactListFragment;
import com.gmail.fuskerr63.android.library.fragment.map.ContactMapFragment;
import com.gmail.fuskerr63.android.library.fragment.map.ContactsMapFragment;
import com.gmail.fuskerr63.java.entity.Contact;
import com.gmail.fuskerr63.library.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.reactivex.annotations.NonNull;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        ContactListFragment.onMenuItemClickContacts,
        ContactDetailsFragment.OnMenuItemClickDetails{

    private final String EXTRA_ID = "ID";
    private final String CONTACT_LIST_FRAGMENT_TAG = "CONTACT_LIST_FRAGMENT_TAG";
    private final String CONTACT_DETAILS_FRAGMENT_TAG = "CONTACT_DETAILS_FRAGMENT_TAG";
    private final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";
    private final int PERMISSIONS_REQUEST = 5050;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.empty);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.READ_CONTACTS }, PERMISSIONS_REQUEST);
        }
        Intent intent = getIntent();
        if(intent != null && intent.getExtras() != null) {
            showDetails(intent.getExtras().getInt(EXTRA_ID));
        } else {
            if(savedInstanceState == null) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ContactListFragment contactListFragment = (ContactListFragment) manager.findFragmentByTag(CONTACT_LIST_FRAGMENT_TAG);
                if(contactListFragment == null) {
                    contactListFragment = ContactListFragment.newInstance();
                    transaction.replace(R.id.fragment_container, contactListFragment, CONTACT_LIST_FRAGMENT_TAG);
                    transaction.commit();
                }
            }
        }
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

    public void showContactsMap() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContactsMapFragment mapFragment = (ContactsMapFragment) manager.findFragmentByTag(MAP_FRAGMENT_TAG);
        if(mapFragment == null){
            mapFragment = ContactsMapFragment.newInstance();
            transaction.replace(R.id.fragment_container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void showContactMap(int id, String name) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContactMapFragment mapFragment = (ContactMapFragment) manager.findFragmentByTag(MAP_FRAGMENT_TAG);
        if(mapFragment == null){
            mapFragment = ContactMapFragment.newInstance(id, name);
            transaction.replace(R.id.fragment_container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        showDetails(view.getId());
    }

    @Override
    public void onMenuItemClickDetails(int id, String name) { showContactMap(id, name); }

    @Override
    public void onMenuItemClickContacts() { showContactsMap(); }
}
