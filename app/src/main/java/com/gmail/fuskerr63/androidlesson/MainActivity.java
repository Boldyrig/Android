package com.gmail.fuskerr63.androidlesson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gmail.fuskerr63.fragments.ContactDetailsFragment;
import com.gmail.fuskerr63.fragments.ContactListFragment;
import com.gmail.fuskerr63.service.Contact;
import com.gmail.fuskerr63.service.ContactService;
import com.gmail.fuskerr63.receiver.ContactReceiver;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements
        ContactDetailsFragment.OnClickButtonListener,
        ContactService.ServiceInterface,
        ContactListFragment.OnListItemClickListener,
        ContactReceiver.ContactNotification {
    private ContactService contactService;
    private boolean bound = false;
    private ServiceConnection connection;
    private AlarmManager alarmManager;
    private ContactReceiver contactReceiver;

    private final String EXTRA_ID = "ID";
    private final String EXTRA_TEXT = "TEXT";
    private final String EXTRA_TEXT_VALUE = "Today is the birthday of ";
    private final String ACTION = "com.gmail.fuskerr63.action.notification";
    private final String CONTACT_LIST_FRAGMENT_TAG = "CONTACT_LIST_FRAGMENT_TAG";
    private final String CONTACT_DETAILS_FRAGMENT_TAG = "CONTACT_DETAILS_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // добавление toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                contactService = ((ContactService.ContactBinder) binder).getService();
                bound = true;
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                ContactListFragment contactListFragment = (ContactListFragment) manager.findFragmentByTag(CONTACT_LIST_FRAGMENT_TAG);
                if(contactListFragment == null) {
                    contactListFragment = ContactListFragment.newInstance();
                    transaction.add(R.id.fragment_container, contactListFragment, CONTACT_LIST_FRAGMENT_TAG);
                    transaction.commit();
                } else {
                    contactListFragment.serviceConnected();
                }
                ContactDetailsFragment contactDetailsFragment = (ContactDetailsFragment) manager.findFragmentByTag(CONTACT_DETAILS_FRAGMENT_TAG);
                if(contactDetailsFragment != null) {
                    contactDetailsFragment.serviceConnected();
                }
                Intent intent = getIntent();
                if(intent != null && intent.hasExtra(EXTRA_ID)) {
                    long id = intent.getExtras().getLong(EXTRA_ID);
                    showContactDetails(id);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bound = false;
                contactService = null;
            }
        };
        bindService(new Intent(this, ContactService.class), connection, BIND_AUTO_CREATE);
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        contactReceiver = new ContactReceiver();
        registerReceiver(contactReceiver, new IntentFilter(ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            unbindService(connection);
            bound = false;
            contactService = null;
        }
        alarmManager = null;
        if(contactReceiver != null) {
            unregisterReceiver(contactReceiver);
            contactReceiver = null;
        }
    }

    @Override
    public void onListItemClick(View view) {
        showContactDetails(view.getId());
    }

    public void showContactDetails(long id) {
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
    public void onClickButton(View view, long id, Contact contact) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(EXTRA_TEXT, EXTRA_TEXT_VALUE + contact.getName());
        intent.putExtra(EXTRA_ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Boolean allow = contact.getAllowNotification();
        if(allow) {
            alarmManager.cancel(pendingIntent);
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
            alarmManager.setInexactRepeating(AlarmManager.RTC, nextBirthday.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 365, pendingIntent);
            ((Button) view).setText(R.string.cancel_notification);
        }
        contact.setAllowNotification(!allow);
    }

    @Override
    public Contact[] getContacts() {
        return contactService.getContacts();
    }

    @Override
    public Contact getContactById(int id) {
        return contactService.getContactById(id);
    }

    @Override
    public void createNotification(String text, long id) {
        final int NOTIFICATION_ID = 1;
        final String CHANNEL_ID = "channelId";
        final String CHANNEL_NAME = "channelName";
        final String CHANNEL_DESCRIPTION = "channelDescription";
        final String NOTIFICATION_TITLE = "Contacts";

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_ID, id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.android_icon)
                .setContentText(text)
                .setContentTitle(NOTIFICATION_TITLE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build());
    }
}
