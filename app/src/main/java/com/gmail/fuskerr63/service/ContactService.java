package com.gmail.fuskerr63.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.gmail.fuskerr63.androidlesson.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactService extends Service {
    private Contact[] contacts;
    private ContactBinder binder;
    private ExecutorService executor;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new ContactBinder();
        executor = Executors.newFixedThreadPool(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binder = null;
        executor = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        executor.execute(new ContactRunnable());
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public Contact getContactById(int id) {
        try{
            return contacts[id];
        } catch(ArrayIndexOutOfBoundsException e){
            return null;
        }
    }

    public class ContactBinder extends Binder {
        public ContactService getService() {
            return ContactService.this;
        }
    }

    class ContactRunnable implements Runnable {

        @Override
        public void run() {
            contacts = new Contact[]{
                    new Contact(R.drawable.android_icon,"Fedor", "443344", "99", "fedor@gmail.com", "fedor@gmail.com"),
                    new Contact(R.drawable.android_icon,"Igor", "668844", "12", "iigorTheBest@yandex.ru", "iigorTheBest@yandex.ru"),
                    new Contact(R.drawable.android_icon,"Leonid", "009863", "55", "leo@mail.ru", "none")
            };
        }
    }
}
