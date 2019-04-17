package io.github.yazdipour.ketabdlr.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {
    public static void build(Context context, Class<?> cls,
                             String title, String content,
                             int nId) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, cls), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Important")
                .setContentTitle(title)
                .setContentText(content)
                .setColor(Color.CYAN)
                .setContentIntent(pendingIntent);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = builder.build();
        n.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
        if (manager != null)
            manager.notify((nId == -1) ? (int) System.currentTimeMillis() : nId, n);
    }
}
