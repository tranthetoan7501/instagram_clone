package com.hcmus.mobilappsocialnetworkingimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.postsViewHolder> {
    List<String> name;
    List<String> image;
    List<String> description;
    List<String> date;
    Context context;

    public postsAdapter(List<String> name, List<String> image, List<String> description, List<String> date, Context context) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.date = date;
        this.context = context;
    }

    @NonNull
    @Override
    public postsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        return new postsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postsViewHolder holder, int position) {
        if(name.isEmpty()) return;
        holder.up_name.setText(name.get(position));
        List<SlideModel> models = new ArrayList<>();
        models.add(new SlideModel(image.get(position)));
        holder.image.setImageList(models,false);
        holder.below_name.setText(name.get(position));
        holder.description.setText(description.get(position));
        holder.date.setText(date.get(position));

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    class postsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView up_name;
        Button setting;
        ImageSlider image;
        ImageButton like;
        ImageButton comment;
        ImageButton share;
        TextView num_likes;
        TextView below_name;
        TextView description;
        TextView date;
        public postsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            up_name = itemView.findViewById(R.id.up_name);
            setting = itemView.findViewById(R.id.setting_button);
            image = itemView.findViewById(R.id.image_slider);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.like);
            share = itemView.findViewById(R.id.like);
            num_likes = itemView.findViewById(R.id.num_likes);
            below_name = itemView.findViewById(R.id.below_name);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
        }
    }
}
