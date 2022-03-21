package com.example.communityapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class finish extends Dialog implements View.OnClickListener {

    private Context fContext;
    private TextView fin_tv;
    private Button fin_yes,fin_no;

    public finish(@NonNull Context context){
        super(context);
        fContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finish_layout);

        fin_tv=findViewById(R.id.fin_tv);
        fin_yes=findViewById(R.id.fin_yes); fin_no=findViewById(R.id.fin_no);

        fin_yes.setOnClickListener(this); fin_no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fin_yes:
                dismiss();
                ((MainActivity)MainActivity.mContext).finish();
                break;
            case R.id.fin_no:
                dismiss();
                break;
        }

    }
}
