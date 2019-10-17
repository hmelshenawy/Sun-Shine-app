package com.haitham.sunshineapp;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import static com.haitham.sunshineapp.WeatherApplication.CHANNEL_ID;


public class MainActivity extends AppCompatActivity {

    //String  forcastURL = "https://api.apixu.com/v1/forecast.json?key=719c84279eb9431dbe4204412191908&q=cairo&days=7";
    String forcastURL_OpenWeather = "https://api.openweathermap.org/data/2.5/forecast?q=cairo&APPID=76c89581cd5d04d2863421315e34f260";
    MyAdapter forcastAdapter;
    ListView lvforcast;
    String URI_BASE= "https://api.openweathermap.org/data/2.5/forecast?q=";
    String URI_DAYS = "&APPID=76c89581cd5d04d2863421315e34f260";
    StringBuilder URIBUILDER;
    Data_Retrive my;
    //URLThreading my;
    String location;
    Weather_DataBase_Adapter mDB_Adaptor;
    ArrayList forcatlist;
    TextView tvlocation;
    TextView tvdate;
    Calendar calendar;
    SimpleDateFormat dateFormat;

    public String uriMaker(){

        loadData();

        URIBUILDER = new StringBuilder();
        URIBUILDER.append(URI_BASE);
        URIBUILDER.append(location);
        URIBUILDER.append(URI_DAYS);

        return URIBUILDER.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Sun Shine Weather");
        lvforcast = findViewById(R.id.listview_forcast);
         tvlocation = findViewById(R.id.tv_location);
         tvdate = findViewById(R.id.tvDate);
         calendar = Calendar.getInstance();
         dateFormat = new SimpleDateFormat("EEE, dd-MMM");

        my = new Data_Retrive(this, uriMaker());

        if (internet_connection()) {

            my.execute();

        }
        else {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("No Internet connection!!");
            alert.setTitle("Error!!");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();
        }

        forcastAdapter = new MyAdapter(this, dataBase_Recall());
        lvforcast.setAdapter(forcastAdapter);
        tvlocation.setText(location);

        lvforcast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                String mforcast = "Temprature: "+forcastAdapter.getItem(i).temp+"°C"+"\n"+
//                        "Humidity: "+forcastAdapter.getItem(i).humidity+"%\n"+forcastAdapter.getItem(i).condition;

                Animation animation = new ScaleAnimation(1,1,1,2);

                animation.setDuration(300);

                forcastAdapter.selectedItem(i);

                lvforcast.setAdapter(forcastAdapter);

                forcastAdapter.notifyDataSetChanged();

            }
        });


    }

    public void datBase_Update(ArrayList<Forcast_List> list){

        mDB_Adaptor = new Weather_DataBase_Adapter(getBaseContext());

        for (int i = 0; i <list.size() ; i++) {

            long id = mDB_Adaptor.insertData(list.get(i).dt_txt, list.get(i).temp,list.get(i).curr_temp,
                                            list.get(i).condition, list.get(i).humidity);
           // System.out.println(id);
        }
    }

    public ArrayList<Forcast_List> dataBase_Recall(){
        mDB_Adaptor = new Weather_DataBase_Adapter(this);
         forcatlist = mDB_Adaptor.getData();

        return forcatlist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.refresh) {
                loadData();

            if (internet_connection()) {

                Data_Retrive my2 = new Data_Retrive(this, uriMaker());
                my2.execute();

        }  else {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Check Internet connection!!");
            alert.setTitle("Error!!");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        }


        }
        if (item.getItemId() == R.id.temp) {

            String todayTemp = my.currentTemp;
            String todayHumidity = my.currentHumidity;
            String  CUR_LOCATTION= my.CUR_LOCATION;
            String condition = my.weatherList.get(0).condition;
            String localtime = my.localtime;
            String country = my.country;
            Intent in = new Intent(this, temp.class);
            in.putExtra("t", todayTemp);
            in.putExtra("h", todayHumidity);
            in.putExtra("l", CUR_LOCATTION);
            in.putExtra("c", condition);
            in.putExtra("country", country);
            in.putExtra("localtime", localtime);
            startActivity(in);

        }
        if (item.getItemId() == R.id.setting) {
            Intent in = new Intent(this,Setting.class);
            startActivity(in);
        }
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void alarm_Set(int hr, int min){

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd, HH:mma");
        String s = String.valueOf(simpleDateFormat.format(calendar.getTime().getTime()));

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent in = new Intent(MainActivity.this, M_Alert_Manger.class);
        in.putExtra("temp", forcastAdapter.getItem(0).temp);
        in.putExtra("location", my.CUR_LOCATION);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 13, in, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        System.out.println(s);

    }


    protected void dataStore() {

        SharedPreferences shared = getSharedPreferences("mydata", MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("location", location);
        editor.apply();
    }


    protected void loadData() {

        SharedPreferences shared = getSharedPreferences("mydata", MODE_PRIVATE);
        location = shared.getString("location", "Cairo");
        Type typ = new TypeToken<Forcast_List[]>() {}.getType();
        System.out.println("current location is "+ location);
    }

    boolean internet_connection(){

        //Check if connected to internet, output accordingly
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

     class Data_Retrive extends AsyncTask<String, Void, Void> {

        Context context;
        String urlString;
        URL url1;

        //URLConnection urlconnect;
        HttpURLConnection urlconnect;
        String CUR_LOCATION;
        Scanner in;
        StringBuffer buffer;
        String result ;
        String currentTemp;
        String currentHumidity;
        ArrayList<Forcast_List> weatherList = new ArrayList();
        String country;
        String localtime;
        boolean isComplete;


        public Data_Retrive(Context context, String urlString) {
            this.context = context;
            this.urlString = urlString;
        }


        @Override
        protected Void doInBackground(String... strings) {

            buffer = new StringBuffer(" ");
            try {

                url1 = new URL(urlString);
                urlconnect = (HttpURLConnection) url1.openConnection();
                urlconnect.connect();

                if (urlconnect.getResponseCode() >= 400)
                {

                    isComplete = false;
                    //Toast.makeText(context, "Location is invalid!! ", Toast.LENGTH_SHORT).show();
                    System.out.println(urlconnect.getResponseCode());
                    System.out.println(isComplete);

                   // cancel(true) ;

                    return null;

                }


                System.out.println(isComplete);
                System.out.println( urlconnect.getResponseCode() );

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
            isComplete = true;

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if ( isComplete == false ){

                Toast.makeText(context, "Location is invalid!!", Toast.LENGTH_SHORT).show();

                return;
            }
            else if ( isComplete == true ){

                tvlocation.setText(my.CUR_LOCATION+ ", " + my.country);
                tvdate.setText(dateFormat.format(calendar.getTimeInMillis()));

                mDB_Adaptor.delete_AllData();
                datBase_Update(my.weatherList);
                forcastAdapter = new MyAdapter(context, dataBase_Recall());
                lvforcast.setAdapter(forcastAdapter);
                forcastAdapter.notifyDataSetChanged();
                Toast.makeText(context, "List updated!!", Toast.LENGTH_LONG).show();

                WeatherApplication.location = my.CUR_LOCATION;
                WeatherApplication.temp = forcastAdapter.getItem(0).temp;

                ComponentName componentName = new ComponentName(MainActivity.this, MJob_Intent_Service.class);

                JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                        .setPeriodic(30 * 60 * 1000)
                        .setPersisted(true)
                        .build();

                JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
                int i = jobScheduler.schedule(jobInfo);
                System.out.println("Job schedule " + i);

            }

        }


        public ArrayList<Forcast_List> jasonGetter(String r) {

            try {

                System.out.println(r);

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

                }

            } catch (JSONException e) {
                System.out.println("json exeption" + e);
                e.printStackTrace();
            }

            return weatherList;
        }


    }
}
