package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class update_date extends AppCompatActivity implements View.OnClickListener {

    private EditText et2;
    private Button button1,button2,button3,button4,button5;
    private NumberPicker pick1,pick2,pick3,pick4;
    private TextView textView3,et1;

    private String a,b,c,d,col,cur1;
    int m;
    String new_time="";
    int ye1,mo1,da1;
    ArrayList<String> time = new ArrayList<>();

    private FirebaseDatabase Database = FirebaseDatabase.getInstance();
    private DatabaseReference updateReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_date);

        Intent intent = getIntent();
        a=intent.getStringExtra("title");
        b=intent.getStringExtra("user");
        c=intent.getStringExtra("cont");
        d=intent.getStringExtra("date");

        et1=findViewById(R.id.et1); et2=findViewById(R.id.et2);
        textView3=findViewById(R.id.textView3);
        button1=findViewById(R.id.button1); button2=findViewById(R.id.button2); button3=findViewById(R.id.button3); button4=findViewById(R.id.button4); button5=findViewById(R.id.button5);
        pick1=findViewById(R.id.pick1); pick2=findViewById(R.id.pick2); pick3=findViewById(R.id.pick3); pick4=findViewById(R.id.pick4);

        m=((user_date)user_date.dateContext).code3;
        ye1=((user_date)user_date.dateContext).y; mo1=((user_date)user_date.dateContext).m; da1=((user_date)user_date.dateContext).d;
        cur1=String.valueOf(ye1)+String.valueOf(mo1)+String.valueOf(da1);

        pick1.setMaxValue(2); pick1.setMinValue(0);
        pick2.setMaxValue(9); pick2.setMinValue(0);
        pick3.setMaxValue(5); pick3.setMinValue(0);
        pick4.setMaxValue(9); pick4.setMinValue(0);

        if(m==0){
            textView3.setText("시작 시간");
        }
        else if(m==1){
            textView3.setText("スタートタイム.");
        }
        else if(m==2){
            textView3.setText("Start");
        }
        String[] div = d.split("");
        for(int i=0;i<div.length;i++){
            if(!div[i].equals(":")) time.add(div[i]);
        }
        timer(time.size());
        et1.setText(a); et2.setText(c);
        button1.setOnClickListener(this); button2.setOnClickListener(this); button3.setOnClickListener(this);
        button4.setOnClickListener(this); button5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button1:
                button1.setBackgroundResource(R.drawable.custom_green);
                button2.setBackgroundResource(R.drawable.custom_nomal);
                button3.setBackgroundResource(R.drawable.custom_nomal);
                col="green";
                break;
            case R.id.button2:
                button1.setBackgroundResource(R.drawable.custom_nomal);
                button2.setBackgroundResource(R.drawable.custom_yellow);
                button3.setBackgroundResource(R.drawable.custom_nomal);
                col="yellow";
                break;
            case R.id.button3:
                button1.setBackgroundResource(R.drawable.custom_nomal);
                button2.setBackgroundResource(R.drawable.custom_nomal);
                button3.setBackgroundResource(R.drawable.custom_red);
                col="red";
                break;
            case R.id.button4:
                /*a=intent.getStringExtra("title");
                b=intent.getStringExtra("user");
                c=intent.getStringExtra("cont");
                d=intent.getStringExtra("date");*/
                try {
                    if((!et2.getText().toString().equals(null)) && (!col.equals(null))){
                        new_time+=String.valueOf(pick1.getValue())+String.valueOf(pick2.getValue())+":"+String.valueOf(pick3.getValue())+String.valueOf(pick4.getValue());
                        updateReference=FirebaseDatabase.getInstance().getReference("schedule").child(cur1).child(b);
                        Map<String, Object> map = new HashMap<>();
                        Map<String, Object> itemMap = null;
                        listitem item = new listitem(b,et1.getText().toString(),new_time,col,et2.getText().toString());
                        itemMap = item.LMap();
                        map.put(et1.getText().toString(),itemMap);
                        updateReference.updateChildren(map);
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(update_date.this,"미입력 항목이 존재합니다.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.button5:
                finish();
                break;
        }
    }
    public void timer(int n){
        switch (n){
            case 3:
                pick1.setValue(0);
                pick2.setValue(Integer.parseInt(time.get(0)));
                pick3.setValue(Integer.parseInt(time.get(1)));
                pick4.setValue(Integer.parseInt(time.get(2)));
                break;
            case 4:
                pick1.setValue(Integer.parseInt(time.get(0)));
                pick2.setValue(Integer.parseInt(time.get(1)));
                pick3.setValue(Integer.parseInt(time.get(2)));
                pick4.setValue(Integer.parseInt(time.get(3)));
                break;
        }
    }
}