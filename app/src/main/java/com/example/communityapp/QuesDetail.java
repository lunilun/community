package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class QuesDetail extends AppCompatActivity implements View.OnClickListener {

    private TextView detail_title,detail_text;
    private Button detail_revise, detail_cancel;
    //private ImageView detail_img;
    private DatabaseReference DDatabase;

    private List<String> dataArray =  new ArrayList<String>();
    String user,name;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ques_detail);

        Intent intent = getIntent();
        String data = intent.getStringExtra("title"); //글 제목

        detail_title=findViewById(R.id.detail_title); detail_text=findViewById(R.id.detail_text);
        detail_revise=findViewById(R.id.detail_revise); detail_cancel=findViewById(R.id.detail_cancel);
        //detail_img=findViewById(R.id.detail_img);

        user=((MyQuestion)MyQuestion.context).key; //사용자
        name=((MyQuestion)MyQuestion.context).point; //폴더명
        //Toast.makeText(QuesDetail.this,user+"/"+name,Toast.LENGTH_SHORT).show();
        //Toast.makeText(QuesDetail.this,data,Toast.LENGTH_SHORT).show();
        if(name.equals("Suggestions")) DDatabase=FirebaseDatabase.getInstance().getReference("Question").child(user).child(data);
        else DDatabase=FirebaseDatabase.getInstance().getReference("folder").child(user).child(name).child(data);
        DDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    dataArray.add(Snapshot.getValue().toString());
                }
                detail_title.setText(dataArray.get(1)); detail_text.setText(dataArray.get(0));
                String img = dataArray.get(1)+".png";
                /*if(name.equals("Suggestion")){
                    storageRef=storage.getReference()
                            .child("Question")
                            .child(user)
                            .child(img);
                }else{
                    storageRef=storage.getReference()
                            .child("folder")
                            .child(user)
                            .child(name)
                            .child(img);
                }
                storageRef.getBytes(1024*1024)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                detail_img.setBackgroundResource(0);
                                detail_img.setImageBitmap(bitmap);
                            }
                        });*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dataArray.clear();
        detail_revise.setOnClickListener(this); detail_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detail_revise:
                Toast.makeText(QuesDetail.this,"수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.detail_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}