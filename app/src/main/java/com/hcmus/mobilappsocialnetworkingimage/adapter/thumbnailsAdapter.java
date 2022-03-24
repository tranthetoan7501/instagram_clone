package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.hcmus.mobilappsocialnetworkingimage.activity.mainActivity;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class thumbnailsAdapter extends RecyclerView.Adapter<thumbnailsAdapter.thumbnailsViewHolder> {
    List<thumbnailsModel> thumbnails;
    Context context;

    public thumbnailsAdapter(List<thumbnailsModel> thumbnails, Context context) {
        this.thumbnails = thumbnails;
        this.context = context;
    }

    @NonNull
    @Override
    public thumbnailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail,parent,false);
        return new thumbnailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull thumbnailsViewHolder holder, int position) {
        if (thumbnails.isEmpty()) return;
        Picasso.get().load(thumbnails.get(position).getImage_paths().get(0)).into(holder.thumbnail);
        if(thumbnails.get(position).getImage_paths().size() > 1){
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_photo_lib);
        }

//        else if(type==2){
//            holder.thumbnail.setVisibility(View.GONE);
//            holder.video.setVisibility(View.VISIBLE);
//            holder.video.setVideoPath(image.get(position));
//            holder.video.requestFocus();
//            holder.video.pause();
//            holder.icon.setImageResource(R.drawable.ic_video_1);
//        }

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof mainActivity){
                    ((mainActivity)context).turnOnFragment("postFragment", thumbnails.get(holder.getPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return thumbnails.size();
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
