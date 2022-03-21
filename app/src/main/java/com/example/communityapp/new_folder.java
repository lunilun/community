package com.example.communityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class new_folder extends Dialog implements View.OnClickListener {

    private Context mContext;
    private EditText et_name;
    private TextView btn_yes,btn_no;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private FirebaseAuth fAuth;
    String name="";

    public new_folder(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_form);

        et_name=findViewById(R.id.et_name);
        btn_yes=findViewById(R.id.btn_yes);
        btn_no=findViewById(R.id.btn_no);
        mContext=getContext();
        fAuth=FirebaseAuth.getInstance();
        while(true){
            String[] arr = fAuth.getCurrentUser().getEmail().split("");
            for(int i=0;i<arr.length;i++){
                if(arr[i].equals("@")) break;
                else name+=arr[i];
            }
            break;
        }
        btn_yes.setOnClickListener(this); btn_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_yes:
                //fDatabase= FirebaseDatabase.getInstance().getReference().child("folder").child(name).child(et_name.getText().toString());
                //databaseReference.child("folder").child(name).push().setValue("test");
                databaseReference.child("folder").child(name).child(et_name.getText().toString()).child("empty").setValue("empty");
                dismiss();
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }
}
