package com.allandequeiroz.random.image.background;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/** Author: Allan de Queiroz */
public class NotificationCenter {

  private static final String MESSAGE_PREFIX = "[Random Background]";

  public static void notify(final String message) {
    final Notification n =
        new Notification(
            "extras",
            "Notice",
            String.format("%s - %s", MESSAGE_PREFIX, message),
            NotificationType.INFORMATION);
    Notifications.Bus.notify(n);
  }
}
