package com.gmail.fuskerr63.java.interactor;

import com.gmail.fuskerr63.java.entity.Contact;

public interface NotificationInteractor {
    NotificationStatus toggleNotificationForContact(Contact contact);

    @SuppressWarnings("unused")
    NotificationStatus getNotificationStatusForContact(Contact contact);
}
