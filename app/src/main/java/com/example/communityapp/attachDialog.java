package com.example.communityapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class attachDialog {

    private Context acontext;
    private ImageView img_attach;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    String img,name,where;

    public attachDialog(Context context) {
        this.acontext=context;
    }

    public void Dialog(){
        final Dialog dialog = new Dialog(acontext);

        img_attach=dialog.findViewById(R.id.img_attach);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.attach_pop);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        name= ((ReComment)ReComment.context).cel1;
        where =((ReComment)ReComment.context).file;

        img = name+".png";
        storageRef=storage.getReference()
                .child("Question")
                .child(name)
                .child(where);
        storageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        img_attach.setBackgroundResource(0);
                        img_attach.setImageBitmap(bitmap);
                    }
                });
    }
}