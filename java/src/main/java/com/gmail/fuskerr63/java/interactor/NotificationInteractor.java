package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.Contact;

public interface NotificationInteractor {
    NotificationStatus toggleNotificationForContact(Contact contact);

    NotificationStatus getNotificationStatusForContact(Contact contact);
}
