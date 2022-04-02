package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.storiesActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class storiesAdapter extends RecyclerView.Adapter<storiesAdapter.storiesViewHolder> {
    Vector<String> listImage;
    Vector<String> listName;
    Context context;

    public storiesAdapter(Vector<String> listImage, Vector<String> listName, Context context) {
        this.listName = listName;
        this.listImage = listImage;
        this.context = context;
    }

    @NonNull
    @Override
    public storiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story,parent,false);
        return new storiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull storiesViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(listName.isEmpty()) return;
        Picasso.get().load(listImage.get(position)).into(holder.circleImageView);
        FirebaseDatabase database= FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference databaseReference=database.getReference("users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null) {
                        listName.set(position, dataSnapshot.child(listName.get(position)).child("username").getValue().toString());
                        holder.textView.setText(listName.get(position));
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.circleImageView.setBorderColor(context.getColor(R.color.LightGrey));
                Intent intent=new Intent(context, storiesActivity.class);
                intent.putExtra("position",String.valueOf(position));
                intent.putStringArrayListExtra("Name",new ArrayList<String>(listName));
                intent.putStringArrayListExtra("Image",new ArrayList<String>(listImage));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.avatar:
//                Toast.makeText(view.getContext(),"abc",Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

    class storiesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;

        public storiesViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar);
            textView = itemView.findViewById(R.id.name);
        }
    }

}
