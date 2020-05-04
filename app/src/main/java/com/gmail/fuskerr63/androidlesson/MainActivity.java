package com.gmail.fuskerr63.androidlesson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.gmail.fuskerr63.fragments.ContactDetailsFragment;
import com.gmail.fuskerr63.fragments.ContactListFragment;
import com.gmail.fuskerr63.service.ContactService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private IBinder contactBinder;
    private boolean bound = false;
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // добавление toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                contactBinder = binder;
                bound = true;
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ContactListFragment contactListFragment = (ContactListFragment) manager.findFragmentByTag("CONTACT_LIST_FRAGMENT");
                if(contactListFragment == null) {
                    contactListFragment = ContactListFragment.newInstance();
                    contactListFragment.setBinder(binder);
                    transaction.add(R.id.fragment_container, contactListFragment, "CONTACT_LIST_FRAGMENT");
                    transaction.commit();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
                contactBinder = null;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ContactService.class), connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
            contactBinder = null;
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ContactDetailsFragment contactDetailsFragment = (ContactDetailsFragment) manager.findFragmentByTag("CONTACT_DETAILS_FRAGMENT");
        if(contactDetailsFragment == null) {
            contactDetailsFragment = ContactDetailsFragment.newInstance(view.getId());
            contactDetailsFragment.setBinder(contactBinder);
            transaction.replace(R.id.fragment_container, contactDetailsFragment, "CONTACT_DETAILS_FRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
