package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.storiesActivity;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;

public class storiesAdapter extends RecyclerView.Adapter<storiesAdapter.storiesViewHolder> {
    List<List<String>> listImage;
    Vector<String> listName;
    Vector<String> listAvt;
    Context context;
    List<String> currentUser;

    public storiesAdapter(List<String> currentUser,List<List<String>> listImage, Vector<String> listName, Vector<String> listAvt, Context context) {
        this.listName = listName;
        this.listImage = listImage;
        this.context = context;
        this.listAvt=listAvt;
        this.currentUser=currentUser;
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
        System.out.println(listImage.get(position).get(0));
        Picasso.get().load(listImage.get(position).get(0)).into(holder.circleImageView);
        holder.textView.setText(listName.get(position));

        holder.circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.circleImageView.setBorderColor(context.getColor(R.color.LightGrey));
                Intent intent=new Intent(context, storiesActivity.class);
                intent.putExtra("position",String.valueOf(position));
                intent.putExtra("Image", (Serializable) listImage);
                intent.putExtra("currentUser",currentUser.get(0));
                intent.putStringArrayListExtra("Name",new ArrayList<String>(listName));
                //intent.putStringArrayListExtra("Image",new ArrayList<String>(listImage));
                intent.putStringArrayListExtra("Avatar",new ArrayList<String>(listAvt));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listName.size();
    }


    static class storiesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView textView;

        public storiesViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.avatar);
            textView = itemView.findViewById(R.id.name);
        }
    }

}
