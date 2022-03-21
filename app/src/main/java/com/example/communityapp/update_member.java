package com.example.communityapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class update_member extends Dialog implements View.OnClickListener {

    private Context uContext;
    private EditText re_name,re_grade;
    private TextView re_yes,re_no,re_number;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference;
    private FirebaseAuth uAuth;
    private FirebaseUser firebaseUser;

    private ArrayList<String> re = new ArrayList<>();

    String mail,number;

    public update_member(@NonNull Context context) {
        super(context);
        uContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_member);

        uContext=getContext();
        uAuth=FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        re_grade=findViewById(R.id.re_grade); re_name=findViewById(R.id.re_name); re_number=findViewById(R.id.re_number);
        re_yes=findViewById(R.id.re_yes); re_no=findViewById(R.id.re_no);

        mail=((postpage)postpage.pContext).check;
        //Toast.makeText(uContext,firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
        databaseReference=FirebaseDatabase.getInstance().getReference("member_list").child(mail);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                re.clear();
                for(DataSnapshot reSnapshot : snapshot.getChildren()){
                    re.add(reSnapshot.getValue().toString());
                    //Toast.makeText(uContext,reSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                }
                re_name.setText(re.get(2)); re_grade.setText(re.get(0)); re_number.setText(re.get(3));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        re_yes.setOnClickListener(this); re_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.re_yes:
                //Toast.makeText(uContext,re_name.getText().toString()+" / "+re_grade.getText().toString()+" / "+re_number.getText().toString(),Toast.LENGTH_SHORT).show();
                databaseReference=FirebaseDatabase.getInstance().getReference("member_list");
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> postMap = null;
                Member member = new Member(re_name.getText().toString(),re_number.getText().toString(),re_grade.getText().toString(),mail);
                postMap = member.toMap();
                map.put(mail,postMap);
                databaseReference.updateChildren(map);
                //firebaseUser.updatePassword(re_number.getText().toString());
                Toast.makeText(uContext,"변경 완료되었습니다.",Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.re_no:
                dismiss();
                break;
        }
    }
}
