package com.example.communityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDialog{

    private Context context;
    private Button btn_x;
    private TextView info_main,info_title,check_info;
    private CheckBox checkBox;

    private SharedPreferences SPreferences;
    private String strSDFormatDay = "0";
    private final String NameSPreferences = "Day";


    public CustomDialog(Context context) {
        this.context=context;
    }

    public void callDialog(){
        final Dialog dialog = new Dialog(context);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.info_pop);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        btn_x=dialog.findViewById(R.id.btn_x);
        info_main=dialog.findViewById(R.id.info_main); info_title=dialog.findViewById(R.id.info_title);
        check_info=dialog.findViewById(R.id.check_info); checkBox=dialog.findViewById(R.id.checkBox);

        long CurrentTime = System.currentTimeMillis();
        Date today = new Date(CurrentTime);
        SimpleDateFormat format = new SimpleDateFormat("dd");
        strSDFormatDay = format.format(today); // 'dd' 형태로 포맷 변경

        SPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String strSPreferencesDay = SPreferences.getString(NameSPreferences, "0");
        if((Integer.parseInt(strSDFormatDay) - Integer.parseInt(strSPreferencesDay)) != 0) dialog.show();

        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked()){
                    SharedPreferences.Editor SPreferencesEditor = SPreferences.edit();
                    SPreferencesEditor.putString(NameSPreferences, strSDFormatDay); // 오늘 '일(day)' 저장
                    SPreferencesEditor.commit();
                    dialog.dismiss(); // dialog 닫기
                }
                dialog.dismiss();
            }
        });
    }
}