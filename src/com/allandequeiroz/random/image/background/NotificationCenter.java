package com.allandequeiroz.random.image.background;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
/** Author: Allan de Queiroz */
public class NotificationCenter {

  private static final Logger LOG = Logger.getInstance(NotificationCenter.class);
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

  public static void error(final String message, final Throwable t) {
    LOG.error(message, t);
  }

  public static void info(final String message) {
    LOG.info(message);
  }

  public static void warn(final String message) {
    LOG.warn(message);
  }
}
