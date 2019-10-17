package com.haitham.sunshineapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Setting extends AppCompatActivity {

    AutoCompleteTextView ed_location;
    String location;
    String[] cities= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ed_location=findViewById(R.id.ed_location);
        cities = getResources().getStringArray(R.array.Citis);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,cities );
        ed_location.setAdapter(adapter);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3DB2E9")));

        SharedPreferences shared = getSharedPreferences("mydata", MODE_PRIVATE);
        location = shared.getString("location", "Cairo");

        ed_location.setText(location);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        location = ed_location.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save) {
            ed_location = findViewById(R.id.ed_location);

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Do you want to change location!!");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    location = ed_location.getText().toString();
                    SharedPreferences shared = getSharedPreferences("mydata", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("location", location);
                    editor.apply();
                    Toast.makeText(getBaseContext(),"Location changed",Toast.LENGTH_LONG).show();
                    Intent in = new Intent(Setting.this,MainActivity.class);
                    startActivity(in);
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        }
        return true;
    }
}
