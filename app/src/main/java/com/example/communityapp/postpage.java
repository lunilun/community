package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class postpage extends AppCompatActivity implements View.OnClickListener{

    private Button detail,new_dir,btn_del,btn_re;
    private TextView card_writer,post_motto,post_mail,logout,update;
    private TextView card_title,card_date,tv_help;
    private ImageView img_help,img_date,post_profile;
    private CardView card_view;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    private Spinner sp_title;
    private List<String> spinnerArray =  new ArrayList<String>();
    private ArrayAdapter<String> spinnerAdapter;

    public static Context pContext;
    ArrayList<String> arr_name = new ArrayList<>();

    private new_folder new_folder;
    private update_member update_member;
    private EditText et;

    String check="";
    int checkCode=0;
    int position=0;
    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postpage);

        pContext=this;
        auth=FirebaseAuth.getInstance();
        //tv_title=findViewById(R.id.tv_title);
        post_mail=findViewById(R.id.post_mail); post_motto=findViewById(R.id.post_motto); card_writer=findViewById(R.id.card_writer);
        card_title=findViewById(R.id.card_title); card_date=findViewById(R.id.card_date); tv_help=findViewById(R.id.tv_help);
        img_help=findViewById(R.id.img_help); img_date=findViewById(R.id.img_date); post_profile=findViewById(R.id.post_profile);
        logout=findViewById(R.id.logout); update=findViewById(R.id.update);
        card_view=findViewById(R.id.card_view);
        detail=findViewById(R.id.detail); new_dir=findViewById(R.id.new_dir); btn_del=findViewById(R.id.btn_del); btn_re=findViewById(R.id.btn_re);
        sp_title=findViewById(R.id.sp_title);

        card_view.setBackgroundColor(Color.parseColor("#800000"));
        Date Current = Calendar.getInstance().getTime();
        String today = new SimpleDateFormat("yyyy/MM/dd",Locale.getDefault()).format(Current);
        card_title.setText(auth.getCurrentUser().getEmail());
        card_date.setText(today);

        while(true){
            String[] arr = auth.getCurrentUser().getEmail().split("");
            for(int i=0;i<arr.length;i++){
                if(arr[i].equals("@")) break;
                else check+=arr[i];
            }
            break;
        }
        try {
            post_mail.setText(auth.getCurrentUser().getEmail());
            databaseReference=FirebaseDatabase.getInstance().getReference().child("member_list").child(check);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot Snapshot : snapshot.getChildren()) {
                        arr_name.add(Snapshot.getValue().toString());
                    }
                    post_motto.setText(arr_name.get(2)+" "+arr_name.get(0));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){

        }
        arr_name.clear();

        try {
            databaseReference=FirebaseDatabase.getInstance().getReference().child("folder").child(check);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    spinnerArray.clear();
                    spinnerArray.add("Suggestions");
                    for (DataSnapshot spinnerSnapshot : dataSnapshot.getChildren()){
                        spinnerArray.add(spinnerSnapshot.getKey());
                    }
                    spinnerAdapter = new ArrayAdapter<String>(postpage.this, android.R.layout.simple_spinner_item, spinnerArray);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_title.setAdapter(spinnerAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){

        }
        logout.setOnClickListener(this); card_view.setOnClickListener(this);
        detail.setOnClickListener(this); new_dir.setOnClickListener(this);
        btn_del.setOnClickListener(this); btn_re.setOnClickListener(this);
        img_date.setOnClickListener(this); update.setOnClickListener(this);

        String img = check+"_profile.png";
        storageRef=storage.getReference()
                .child("profile")
                .child(img);
        storageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        post_profile.setBackgroundResource(0);
                        post_profile.setImageBitmap(bitmap);
                    }
                });
        Intent intent = getIntent();
        String msg = intent.getStringExtra("name");
        code = intent.getIntExtra("code",0);
        Toast.makeText(postpage.this,msg,Toast.LENGTH_SHORT).show();

        sp_title.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    btn_del.setEnabled(false);
                    btn_re.setEnabled(false);
                    btn_del.setBackgroundResource(0);
                    btn_re.setBackgroundResource(0);
                    if(code==0){
                        tv_help.setText("건의 사항");
                        checkCode=0;
                    }
                    else if(code==1){
                        tv_help.setText("サポート");
                        checkCode=1;
                    }
                    else if(code==2){
                        tv_help.setText("Suggestions");
                        checkCode=2;
                    }
                    img_help.setBackgroundResource(R.drawable.help);
                    card_writer.setText("From Team leader");
                }
                else{
                    btn_del.setEnabled(true);
                    btn_re.setEnabled(true);
                    btn_del.setBackgroundResource(R.drawable.x);
                    btn_re.setBackgroundResource(R.drawable.renew);
                    img_help.setBackgroundResource(R.drawable.idea);
                    tv_help.setText(spinnerAdapter.getItem(i));
                    card_writer.setText("From My Opinion");
                }
                position=i;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_view:
                if(!arr_name.get(0).equals("관리자") ||
                        (arr_name.get(0).equals("관리자") && !spinnerAdapter.getItem(position).equals("Suggestions"))){
                    Intent intent = new Intent(postpage.this,newArticle.class);
                    intent.putExtra("code",checkCode);
                    intent.putExtra("point",spinnerAdapter.getItem(position));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(postpage.this,"관리자는 사용할 수 없습니다.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.logout:
                auth.signOut();
                finish();
                Toast.makeText(postpage.this,"수고많으셨습니다.",Toast.LENGTH_SHORT).show();
                break;
            case R.id.detail:
                if(!arr_name.get(0).equals("관리자")  || (arr_name.get(0).equals("관리자")&&!spinnerAdapter.getItem(position).equals("Suggestions"))){
                    Intent intent1 = new Intent(postpage.this,MyQuestion.class);
                    intent1.putExtra("code2",checkCode);
                    intent1.putExtra("array",spinnerAdapter.getItem(position));
                    startActivity(intent1);
                }else{
                    Intent intent2 = new Intent(postpage.this,AdminQuestion.class);
                    startActivity(intent2);
                }
                break;
            case R.id.new_dir:
                new_folder = new new_folder(postpage.this);
                new_folder.setCanceledOnTouchOutside(false);
                new_folder.setCancelable(false);
                new_folder.show();
                break;
            case R.id.btn_del:
                //Toast.makeText(postpage.this,"삭제 준비 완료",Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder builder = new AlertDialog.Builder(postpage.this);
                builder.setTitle("주의").setMessage("해당 폴더의 모든 데이터가 삭제됩니다.\n정말 삭제하시겠습니까?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            i=position;
                            databaseReference=FirebaseDatabase.getInstance().getReference().child("folder").child(check).child(spinnerAdapter.getItem(i));
                            databaseReference.removeValue();
                            spinnerArray.remove(spinnerAdapter.getItem(i));
                            Toast.makeText(postpage.this,"삭제를 완료했습니다.",Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(postpage.this,"ERROR",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(postpage.this,"삭제를 취소하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.btn_re:
                Toast.makeText(postpage.this,"미구현 버튼입니다",Toast.LENGTH_SHORT).show();
                /*et = new EditText(postpage.this);
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(postpage.this);
                builder2.setTitle("확인").setMessage("해당 폴더명을 변경하시겠습니까\n변경하실 이름을 입력해주세요.");
                builder2.setView(et);
                builder2.setIcon(R.drawable.question);
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference=FirebaseDatabase.getInstance().getReference("folder").child(check)*//*.child(spinnerAdapter.getItem(position))*//*;
                        //update();
                        *//*databaseReference.setValue(et.getText().toString());
                        tv_help.setText(et.getText().toString());
                        spinnerArray.remove(position);
                        spinnerArray.add(position,et.getText().toString());*//* //[key value]만 변경하는 방법 찾기
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(postpage.this,"변경을 취소하였습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog alertDialog2 = builder2.create();
                alertDialog2.show();*/
                break;
            case R.id.img_date:
                Intent intent = new Intent(postpage.this, user_date.class);
                intent.putExtra("code3",checkCode);
                startActivity(intent);
                break;
            case R.id.update:
                update_member = new update_member(postpage.this);
                update_member.setCanceledOnTouchOutside(false);
                update_member.setCancelable(false);
                update_member.show();
                break;
        }
    }

    /*public void update(){
        databaseReference=FirebaseDatabase.getInstance().getReference("folder").child(check).child(spinnerAdapter.getItem(position));
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put(et.getText().toString(), "empty");
        databaseReference.updateChildren(updateMap);
    }*/

    @Override public void onBackPressed() {
        //super.onBackPressed();
        }
}