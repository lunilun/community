package com.example.communityapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

public class MainAdap extends RecyclerView.Adapter<MainAdap.CustomViewHolder>  {

    private ArrayList<Member> arrayList;
    Context mContext;
    private DatabaseReference mDatabase;
    private update_member update_member;
    public static String mail;

    private FirebaseUser user;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    String file_name;

    public MainAdap(ArrayList<Member> arrayList,Context context) {
        this.arrayList = arrayList;
        this.mContext=context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        //user = FirebaseAuth.getInstance().getCurrentUser();
        file_name = arrayList.get(position).getMail()+"_profile.png";
        storageRef=storage.getReference()
                .child("profile")
                .child(file_name);
        storageRef.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        holder.iv_pro.setImageBitmap(bitmap);
                    }
                });
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_grade.setText(arrayList.get(position).getGrade());
        holder.tv_number.setText(arrayList.get(position).getNumber());
        holder.tv_mail.setText(arrayList.get(position).getMail()+"@company.com");

        final String myKey = arrayList.get(position).getMail();
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mail=arrayList.get(position).getMail();
                update_member = new update_member(mContext);
                update_member.setCanceledOnTouchOutside(false);
                update_member.setCancelable(false);
                update_member.show();*/
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("member_list");
                mDatabase.child(myKey).removeValue();
                remove(holder.getAdapterPosition());
                //Toast.makeText(view.getContext(),myKey,Toast.LENGTH_SHORT).show();
                try {
                    //user.delete();
                }catch (Exception e){
                    Toast.makeText(view.getContext(),"등록 에러 데이터 입니다.",Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    public void remove(int position){
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView iv_pro;
        protected TextView tv_name;
        protected TextView tv_grade;
        protected TextView tv_number;
        protected TextView tv_mail;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_pro = itemView.findViewById(R.id.iv_pro);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_grade= itemView.findViewById(R.id.tv_grade);
            this.tv_number=itemView.findViewById(R.id.tv_number);
            this.tv_mail=itemView.findViewById(R.id.tv_mail);
        }
    }
}
