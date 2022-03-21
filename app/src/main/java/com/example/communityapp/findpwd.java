package com.example.communityapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class findpwd extends AppCompatActivity {

    private EditText find_mail,find_name;
    private TextView mail,name,main,res_tv;
    private Button btn_find,btn_end,btn_clear;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private ArrayList<String> strArray = new ArrayList<>();

    String strName="";

    String anw;
    int language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);

        find_mail=findViewById(R.id.find_mail); find_name=findViewById(R.id.find_name);
        mail=findViewById(R.id.mail); name=findViewById(R.id.name); main=findViewById(R.id.main); res_tv=findViewById(R.id.res_tv);
        btn_find=findViewById(R.id.btn_find); btn_end=findViewById(R.id.btn_end); btn_clear=findViewById(R.id.btn_clear);

        language= ((MainActivity)MainActivity.mContext).textCode;
        if(language==0){
            main.setText("비밀번호 찾기");
            btn_end.setText("확인");
            btn_find.setText("찾기");
            btn_clear.setText("초기화");
            name.setText("이름");
            mail.setText("메일");
        }
        else if(language==1){
            main.setText("サーチパスワード");
            btn_end.setText("チェック");
            btn_find.setText("サーチ");
            btn_clear.setText("リセツト");
            name.setText("ネーム");
            mail.setText("メール");
        }
        else if(language==2){
            main.setText("Check Password");
            btn_end.setText("Check");
            btn_find.setText("Find");
            btn_clear.setText("Clear");
            name.setText("Name");
            mail.setText("E-Mail");
        }

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    strName=SearchName(find_mail.getText().toString());
                    reference=firebaseDatabase.getReference("member_list").child(strName);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            strArray.clear();
                            for(DataSnapshot findSnapshot : snapshot.getChildren()){
                                strArray.add(findSnapshot.getValue().toString());
                                //Toast.makeText(findpwd.this,findSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                            }
                            String full_mail=strArray.get(1)+"@company.com";
                            try {
                                if(!strArray.get(2).equals(find_name.getText().toString()) || !full_mail.equals(find_mail.getText().toString())){
                                    //Toast.makeText(findpwd.this,"입력한 정보가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                                    anw="(ERROR)\nincorrect information.\nOr exist blank";
                                    res_tv.setText(anw);
                                }
                                else{
                                    anw="Thank you!\n"+strArray.get(2)+"'s password is "+strArray.get(3);
                                    SpannableStringBuilder ssb = new SpannableStringBuilder(anw);
                                    ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), 28, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    res_tv.setText(ssb);
                                }
                            }catch (Exception e){
                                anw="(ERROR)\nData does not exist.";
                                res_tv.setText(anw);
                                //Toast.makeText(findpwd.this,"데이터가 존재하지 않습니다.",Toast.LENGTH_SHORT).show();
                            }

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_name.setText("");
                find_mail.setText("");
                res_tv.setText("");
            }
        });
    }

    public String SearchName(String s){
        String sName="";
        while(true){
            String[] str = s.split("");
            for(int i=0;i<str.length;i++){
                if(str[i].equals("@")) break;
                else sName+=str[i];
            }
            break;
        }
        return sName;
    }
}