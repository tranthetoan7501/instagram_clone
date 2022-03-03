package com.hcmus.mobilappsocialnetworkingimage;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class activitiesAdapter extends RecyclerView.Adapter<activitiesAdapter.activitiesViewHolder> {
    List<String> image;
    List<String> do_something;
    List<String> preview;
    Context context;

    public activitiesAdapter(List<String> image, List<String> do_something, List<String> preview, Context context) {
        this.image = image;
        this.do_something = do_something;
        this.preview = preview;
        this.context = context;
    }

    @NonNull
    @Override
    public activitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity,parent,false);
        return new activitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull activitiesViewHolder holder, int position) {
        if(image.isEmpty()) return;
        String[] str = do_something.get(position).split(" ");
        Picasso.get().load(image.get(position)).into(holder.avatar);
        holder.description.setText(Html.fromHtml("<b>"+str[0]+"</b>" + do_something.get(position).replace(str[0],"")));
        Picasso.get().load(preview.get(position)).into(holder.preview);
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    class activitiesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView description;
        ImageView preview;
        public activitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            description = itemView.findViewById(R.id.who);
            preview = itemView.findViewById(R.id.preview);
        }
    }
}
