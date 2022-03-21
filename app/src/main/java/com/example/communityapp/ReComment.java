package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReComment extends AppCompatActivity implements View.OnClickListener {

    private TextView re_title,re_cont;
    private Button btn_ycom,btn_ncom;
    private EditText re_review;
    public static Context context;

    private ArrayList<String> cont = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase= FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;

    String root,cel1,cel2,file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_comment);

        re_title=findViewById(R.id.re_title); re_cont=findViewById(R.id.re_cont);
        btn_ncom=findViewById(R.id.btn_ncom); btn_ycom=findViewById(R.id.btn_ycom);
        re_review=findViewById(R.id.re_review);
        context=this;

        btn_ncom.setOnClickListener(this); btn_ycom.setOnClickListener(this);

        Intent intent = getIntent();
        root = intent.getStringExtra("category");
        cel1= intent.getStringExtra("cel1"); //사용자명
        file = intent.getStringExtra("fileName");

        try {
            if(root.equals("Question")){
                databaseReference= FirebaseDatabase.getInstance().getReference("Question").child(cel1).child(file);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cont.clear();
                        for (DataSnapshot QuesSnapshot : snapshot.getChildren()){
                            cont.add(QuesSnapshot.getValue().toString());
                            //Toast.makeText(ReComment.this,QuesSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                        }
                        re_title.setText(cont.get(1)); re_cont.setText(cont.get(0));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }catch (Exception e){
            Toast.makeText(ReComment.this,"등록된 내용이 없습니다.",Toast.LENGTH_SHORT).show();
        }
        databaseReference=FirebaseDatabase.getInstance().getReference("Comment").child(cel1);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ContSnapshot : snapshot.getChildren()){
                    if(!ContSnapshot.getValue().equals(null) && ContSnapshot.getKey().equals(file)) {
                        re_review.setText(ContSnapshot.getValue().toString());
                        btn_ycom.setText("수정");
                    }
                    else{
                        btn_ycom.setText("저장");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ycom:
                String data = re_review.getText().toString();
                databaseReference.getDatabase().getReference("Comment").child(cel1).child(file).setValue(data);
                finish();
                break;
            case R.id.btn_ncom:
                finish();
                break;
        }
    }
}