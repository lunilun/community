package com.example.communityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class splash extends AppCompatActivity {

    private AnimationDrawable point;
    private ImageView cha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        cha=findViewById(R.id.cha);
        point =(AnimationDrawable)cha.getBackground();
        point.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1800);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}