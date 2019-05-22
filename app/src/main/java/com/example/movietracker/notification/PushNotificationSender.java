package com.example.movietracker.notification;

import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.movietracker.R;
import com.example.movietracker.view.activity.MainActivity;
import com.example.movietracker.view.model.PushNotificationsMovieInfo;

public class PushNotificationSender {

    private Context context;
    private NotificationManager mNotificationManager;

    public PushNotificationSender(Context context) {
        this.context = context;
    }

    public void sendNotification(PushNotificationsMovieInfo notificationBody, int notificationId ) {

        String channelId = "Your_channel_id";
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title", importance);
            mNotificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), channelId);
        Intent ii = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);


      /*  RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.notification_large);*/

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(notificationBody.getMovieTitlesList().toString());
        bigText.setBigContentTitle(notificationBody.getTitle());
        bigText.setSummaryText("Click to open favorites");

        mBuilder/*.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)*/
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.ic_clear)
        .setContentTitle("Movie Tracker")
        .setContentText("Movie updated")
                /*.setLargeIcon(R..ic_imdb)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(myBitmap)
                        .bigLargeIcon(null))*/
        .setPriority(Notification.PRIORITY_MAX);

        mNotificationManager.notify(0, mBuilder.build());
    }
}
