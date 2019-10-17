package com.haitham.sunshineapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import static com.haitham.sunshineapp.WeatherApplication.CHANNEL_ID;

public class MJob_Intent_Service extends JobService {

    String  forcastURL = "https://api.apixu.com/v1/forecast.json?key=719c84279eb9431dbe4204412191908&q=cairo&days=7";

    StringBuffer buffer;
    URL url1;
    URLConnection urlconnect;
    String CUR_LOCATION;
    Scanner in;
    String result ;
    String currentTemp;
    String currentHumidity;
    String country;
    String localtime;
    ArrayList<Forcast_List> weatherList = new ArrayList();
    Data_Retrive myD;

    String location = "";
    String temp = "";


    public void send_Notifcation( ) {


        NotificationManagerCompat notiCompat =  NotificationManagerCompat.from( getBaseContext() );
        NotificationCompat.Builder builder = new NotificationCompat.Builder( getBaseContext() , CHANNEL_ID );
        builder.setContentTitle( myD.currentTemp + "°C, " + myD.CUR_LOCATION );
        builder.setContentText( myD.weatherList.get(0).temp + "°C" );
        builder.setPriority ( NotificationCompat.PRIORITY_HIGH );
        builder.setCategory ( NotificationCompat.CATEGORY_ALARM) ;
        builder.setSmallIcon ( R.drawable.weathericon );
        builder.build();

        Notification notification = builder.build();

        notiCompat.notify(1,notification);
    }


    public void alarm_Set(int hr, int min){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd, HH:mma");
        String s = String.valueOf(simpleDateFormat.format(calendar.getTime().getTime()));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(getBaseContext(), M_Alert_Manger.class);
        in.putExtra("temp", temp);
        in.putExtra("location", location);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 13, in, 0);

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pendingIntent);
        long l = (long) (.02* 60*60*1000);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), l, pendingIntent);

        System.out.println("intentservicetime"+s);

    }


    public void readURLData(String urlString) {

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

    }


    public ArrayList<Forcast_List> jasonGetter(String r) {

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


    public String uriMaker(){

        String URI_BASE= "https://api.openweathermap.org/data/2.5/forecast?q=";
        String URI_DAYS = "&APPID=76c89581cd5d04d2863421315e34f260";

        loadData();

        StringBuilder URIBUILDER = new StringBuilder();
        URIBUILDER.append(URI_BASE);
        URIBUILDER.append(location);
        URIBUILDER.append(URI_DAYS);

        return URIBUILDER.toString();
    }


    protected void loadData() {

        SharedPreferences shared = getSharedPreferences("mydata", MODE_PRIVATE);
        location = shared.getString("location", "Cairo");
        Type typ = new TypeToken<Forcast_List[]>() {}.getType();
        System.out.println("current location is "+ location);
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

     myD = new Data_Retrive(getBaseContext(), uriMaker());
    myD.execute();

        return true ;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }


    class Data_Retrive extends AsyncTask<String, Void, Void> {

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
        String country;
        String localtime;


        public Data_Retrive(Context context, String urlString) {
            this.context = context;
            this.urlString = urlString;
        }


        @Override
        protected Void doInBackground(String... strings) {

            buffer = new StringBuffer(" ");
            try {
                url1 = new URL(urlString);
                urlconnect = url1.openConnection();
                urlconnect.connect();
                in = new Scanner(urlconnect.getInputStream());


            } catch (MalformedURLException e) {
                e.printStackTrace();
                System.out.println(e);
                return null;

            } catch (IOException e) {
                e.printStackTrace();
            }
            while (in.hasNextLine()){
                buffer.append(in.nextLine());}
            result = buffer.toString();
            //  System.out.println(result);
            jasonGetter(result);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            send_Notifcation();


        }


        public ArrayList<Forcast_List> jasonGetter(String r) {

            try {

                JSONObject weatherAPI = new JSONObject(r);

                JSONArray forecastday = weatherAPI.getJSONArray("list");

                JSONObject location = weatherAPI.getJSONObject("city");

                CUR_LOCATION = String.valueOf(location.getString("name"));
                country = location.getString("country");
                localtime = location.getString("timezone");

                weatherList.clear();

                for (int i = 0; i < forecastday.length(); i++) {

                    Forcast_List mylist = new Forcast_List();
                    JSONObject forec= forecastday.getJSONObject(i);
                    JSONObject main = forec.getJSONObject("main");
                    double maxtemp_c = Math.round((main.getDouble("temp_max") - 272.15)*10)/10d;
                    double mintemp_c = Math.round((main.getDouble("temp_min") - 272.15)*10)/10d;
                    double currtemp_c = Math.round((main.getDouble("temp") - 272.15)*10)/10d;

                    String max = String.valueOf(maxtemp_c);
                    String min = String.valueOf(mintemp_c);
                    currentTemp = String.valueOf(currtemp_c);
                    mylist.temp = max.substring(0, max.length()-2)+"/"+ min.substring(0, min.length()-2);

                    double avghumidity = main.getDouble("humidity");
                    mylist.humidity = String.valueOf(avghumidity);
                    String date = forec.getString("dt_txt").substring(5);
                    mylist.dt_txt = date;
                    JSONArray condition = forec.getJSONArray("weather");
                    JSONObject condition1 = condition.getJSONObject(0);
                    String text = condition1.getString("main");

                    System.out.println("current text: "+ text);

                    System.out.println("inside for loop: "+ currentTemp) ;
                    mylist.condition = text;
                    mylist.curr_temp = currentTemp;

                    weatherList.add(mylist);


                    weatherList.add(mylist);

                }

            } catch (JSONException e) {
                System.out.println("json exeption" + e);
                e.printStackTrace();
            }

            return weatherList;
        }


    }
}
