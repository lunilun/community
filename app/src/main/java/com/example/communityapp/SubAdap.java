package com.example.communityapp;

import android.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SubAdap extends RecyclerView.Adapter<SubAdap.CustomViewHolder> {

    private ArrayList<Question> array;
    Context mContext;
    private DatabaseReference sDatabase;
    ArrayList<Integer> list = new ArrayList<>();

    public SubAdap(ArrayList<Question> arrayList,Context context) {
        this.array = arrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list,parent,false);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);

        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.ques_title.setText(array.get(position).getTitle());

        sDatabase = FirebaseDatabase.getInstance().getReference("Comment").child(((MyQuestion)MyQuestion.context).key);
        sDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot subSnapshot : snapshot.getChildren()){
                    if(subSnapshot.getKey().contains(array.get(position).getTitle())) list.add(position);
                }
                for(int i=0;i<list.size();i++) {
                    holder.ques_3.setTextColor(Color.BLUE);
                    holder.ques_1.setTextColor(Color.WHITE);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,QuesDetail.class);
                intent.putExtra("title",holder.ques_title.getText().toString());
                ((MyQuestion)mContext).startActivity(intent);
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
        protected TextView ques_title;
        protected TextView ques_1;
        protected TextView ques_3;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            ques_title=itemView.findViewById(R.id.ques_title);
            ques_1=itemView.findViewById(R.id.ques_1);
            ques_3=itemView.findViewById(R.id.ques_3);

            ques_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sDatabase = FirebaseDatabase.getInstance().getReference("Comment").child(((MyQuestion)MyQuestion.context).key);
                    sDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for (DataSnapshot subSnapshot : snapshot.getChildren()){
                                if(subSnapshot.getKey().equals(ques_title.getText().toString())){
                                    Toast.makeText(mContext,subSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        }
    }
}
