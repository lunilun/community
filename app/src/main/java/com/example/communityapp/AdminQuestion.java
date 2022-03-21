package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminQuestion extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseAuth auth;

    private ArrayList<String> spinner_one = new ArrayList<>();
    private ArrayList<String> spinner_two = new ArrayList<>();
    //private ArrayList<String> spinner_three = new ArrayList<>();
    private ArrayAdapter<String> spinner1Adapter;
    private ArrayAdapter<String> spinner2Adapter;
    //private ArrayAdapter<String> spinner3Adapter;

    private Spinner spinner1,spinner2,spinner3;

    private LinearLayoutManager linearLayoutManager;
    private AdminAdap adminAdap;
    private ArrayList<Question> array;
    private RecyclerView admin_recycler;

    public static Context aContext;
    String category,sel1,sel2,check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_question);

        auth=FirebaseAuth.getInstance();
        spinner1=findViewById(R.id.spinner1); spinner2=findViewById(R.id.spinner2); //spinner3=findViewById(R.id.spinner3);
        admin_recycler=findViewById(R.id.admin_recycler);
        aContext=this;

        linearLayoutManager = new LinearLayoutManager(AdminQuestion.this);
        admin_recycler.setLayoutManager(linearLayoutManager);
        array = new ArrayList<>();
        adminAdap = new AdminAdap(array,AdminQuestion.this);
        admin_recycler.setAdapter(adminAdap);

        while(true){
            String[] arr = auth.getCurrentUser().getEmail().split("");
            for(int i=0;i<arr.length;i++){
                if(arr[i].equals("@")) break;
                else check+=arr[i];
            }
            break;
        }

       /* ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot adminSnapshot : snapshot.getChildren()){
                    if(!adminSnapshot.getKey().equals("member_list")) spinner_one.add(adminSnapshot.getKey());
                    //Toast.makeText(AdminQuestion.this,adminSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        //spinner_one.clear(); spinner_two.clear(); spinner_three.clear();
        spinner_one.add("Question"); //spinner_one.add("folder");
        spinner1Adapter = new ArrayAdapter<String>(AdminQuestion.this, android.R.layout.simple_spinner_item, spinner_one);
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(spinner1Adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner1Adapter.getItem(i).equals("Question")){
                    category=spinner1Adapter.getItem(i);
                    ref=FirebaseDatabase.getInstance().getReference("Question");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            spinner_two.clear();
                            for (DataSnapshot postSnapshot : snapshot.getChildren()){
                                spinner_two.add(postSnapshot.getKey());
                            }
                            spinner2Adapter = new ArrayAdapter<String>(AdminQuestion.this, android.R.layout.simple_spinner_item, spinner_two);
                            spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(spinner2Adapter);
                            spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    sel1=spinner2Adapter.getItem(i);
                                    ref=FirebaseDatabase.getInstance().getReference("Question").child(spinner2Adapter.getItem(i));
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            array.clear();
                                            for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                                                Question question = itemSnapshot.getValue(Question.class);
                                                array.add(question);
                                            }
                                            adminAdap.notifyDataSetChanged();
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}