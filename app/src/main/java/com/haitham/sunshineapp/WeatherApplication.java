package com.haitham.sunshineapp;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class WeatherApplication extends Application {

    public final static String CHANNEL_ID = "Channel_ID";
    public static String location = "";
    public static String temp = "";

    NotificationChannel notificationChannel;
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "mchannel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("mNoteChannel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
    }


}
