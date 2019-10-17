package com.haitham.azkar;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Page1 extends AppCompatActivity {

    ImageView az1ply , az1pus , az2ply, az2pus , az3ply, az3pus;
    MediaPlayer mp1, mp2, mp3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);
        az1ply = (ImageView) findViewById(R.id.az1ply);
        az1pus = (ImageView) findViewById(R.id.az1pus);
        az2ply = (ImageView) findViewById(R.id.az2ply);
        az2pus = (ImageView) findViewById(R.id.az2pus);
        az3ply = (ImageView) findViewById(R.id.az3ply);
        az3pus = (ImageView) findViewById(R.id.az3pus);


        mp1 = MediaPlayer.create(this, R.raw.mp01);
        mp2 = MediaPlayer.create(this, R.raw.mp02);
        mp3 = MediaPlayer.create(this,R.raw.mp03);

        az1ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (mp1.isPlaying()){
                mp1.pause();
                   Toast.makeText(getBaseContext(), "Pause", Toast.LENGTH_SHORT).show();}
               else {mp1.start();
                Toast.makeText(Page1.this, "Play", Toast.LENGTH_LONG).show();}

            }
        });
        az1pus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp1.pause();
                mp1.seekTo(0);
            }
        });
        az2ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp2.isPlaying()){
                    mp2.pause();}
                else{
                    mp2.start();}
            }
        });
        az2pus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp2.pause();
                mp2.seekTo(0);



            }
        });
        az3ply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mp3.isPlaying()){
                    mp3.pause();}
                else mp3.start();
            }
        });
        az3pus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp3.pause();
                mp3.seekTo(0);
            }
        });

    }
}
