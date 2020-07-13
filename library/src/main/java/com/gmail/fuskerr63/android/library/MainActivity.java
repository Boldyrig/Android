package com.gmail.fuskerr63.android.library;

import androidx.appcompat.app.AppCompatActivity;
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
import com.gmail.fuskerr63.library.R;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        ContactListFragment.OnMenuItemClickContacts,
        ContactDetailsFragment.OnMenuItemClickDetails {

    private static final String EXTRA_ID = "ID";
    private static final String CONTACT_LIST_FRAGMENT_TAG = "CONTACT_LIST_FRAGMENT_TAG";
    private static final String CONTACT_DETAILS_FRAGMENT_TAG = "CONTACT_DETAILS_FRAGMENT_TAG";
    private static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";
    private static final int PERMISSIONS_REQUEST = 5050;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.empty);
        setSupportActionBar(findViewById(R.id.toolbar));

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST);
        }
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            showDetails(intent.getExtras().getInt(EXTRA_ID));
        } else {
            if (savedInstanceState == null) {
                FragmentManager manager = getSupportFragmentManager();
                ContactListFragment contactListFragment =
                        (ContactListFragment) manager.findFragmentByTag(CONTACT_LIST_FRAGMENT_TAG);
                if (contactListFragment == null) {
                    FragmentTransaction transaction = manager.beginTransaction();
                    contactListFragment = ContactListFragment.newInstance();
                    transaction.replace(
                            R.id.fragment_container,
                            contactListFragment,
                            CONTACT_LIST_FRAGMENT_TAG);
                    transaction.commit();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Toast.makeText(
                getApplicationContext(),
                "Application closed",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void showDetails(int id) {
        FragmentManager manager = getSupportFragmentManager();
        ContactDetailsFragment contactDetailsFragment =
                (ContactDetailsFragment) manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG);
        if (contactDetailsFragment == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            contactDetailsFragment = ContactDetailsFragment.newInstance(id);
            transaction.replace(
                    R.id.fragment_container,
                    contactDetailsFragment,
                    CONTACT_DETAILS_FRAGMENT_TAG);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void showContactsMap() {
        FragmentManager manager = getSupportFragmentManager();
        ContactsMapFragment mapFragment =
                (ContactsMapFragment) manager.findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            mapFragment = ContactsMapFragment.newInstance();
            transaction.replace(R.id.fragment_container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    private void showContactMap(int id, String name) {
        FragmentManager manager = getSupportFragmentManager();
        ContactMapFragment mapFragment =
                (ContactMapFragment) manager.findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            FragmentTransaction transaction = manager.beginTransaction();
            mapFragment = ContactMapFragment.newInstance(id, name);
            transaction.replace(R.id.fragment_container, mapFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onClick(@NonNull View view) {
        showDetails(view.getId());
    }


    @Override
    public void onMenuItemClickDetails(int id, @Nullable String name) {
        showContactMap(id, name);
    }


    @Override
    public void onMenuItemClickContacts() {
        showContactsMap();
    }
}
