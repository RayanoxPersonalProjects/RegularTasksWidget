package com.rb.android.regularTasksWidget.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.rb.android.regularTasksWidget.R;

public class NotificationUtil {

    private static final String CHANNEL_ID = "RegularTask_Widget_Channel_ID";


    public static void PublishNotif_Info(String text, Context context) {
        showNotification("INFO - Regular Tasks Widget", text, context);
    }

    public static void PublishNotif_Warn(String text, Context context) {
        showNotification("WARN - Regular Tasks Widget", text, context);
    }

    public static void PublishNotif_Error(String text, Context context) {
        showNotification("ERROR - Regular Tasks Widget", text, context);
    }



    /*
            DOES NOT WORK
     */
    private static void showNotification(String title, String message, Context context) {
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Regular Tasks Widget",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("The channel of notification related to the regular tasks widget");
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher) // notification icon
                .setContentTitle(title) // title for notification
                .setContentText(message)// message for notification
                .setAutoCancel(true); // clear notification after click

        //Intent intent = new Intent(getApplicationContext(), ACTIVITY_NAME.class);
        //PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
