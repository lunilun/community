package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class user_date extends AppCompatActivity implements View.OnClickListener {

    private CalendarView calendar;
    private RecyclerView list_date;
    private Button btn_date1,btn_date2;
    private TextView date_title,now;

    private FirebaseDatabase database= FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    private LinearLayoutManager linearLayoutManager;
    private listAdap listAdap;
    private ArrayList<listitem> arrayList;

    public static Context dateContext;
    String text1,text2,text3,cur_date,getTime;
    int code3,y,m,d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        Intent intent = getIntent();
        code3=intent.getIntExtra("code3",0);

        calendar=findViewById(R.id.calendar);
        list_date=findViewById(R.id.list_date);
        btn_date2=findViewById(R.id.btn_date2); btn_date1=findViewById(R.id.btn_date1);
        date_title=findViewById(R.id.date_title); now=findViewById(R.id.now);
        dateContext=this;

        long no = System.currentTimeMillis();
        Date mDate = new Date(no);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMd");
        getTime = simpleDate.format(mDate);

        linearLayoutManager = new LinearLayoutManager(user_date.this);
        list_date.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        listAdap = new listAdap(arrayList,user_date.this);
        list_date.setAdapter(listAdap);


        if(code3==0) {
            text1="새 일정";
            text2="홈으로";
            text3="목록";
        }
        else if(code3==1) {
            text1="ニュー\nスケジュール";
            text2="ホーム";
            text3="リスト";
        }
        else if(code3==2){
            text1="New\nSchedule";
            text2="Home";
            text3="List";
        }
        btn_date1.setText(text1); btn_date2.setText(text2); date_title.setText(text3);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                y=year; m=month+1; d=dayOfMonth;
                cur_date=String.valueOf(y)+String.valueOf(m)+String.valueOf(d);
                now.setText(String.format("%d/ %d/ %d",year,month+1,dayOfMonth));

                databaseReference.child("schedule").child(cur_date).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                            //Toast.makeText(user_date.this,dateSnapshot.getKey(),Toast.LENGTH_LONG).show();
                            for(DataSnapshot childSnapshot : dateSnapshot.getChildren()){
                                //Toast.makeText(user_date.this,childSnapshot.getKey(),Toast.LENGTH_LONG).show();
                                listitem item = childSnapshot.getValue(listitem.class);
                                arrayList.add(item);
                            }
                        }
                        listAdap.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btn_date1.setOnClickListener(this); btn_date2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_date1:
                if(Integer.parseInt(getTime)<Integer.parseInt(cur_date)){
                    Intent intent = new Intent(user_date.this,new_date.class);
                    startActivity(intent);
                }
                else Toast.makeText(user_date.this,"지난 날(또는 당일)의 일정은 잡을 수 없습니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_date2:
                finish();
                //Toast.makeText(user_date.this,getTime+"/"+cur_date,Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}