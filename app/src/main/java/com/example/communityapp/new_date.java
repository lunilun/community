package com.example.communityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class new_date extends AppCompatActivity implements View.OnClickListener {

    private EditText edit1,edit2;
    private Button btn1,btn2,btn3,btn4,btn5;
    private NumberPicker number1,number2,number3,number4;
    private TextView text3,text5,text6;

    int t;
    String color,user,cur;
    String time ="";
    int ye,mo,da;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    ArrayList<Integer> date = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_date);

        edit1=findViewById(R.id.edit1); edit2=findViewById(R.id.edit2);
        text3=findViewById(R.id.text3); text5=findViewById(R.id.text5); text6=findViewById(R.id.text6);
        btn1=findViewById(R.id.btn1); btn2=findViewById(R.id.btn2); btn3=findViewById(R.id.btn3); btn4=findViewById(R.id.btn4); btn5=findViewById(R.id.btn5);
        number1=findViewById(R.id.number1); number2=findViewById(R.id.number2); number3=findViewById(R.id.number3); number4=findViewById(R.id.number4);

        user=((postpage)postpage.pContext).check;
        t=((user_date)user_date.dateContext).code3;
        ye=((user_date)user_date.dateContext).y; mo=((user_date)user_date.dateContext).m; da=((user_date)user_date.dateContext).d;
        cur=String.valueOf(ye)+String.valueOf(mo)+String.valueOf(da);
        date.add(ye); date.add(mo); date.add(da);

        number1.setMaxValue(2); number1.setMinValue(0);
        number2.setMaxValue(9); number2.setMinValue(0);
        number3.setMaxValue(5); number3.setMinValue(0);
        number4.setMaxValue(9); number4.setMinValue(0);

        if(t==0){
            text5.setText("제목");
            text6.setText("내용");
            text3.setText("시작 시간");
        }
        else if(t==1){
            text5.setText("タイトル");
            text6.setText("コンテンツ");
            text3.setText("スタートタイム.");
        }
        else if(t==2){
            text5.setText("Title");
            text6.setText("Content");
            text3.setText("Start");
        }
        btn1.setOnClickListener(this); btn2.setOnClickListener(this); btn3.setOnClickListener(this);
        btn4.setOnClickListener(this); btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn1:
                btn1.setBackgroundResource(R.drawable.custom_green);
                btn2.setBackgroundResource(R.drawable.custom_nomal);
                btn3.setBackgroundResource(R.drawable.custom_nomal);
                color="green";
                break;
            case R.id.btn2:
                btn1.setBackgroundResource(R.drawable.custom_nomal);
                btn2.setBackgroundResource(R.drawable.custom_yellow);
                btn3.setBackgroundResource(R.drawable.custom_nomal);
                color="yellow";
                break;
            case R.id.btn3:
                btn1.setBackgroundResource(R.drawable.custom_nomal);
                btn2.setBackgroundResource(R.drawable.custom_nomal);
                btn3.setBackgroundResource(R.drawable.custom_red);
                color="red";
                break;
            case R.id.btn4:
                time+=String.valueOf(number1.getValue()) + String.valueOf(number2.getValue()) + ":" + String.valueOf(number3.getValue()) + String.valueOf(number4.getValue());
                try{
                    if((!edit1.getText().toString().equals(null)) && (!color.equals(null))){
                        reference=FirebaseDatabase.getInstance().getReference("schedule").child(cur).child(user);
                        Map<String, Object> childUpdates = new HashMap<>();
                        Map<String, Object> listValues = null;
                        listitem listitem = new listitem(user,edit1.getText().toString(),time,color,edit2.getText().toString());
                        listValues = listitem.LMap();
                        childUpdates.put(edit1.getText().toString(), listValues);
                        reference.updateChildren(childUpdates);
                        finish();
                    }
                }catch (Exception e){
                    Toast.makeText(new_date.this,"미입력 항목이 존재합니다.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn5:
                finish();
                break;
        }
    }
}