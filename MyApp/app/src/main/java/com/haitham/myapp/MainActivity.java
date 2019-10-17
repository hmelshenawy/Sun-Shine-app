package com.haitham.myapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    TextView txtView;
    Button emailid;
    Button aboutMe;
    GestureDetector gest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailid = (Button) findViewById(R.id.emailid);
        aboutMe = (Button) findViewById(R.id.aboutMe);
        txtView = (TextView) findViewById(R.id.textView) ;
         gest = new GestureDetector(this, this);
        gest.setOnDoubleTapListener(this);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        txtView.setText("onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        txtView.setText("onShow");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        txtView.setText("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        txtView.setText("onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        txtView.setText("onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        txtView.setText("onFling");
        return false;
    }

    public void showme(View view) {

        String btnTxt;
       if( emailid.isPressed()){  startActivity(new Intent(this, email.class));}
        else if(aboutMe.isPressed()){ Intent in = new Intent(this,aboutme.class);
           startActivity(in);}
        btnTxt = emailid.getText().toString();

        //btnTxt = ((Button) view).getText().toString();

//        if (btnTxt.equals("Email")){
//            startActivity(new Intent(this, email.class));
//        }
//        else if (btnTxt.contains("About Me")){
//            Intent in = new Intent(this,aboutme.class);
//            startActivity(in);
//        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        txtView.setText("onSingleTapConfirm");
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        txtView.setText("onDoubleTapUp");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        txtView.setText("onDoubleTapEvent");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gest.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
