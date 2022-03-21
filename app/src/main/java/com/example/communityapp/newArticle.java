package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class newArticle extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference aDatabase;
    private TextView new_title,new_main;
    private Button btn_save,btn_cancel;
    //private ImageView article_img;
    private EditText article_text,article_title;

    private LinearLayoutManager linearLayoutManager;
    private SubAdap subAdap;
    private ArrayList<Article> array;

    private DatabaseReference nDatabase;
    private FirebaseAuth auth;
    private Uri filePath;
    String ck="";
    int code;
    String point;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);

        Intent intent = getIntent();
        code = intent.getIntExtra("code",0);
        point = intent.getStringExtra("point");

        new_title=findViewById(R.id.new_title); new_main=findViewById(R.id.new_main);
        btn_save=findViewById(R.id.btn_save); btn_cancel=findViewById(R.id.btn_cancel);
        article_text=findViewById(R.id.article_text); article_title=findViewById(R.id.article_title);

        auth=FirebaseAuth.getInstance();
        btn_save.setOnClickListener(this); btn_cancel.setOnClickListener(this);

        while(true){
            String[] arr = auth.getCurrentUser().getEmail().split("");
            for(int i=0;i<arr.length;i++){
                if(arr[i].equals("@")) break;
                else ck+=arr[i];
            }
            break;
        }

        if(code==0){
            new_title.setText("제목");
            new_main.setText(" 내용");
            btn_save.setText("저장");
            btn_cancel.setText("취소");
        }
        else if(code==1){
            new_title.setText("タイトル");
            new_main.setText("  ないよう");
            btn_save.setText("セーブ");
            btn_cancel.setText("キャンセル");
        }
        else if(code==2){
            new_title.setText("Title");
            new_main.setText("  Content");
            btn_save.setText("Save");
            btn_cancel.setText("Cancel");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_save:
                AddDatabase();
                //uploadFile();
                break;
            case R.id.btn_cancel:
                finish();
                break;
            /*case R.id.article_img:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "사진을 선택하세요."), 0);
                break;*/
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode ==RESULT_OK){
            filePath = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                article_img.setBackgroundResource(0);
                article_img.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }*/

    public void AddDatabase(){
        if(point.equals("Suggestions") || point.equals("")){
            nDatabase= FirebaseDatabase.getInstance().getReference().child("Question").child(ck);
            Map<String, Object> listUpdate = new HashMap<>();
            Map<String, Object> listValues = null;
            Article article = new Article(article_title.getText().toString(),article_text.getText().toString());
            listValues = article.Map();
            listUpdate.put(article_title.getText().toString(), listValues);
            nDatabase.updateChildren(listUpdate);
        }
        else{
            nDatabase=FirebaseDatabase.getInstance().getReference().child("folder").child(ck).child(point);
            Map<String, Object> listUpdate = new HashMap<>();
            Map<String, Object> listValues = null;
            Article article = new Article(article_title.getText().toString(),article_text.getText().toString());
            listValues = article.Map();
            listUpdate.put(article_title.getText().toString(), listValues);
            nDatabase.updateChildren(listUpdate);
        }
        finish();
    }

    private void uploadFile(){
        if(filePath !=null){
            final ProgressDialog progressDialog = new ProgressDialog(newArticle.this);
            progressDialog.setTitle("등록중");
            progressDialog.show();
            String filename = article_title.getText().toString()+".png";
            FirebaseStorage storage = FirebaseStorage.getInstance();
            //storage 주소와 폴더 파일명을 지정해 준다.
            if(point.equals("Suggestions") || point.equals("")){
                storageRef = storage.getReferenceFromUrl("gs://community-dbca3.appspot.com").child("Question").child(ck).child(filename);
            }
            else{
                storageRef = storage.getReferenceFromUrl("gs://community-dbca3.appspot.com").child("folder").child(ck).child(point).child(filename);
            }
            storageRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
                            progressDialog.dismiss();
                        }
                    });
        }
    }

}