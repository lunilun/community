package com.example.communityapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AdminAdap extends RecyclerView.Adapter<AdminAdap.CustomViewHolder> {

    private ArrayList<Question> array;
    Context aContext;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;

    public AdminAdap(ArrayList<Question> arrayList,Context context) {
        this.array = arrayList;
        this.aContext = context;
    }

    @NonNull
    @Override
    public AdminAdap.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_list,parent,false);
        AdminAdap.CustomViewHolder customViewHolder = new AdminAdap.CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdminAdap.CustomViewHolder holder, final int position) {
        holder.all_title.setText(array.get(position).getTitle());

        mDatabase= FirebaseDatabase.getInstance().getReference("Comment").child(((AdminQuestion)AdminQuestion.aContext).sel1);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot AdminSnapshot : snapshot.getChildren()){
                    if(AdminSnapshot.getKey().contains(array.get(position).getTitle())) holder.all_title.setTextColor(Color.BLUE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(aContext,ReComment.class);
                intent.putExtra("category",((AdminQuestion)AdminQuestion.aContext).category);
                intent.putExtra("cel1",((AdminQuestion)AdminQuestion.aContext).sel1);
                if(((AdminQuestion)AdminQuestion.aContext).category.equals("folder")){
                    intent.putExtra("cel2",((AdminQuestion)AdminQuestion.aContext).sel2);
                }
                intent.putExtra("fileName",array.get(position).getTitle());
                ((AdminQuestion)aContext).startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != array ? array.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView all_title;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            all_title=itemView.findViewById(R.id.all_title);

        }
    }
}
