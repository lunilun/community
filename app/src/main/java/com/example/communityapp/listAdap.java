package com.example.communityapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listAdap extends RecyclerView.Adapter<listAdap.CustomViewHolder> {

    private ArrayList<listitem> array;
    Context LContext;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    String type;

    public listAdap(ArrayList<listitem> arrayList,Context context) {
        this.array = arrayList;
        this.LContext = context;
    }

    @NonNull
    @Override
    public listAdap.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lay,parent,false);
        listAdap.CustomViewHolder customViewHolder = new listAdap.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final listAdap.CustomViewHolder holder, final int position) {
        type=array.get(position).getTitle();

        holder.textTitle.setText(array.get(position).getTitle());
        holder.textUser.setText(array.get(position).getUser());
        holder.textDate.setText(array.get(position).getDay());
        holder.rev.setEnabled(false);
        holder.del.setEnabled(false);

        final String listKey = array.get(position).getUser();
        if(array.get(position).getColor().equals("red")) holder.textUser.setTextColor(Color.RED);
        else if(array.get(position).getColor().equals("yellow")) holder.textUser.setTextColor(Color.parseColor("#FF8C00"));
        else if(array.get(position).getColor().equals("green")) holder.textUser.setTextColor(Color.GREEN);

        mDatabase=FirebaseDatabase.getInstance().getReference("schedule").child(((user_date)user_date.dateContext).cur_date).child(((postpage)postpage.pContext).check);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    //Toast.makeText(LContext,snapshot1.getKey(),Toast.LENGTH_LONG).show();
                    try {
                        if(snapshot1.getKey().contains(array.get(position).getUser()) && ((postpage)postpage.pContext).check.equals(array.get(position).getTitle())) {
                            holder.rev.setEnabled(true);
                            holder.del.setEnabled(true);
                        }
                    }catch (Exception e){

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LContext,Detail_date.class);
                intent.putExtra("title",holder.textTitle.getText().toString());
                intent.putExtra("user",holder.textUser.getText().toString());
                intent.putExtra("color",array.get(position).getColor());
                intent.putExtra("date",holder.textDate.getText().toString());
                intent.putExtra("cont",array.get(position).getCont());
                ((user_date)LContext).startActivity(intent);
            }
        });
        holder.rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LContext,update_date.class);
                intent1.putExtra("title",holder.textUser.getText().toString());
                intent1.putExtra("date",holder.textDate.getText().toString());
                intent1.putExtra("cont",array.get(position).getCont());
                intent1.putExtra("user",holder.textTitle.getText().toString());
                ((user_date)LContext).startActivity(intent1);
                //Toast.makeText(LContext,"수정을 위한 버튼입니다.",Toast.LENGTH_LONG).show();
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child(listKey).removeValue();
                remove(holder.getAdapterPosition());
            }
        });
    }
    public void remove(int position){
        try {
            array.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != array ? array.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView textTitle;
        protected TextView textUser;
        protected TextView textDate;
        protected Button rev,del;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textUser=itemView.findViewById(R.id.textUser);
            textDate=itemView.findViewById(R.id.textDate);
            rev=itemView.findViewById(R.id.rev);
            del=itemView.findViewById(R.id.del);
        }
    }
}
