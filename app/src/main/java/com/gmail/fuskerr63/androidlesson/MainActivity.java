package com.gmail.fuskerr63.androidlesson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.gmail.fuskerr63.fragments.ContactDetailsFragment;
import com.gmail.fuskerr63.fragments.ContactListFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // добавление toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        // транзакция: добавить фрагмент списка конактов
        if (savedInstanceState == null) {
            // можно делать транзакцию
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragment_container, ContactListFragment.newInstance());
            transaction.commit();
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", view.getId());
        ContactDetailsFragment contactDetails = (ContactDetailsFragment) ContactDetailsFragment.newInstance();
        contactDetails.setArguments(bundle);
        transaction.replace(R.id.fragment_container, contactDetails);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
