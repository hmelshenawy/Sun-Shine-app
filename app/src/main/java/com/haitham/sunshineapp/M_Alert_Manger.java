package com.haitham.sunshineapp;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.haitham.sunshineapp.WeatherApplication.CHANNEL_ID;


public class M_Alert_Manger extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


         send_Notifcation(context, intent);

    }


    public void send_Notifcation(Context context, Intent intent) {


        NotificationManagerCompat notiCompat =  NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle("Today Weather, "+intent.getStringExtra("location"));
        builder.setContentText(intent.getStringExtra("temp")+"°C");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);
        builder.setSmallIcon(R.drawable.weathericon);
        builder.build();

        Notification notification = builder.build();

        notiCompat.notify(1,notification);
    }
}
