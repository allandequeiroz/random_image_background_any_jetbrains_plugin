package io.allandequeiroz.random.image.background;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
class NotificationCenter {

   private static final String MESSAGE_PREFIX = "[Random Image Background]";

   public static void notice(String message) {
      Notification n = new Notification(
            "extras",
            "Notice",
            String.format("%s - %s", MESSAGE_PREFIX, message),
            NotificationType.INFORMATION);
      Notifications.Bus.notify(n);
   }
}
