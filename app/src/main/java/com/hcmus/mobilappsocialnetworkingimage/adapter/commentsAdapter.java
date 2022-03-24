package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.commentsModel;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentsAdapter extends RecyclerView.Adapter<commentsAdapter.commentsViewHolder>{
    Context context;
    List<commentsModel> comments;

    public commentsAdapter(Context context, List<commentsModel> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public commentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment,parent,false);
        return new commentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull commentsViewHolder holder, int position) {
        if (comments.isEmpty()) return;
//        Picasso.get().load(comments.get(position).getUser_id()).into(holder.avatar);
////        String str[] = description.get(position).split("\n");
////        holder.description.setText(Html.fromHtml("<b>" + str[0]+"</b>" + description.get(position).replace(str[0]," ")));


        if(comments.get(position).getLikes().size() == 0 ){
            holder.num_likes.setVisibility(View.GONE);
        }
        else if(comments.get(position).getLikes().size() == 1){
            holder.num_likes.setText(comments.get(position).getLikes().size() + " like");
        }
        else
        holder.num_likes.setText(comments.get(position).getLikes().size() + " likes");
        holder.date.setText(comments.get(position).getDate_created());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        System.out.println();
        DatabaseReference myRef = database.getReference("user_account_settings").child(comments.get(position).getUser_id());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Picasso.get().load(snapshot.child("profile_photo").getValue().toString()).into(holder.avatar);
                holder.description.setText(Html.fromHtml("<b>"+snapshot.child("username").getValue().toString() + "</b> " +comments.get(holder.getAbsoluteAdapterPosition()).getComment()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class commentsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView description;
        TextView date;
        TextView num_likes;
        ImageButton like;
        public commentsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            num_likes = itemView.findViewById(R.id.num_likes);
            like = itemView.findViewById(R.id.like);
        }
    }
}
