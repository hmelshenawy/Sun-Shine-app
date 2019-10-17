package com.haitham.sunshineapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.MainThread;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;

import static com.haitham.sunshineapp.WeatherApplication.CHANNEL_ID;

public class URLThreading extends HandlerThread {

    Context context;
    String urlString;
    URL url1;
    URLConnection urlconnect;
    String CUR_LOCATION;
    Scanner in;
    StringBuffer buffer;
    String result ;
    String currentTemp;
    String currentHumidity;
    ArrayList<Forcast_List> weatherList = new ArrayList();
    Forcast_List[] myArray = new Forcast_List[7];
    Handler urlHandler;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Thread thread= currentThread();
    boolean isRunning ;
    String country;
    String localtime;



    public URLThreading(Context context, String urlString) {
        super("test");
        this.context = context;
        this.urlString= urlString;
    }


    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        urlHandler = new Handler();

    }


    public Handler getUrlHandler() {
        return urlHandler;
    }


    @Override
    public void run() {
        readURLData(urlString);
        send_Notifcation();

        super.run();
    }


    public void readURLData(String urlString) {
        isRunning = true;
        buffer = new StringBuffer(" ");
        try {
            url1 = new URL(urlString);
            urlconnect = url1.openConnection();
            urlconnect.connect();
            in = new Scanner(urlconnect.getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        while (in.hasNextLine()){
        buffer.append(in.nextLine());}
        result = buffer.toString();
      //  System.out.println(result);
        jasonGetter(result);
        isRunning = false;
    }



    public synchronized ArrayList<Forcast_List> jasonGetter(String r) {

        try {

                JSONObject weatherAPI = new JSONObject(r);
                JSONObject current = weatherAPI.getJSONObject("current");
                JSONObject location = weatherAPI.getJSONObject("location");

                 currentTemp = String.valueOf(current.getInt("temp_c"));
                 currentHumidity = String.valueOf(current.getInt("humidity"));
                 CUR_LOCATION = String.valueOf(location.getString("name"));
                 country = location.getString("country");
                 localtime = location.getString("localtime");


                JSONObject forcast = weatherAPI.getJSONObject("forecast");
                JSONArray forecastday = forcast.getJSONArray("forecastday");

                weatherList.clear();

            for (int i = 0; i < forecastday.length(); i++) {

                Forcast_List mylist = new Forcast_List();
                JSONObject forec= forecastday.getJSONObject(i);
                JSONObject day = forec.getJSONObject("day");
                double maxtemp_c = Math.round(day.getDouble("maxtemp_c")*10)/10d;
                double mintemp_c = Math.round(day.getDouble("mintemp_c")*10)/10d;
                String max = String.valueOf(maxtemp_c);
                String min = String.valueOf(mintemp_c);
                mylist.temp = max.substring(0, max.length()-2)+"/"+ min.substring(0, min.length()-2);

                double avghumidity = day.getDouble("avghumidity");
                mylist.humidity = String.valueOf(avghumidity);
                String date = forec.getString("date").substring(5);
                mylist.dt_txt = date;
                JSONObject condition = day.getJSONObject("condition");
                String text = condition.getString("text");
                mylist.condition = text;

                System.out.println(text);
                System.out.println(mylist.temp);

                weatherList.add(mylist);

            }

        } catch (JSONException e) {
            System.out.println("json exeption" + e);
            e.printStackTrace();
        }

        return weatherList;
    }

    public void send_Notifcation( ) {


        NotificationManagerCompat notiCompat =  NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle(" Weather, "+ CUR_LOCATION);
        builder.setContentText(currentTemp + "°C");
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setCategory(NotificationCompat.CATEGORY_ALARM);
        builder.setSmallIcon(R.drawable.weathericon);
        builder.build();

        Notification notification = builder.build();

        notiCompat.notify(1,notification);
    }

}
