package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.hcmus.mobilappsocialnetworkingimage.MainActivity;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.secondActivity;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import com.hcmus.mobilappsocialnetworkingimage.fragment.postFragment;

public class thumbnailsAdapter extends RecyclerView.Adapter<thumbnailsAdapter.thumbnailsViewHolder> {
    List<String> image;
    Context context;
    int type;

    public thumbnailsAdapter(List<String> image, Context context, int type) {
        this.image = image;
        this.context = context;
        this.type = type;
    }

    public thumbnailsAdapter(MainActivity context) {
    }

    @NonNull
    @Override
    public thumbnailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail,parent,false);
        return new thumbnailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull thumbnailsViewHolder holder, int position) {
        if (image.isEmpty()) return;
        Picasso.get().load(image.get(position)).into(holder.thumbnail);
        if(type == 1){
            holder.icon.setImageResource(R.drawable.ic_photo_lib);
        }
        else if(type==2){
            holder.thumbnail.setVisibility(View.GONE);
            holder.video.setVisibility(View.VISIBLE);
            holder.video.setVideoPath(image.get(position));
            holder.video.requestFocus();
            holder.video.pause();
            holder.icon.setImageResource(R.drawable.ic_video_1);
        }

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof MainActivity){
                    ((MainActivity)context).turnOnFragment("postFragment",null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    class thumbnailsViewHolder extends RecyclerView.ViewHolder{
        ImageView thumbnail;
        ImageView icon;
        VideoView video;
        public thumbnailsViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            icon = itemView.findViewById(R.id.icon);
            video =itemView.findViewById(R.id.video);
        }
    }
}
