package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyQuestion extends AppCompatActivity {

    public static Context context;
    private TextView my_title,tv_where;
    private RecyclerView recycler;

    private LinearLayoutManager linearLayoutManager;
    private SubAdap subAdap;
    private ArrayList<Question> array;

    private DatabaseReference QDatabase;
    private FirebaseAuth auth;
    String key="";
    String title="";
    int code2;
    String point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        auth = FirebaseAuth.getInstance();
        my_title=findViewById(R.id.my_title); tv_where=findViewById(R.id.tv_where);
        recycler=findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(MyQuestion.this);
        recycler.setLayoutManager(linearLayoutManager);
        context=this;

        array = new ArrayList<>();
        subAdap = new SubAdap(array,MyQuestion.this);
        recycler.setAdapter(subAdap);

        Intent intent = getIntent();
        code2 = intent.getIntExtra("code2",0);
        point = intent.getStringExtra("array");


        if(code2==0) {
            my_title.setText("마이 리스트");
        }
        else if(code2==1) {
            my_title.setText("マイリスト");
        }
        else if(code2==2) {
            my_title.setText("My List");
        }
        tv_where.setText("for "+point);

        while(true){
            String[] arr = auth.getCurrentUser().getEmail().split("");
            for(int i=0;i<arr.length;i++){
                if(arr[i].equals("@")) break;
                else key+=arr[i];
            }
            break;
        }
        title = my_title.getText().toString();
        if(point.equals("Suggestions")){
            QDatabase = FirebaseDatabase.getInstance().getReference("Question").child(key);
            QDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    array.clear();
                    try {
                        for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                            Question question = questionSnapshot.getValue(Question.class);
                            array.add(question);
                        }
                        subAdap.notifyDataSetChanged();
                    }catch (Exception e){
                        Toast.makeText(MyQuestion.this,"ERROR",Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            QDatabase = FirebaseDatabase.getInstance().getReference("folder").child(key).child(point);
            QDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    array.clear();
                    for (DataSnapshot mySnapshot : snapshot.getChildren()) {
                        if(!mySnapshot.getKey().equals("empty")){
                            Question question2 = mySnapshot.getValue(Question.class);
                            array.add(question2);
                        }
                    }
                    subAdap.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}