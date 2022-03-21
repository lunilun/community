package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adminpage extends AppCompatActivity implements View.OnClickListener{

    EditText admin_name,admin_number,admin_grade,admin_mail;
    Button btn_join,btn_search;
    ImageView admin_img;
    RecyclerView list_mem;

    private LinearLayoutManager linearLayoutManager;
    private MainAdap mainAdap;
    private ArrayList<Member> arrayList;

    String name;
    String number;
    String grade;
    String mail;
    String sort = "mail";

    private Uri filePath;
    public String url;

    private FirebaseDatabase aDatabase;
    private DatabaseReference aDatabaseReference;
    private ChildEventListener aChildEventListener;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminpage);

        admin_name=findViewById(R.id.admin_name); admin_grade=findViewById(R.id.admin_grade); admin_number=findViewById(R.id.admin_number); admin_mail=findViewById(R.id.admin_mail);
        btn_join=findViewById(R.id.btn_join); btn_search=findViewById(R.id.btn_search);
        admin_img=findViewById(R.id.admin_img);

        list_mem = findViewById(R.id.list_mem);
        linearLayoutManager = new LinearLayoutManager(adminpage.this);
        list_mem.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();
        mainAdap = new MainAdap(arrayList,adminpage.this);
        list_mem.setAdapter(mainAdap);

        mAuth = FirebaseAuth.getInstance();

        getFirebaseDatabase();
        btn_join.setOnClickListener(this); btn_search.setOnClickListener(this);
        admin_img.setOnClickListener(this);
    }

    public void postFirebaseDatabase(){
        aDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        Member member = new Member(name,number,grade,mail);
        postValues = member.toMap();
        childUpdates.put("/member_list/"+mail, postValues);
        aDatabaseReference.updateChildren(childUpdates);
    }

    public void getFirebaseDatabase(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                    Member get = memberSnapshot.getValue(Member.class);
                    arrayList.add(get);
                }
                mainAdap.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Query sortbyAge = FirebaseDatabase.getInstance().getReference().child("member_list").orderByChild(sort);
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    private void createUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(adminpage.this,"등록 완료",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(adminpage.this,"등록 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_join:
                if(admin_name.getText().toString().equals("") || admin_number.getText().toString().equals("") || admin_grade.getText().toString().equals("") || admin_mail.getText().toString().equals("")){
                    Toast.makeText(adminpage.this,"빈칸이 존재합니다.",Toast.LENGTH_SHORT).show();
                }
                else if(admin_number.getText().toString().length()<6){
                    Toast.makeText(adminpage.this,"년도를 제외한 사원번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    name = admin_name.getText().toString();
                    number = admin_number.getText().toString();
                    grade = admin_grade.getText().toString();
                    mail = admin_mail.getText().toString();

                    uploadFile();
                    postFirebaseDatabase();
                    getFirebaseDatabase();
                    createUser(mail+"@company.com",number);

                    admin_name.requestFocus(); admin_name.setCursorVisible(true);
                    admin_name.setText(""); admin_number.setText("");
                    admin_grade.setText(""); admin_mail.setText("");
                    admin_img.setImageResource(R.drawable.profile);
                }
                break;
            case R.id.admin_img:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
                break;
            case R.id.btn_search:
                //aDatabaseReference.child("test").push().setValue("2");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0/*1*/ && resultCode ==RESULT_OK){
            filePath = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                admin_img.setImageBitmap(bitmap);
                /*InputStream in = getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                admin_img.setImageBitmap(img);*/
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void uploadFile(){
        if(filePath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(adminpage.this);
            progressDialog.setTitle("등록중");
            progressDialog.show();

            String filename = admin_mail.getText().toString()+"_profile.png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //storage 주소와 폴더 파일명을 지정해 준다.
            final StorageReference storageRef = storage.getReferenceFromUrl("gs://community-dbca3.appspot.com").child("profile/" + filename);
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            /*storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url=uri.toString();
                                }
                            });*/
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            @SuppressWarnings("VisibleForTests")
                            double progress = (100 * snapshot.getBytesTransferred()) /  snapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%");
                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "사진을 선택해주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}