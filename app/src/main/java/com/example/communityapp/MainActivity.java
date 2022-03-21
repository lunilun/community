package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn1,find_pw;
    private EditText et_num,et_pw;
    private ImageView img_admin,korea,japan,america;
    private TextView explain,explain_pw,app_title;
    private CheckBox id_check;
    private Animation animation;

    private SharedPreferences save;
    private boolean saveLoginData;
    private String id;

    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    private long backBtnTime=0;
    public int textCode=0;
    private String error="메일(비밀번호)을 입력해주세요";
    private String date;

    private finish finish;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext=this;

        btn1=findViewById(R.id.btn1); find_pw=findViewById(R.id.find_pw);
        app_title=findViewById(R.id.app_title);
        et_num=findViewById(R.id.et_num); et_pw=findViewById(R.id.et_pw);
        img_admin=findViewById(R.id.img_admin);
        explain=findViewById(R.id.explain); explain_pw=findViewById(R.id.explain_pw);
        korea=findViewById(R.id.korea); japan=findViewById(R.id.japan); america=findViewById(R.id.america);
        id_check=findViewById(R.id.id_check);
        korea.setOnClickListener(this); japan.setOnClickListener(this); america.setOnClickListener(this);
        find_pw.setOnClickListener(this);

        animation = new AlphaAnimation(0.0f,1.0f); //투명도 설정(float형이고 범위는 0.0~1.0)
        animation.setDuration(800);
        animation.setStartOffset(50);
        animation.setRepeatCount(Animation.INFINITE);
        find_pw.startAnimation(animation);

        popup();
        save = getSharedPreferences("save",MODE_PRIVATE);
        load();
        if(saveLoginData){
            et_num.setText(id);
            id_check.setChecked(saveLoginData);
        }
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("member_list");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDatabase.child("Test").push().setValue(2);
                if(et_num.getText().toString().equals("") || et_pw.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this,error,Toast.LENGTH_SHORT).show();
                }
                else{
                    String email =et_num.getText().toString().trim();
                    String password = et_pw.getText().toString().trim();
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                save();
                                if(id_check.isChecked()){
                                    id_check.setChecked(true);
                                }
                                else{
                                    et_num.setText("");
                                }
                                Intent intent = new Intent(MainActivity.this,postpage.class);
                                intent.putExtra("name",firebaseAuth.getCurrentUser().getEmail()+"님 환영합니다.");
                                intent.putExtra("code",textCode);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainActivity.this,"이메일(비밀번호) 오류입니다.",Toast.LENGTH_SHORT).show();
                                et_num.setText("@company.com");
                                et_pw.setText("");
                            }
                        }
                    });
                    et_pw.setText("");
                }
            }
        });
        img_admin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final EditText txtEdit = new EditText(MainActivity.this);

                AlertDialog.Builder clsBuilder = new AlertDialog.Builder( MainActivity.this );
                clsBuilder.setTitle("관리자 비밀번호 입력");
                clsBuilder.setView(txtEdit);
                clsBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick( DialogInterface dialog, int which) {
                                String strText = txtEdit.getText().toString();
                                if(strText.equals("886412")){
                                    Toast.makeText(MainActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,adminpage.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                                else if(strText.equals(null) || strText.equals("")){
                                    Toast.makeText(MainActivity.this,"비밀번호 미입력",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"비밀번호 오류",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                clsBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                clsBuilder.show();
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.korea:
                find_pw.setText("비밀번호 찾기");
                explain.setText("이메일");
                explain_pw.setText("비밀번호");
                btn1.setText("로그인");
                error="메일을 입력해주세요";
                app_title.setText("우뮤니티");
                id_check.setText("이메일 저장");
                app_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
                textCode=0;
                break;
            case R.id.japan:
                find_pw.setText("サーチパスワード");
                explain.setText("メールアドレス");
                explain_pw.setText("パスワード");
                btn1.setText("ログイン");
                error="メールをいれてくださ";
                app_title.setText("ウミュニティ");
                id_check.setText("メールセーブ");
                app_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,30);
                textCode=1;
                break;
            case R.id.america:
                find_pw.setText("Check Password");
                explain.setText("E-Mail");
                explain_pw.setText("Password");
                btn1.setText("Login");
                error="Please enter your Mail";
                app_title.setText("Oumunity");
                id_check.setText("Mail Save");
                app_title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,50);
                textCode=2;
                break;
            case R.id.find_pw:
                Intent intent = new Intent(MainActivity.this,findpwd.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences("File",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(id_check.isChecked()){
            String text = et_num.getText().toString();
            editor.putString("text",text);
        }
        else{
            editor.putString("text","");
        }
        editor.commit();
    }

    public void popup(){
        CustomDialog dialog = new CustomDialog(MainActivity.this);
        dialog.callDialog();
        /*final View pop1 = getLayoutInflater().inflate(R.layout.info_pop, null);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setView(pop1);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();*/
    }

    public void save(){
        SharedPreferences.Editor editor=save.edit();
        editor.putBoolean("SAVE_LOGIN_DATA",id_check.isChecked());
        editor.putString("ID",et_num.getText().toString().trim());
        editor.apply();
    }

    public void load(){
        saveLoginData = save.getBoolean("SAVE_LOGIN_DATA",false);
        id = save.getString("ID","");
    }

    @Override
    public void onBackPressed() {
        finish = new finish(MainActivity.this);
        finish.setCanceledOnTouchOutside(false);
        finish.setCancelable(false);
        finish.show();
        /*long curTime = System.currentTimeMillis();
        long gapTime = curTime-backBtnTime;

        if(gapTime>=0 && gapTime<=2000){
            super.onBackPressed();
        }
        else{
            backBtnTime=curTime;
            Toast.makeText(this,"정말 종료하시겠습니까?",Toast.LENGTH_SHORT).show();
        }*/
    }
}