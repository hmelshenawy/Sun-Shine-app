package com.haitham.sunshineapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class temp extends AppCompatActivity {

    TextView tvTemp;
    Calendar calendar;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        tvTemp = findViewById(R.id.tvTempF);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, dd-MMM, hh:mm:ssa");
        long t = calendar.getTime().getTime();

        dateFormat.format(t);

        Intent in = getIntent();
        String todayTemp = in.getStringExtra("t");
        String humidity = in.getStringExtra("h");
        String Location = in.getStringExtra("l");
        String condition = in.getStringExtra("c");
        String localtime = in.getStringExtra("localtime");
        String country = in.getStringExtra("country");

//        tvTemp.setText(Location+", ");
//        tvTemp.append(country+ ":\n");

        tvTemp.setText( todayTemp + "°C\n");
        tvTemp.append("Humidity " + humidity + "%\n");
        tvTemp.append(condition);
        tvTemp.append("\n"+localtime);

        setTitle(Location+","+ country);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.temp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()== R.id.mnu_share){

            Intent in = new Intent(Intent.ACTION_VIEW);
            in.setData(Uri.parse("geo:1233,1234"));
            startActivity(in.createChooser(in,"mchooser"));

        }

        return super.onOptionsItemSelected(item);
    }
}