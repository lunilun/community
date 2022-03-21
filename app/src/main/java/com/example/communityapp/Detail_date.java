package com.example.communityapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Detail_date extends AppCompatActivity {

    private TextView textView1,textView2,textView3,textView4;
    private NumberPicker picker1,picker2,picker3,picker4;

    String ti,us,co,da,con;
    int n;
    ArrayList<String> temp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_date);

        Intent intent = getIntent();
        ti=intent.getStringExtra("title");
        us=intent.getStringExtra("user");
        co=intent.getStringExtra("color");
        da=intent.getStringExtra("date");
        con=intent.getStringExtra("cont");

        textView1=findViewById(R.id.textView1);textView2=findViewById(R.id.textView2);textView3=findViewById(R.id.textView3); textView4=findViewById(R.id.textView4);
        picker1=findViewById(R.id.picker1); picker2=findViewById(R.id.picker2); picker3=findViewById(R.id.picker3); picker4=findViewById(R.id.picker4);

        n=((user_date)user_date.dateContext).code3;

        picker1.setMaxValue(2); picker1.setMinValue(0);
        picker2.setMaxValue(9); picker2.setMinValue(0);
        picker3.setMaxValue(5); picker3.setMinValue(0);
        picker4.setMaxValue(9); picker4.setMinValue(0);

        textView1.setText(us);
        textView4.setText(con);

        if(n==0){
            textView2.setText("게시자: "+ti);
            textView3.setText("시작 시간");
        }
        else if(n==1){
            textView2.setText("ライター: "+ti);
            textView3.setText("スタートタイム.");
        }
        else if(n==2){
            textView2.setText("Writer: "+ti);
            textView3.setText("Start");
        }

        String[] div_date = da.split("");
        for(int i=0;i<div_date.length;i++){
            if(!div_date[i].equals(":")) temp.add(div_date[i]);
        }

        timer(temp.size());

        if(co.equals("red")) textView1.setTextColor(Color.RED);
        else if(co.equals("yellow")) textView1.setTextColor(Color.parseColor("#FF8C00"));
        else if(co.equals("green")) textView1.setTextColor(Color.GREEN);
    }

    public void timer(int n){
        switch (n){
            case 3:
                picker1.setValue(0);
                picker2.setValue(Integer.parseInt(temp.get(0)));
                picker3.setValue(Integer.parseInt(temp.get(1)));
                picker4.setValue(Integer.parseInt(temp.get(2)));
                break;
            case 4:
                picker1.setValue(Integer.parseInt(temp.get(0)));
                picker2.setValue(Integer.parseInt(temp.get(1)));
                picker3.setValue(Integer.parseInt(temp.get(2)));
                picker4.setValue(Integer.parseInt(temp.get(3)));
                break;
        }
    }
}