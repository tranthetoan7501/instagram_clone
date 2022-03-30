package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.commentsModel;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentsAdapter extends RecyclerView.Adapter<commentsAdapter.commentsViewHolder>{
    Context context;
    List<commentsModel> comments;
    String user;
    String post_id;

    public commentsAdapter(Context context, List<commentsModel> comments, String user, String post_id) {
        this.context = context;
        this.comments = comments;
        this.user = user;
        this.post_id = post_id;
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        if(comments.get(position).getLikes() == null){
            holder.num_likes.setVisibility(View.GONE);
        }
        else {
            if(comments.get(position).getLikes().size() == 1){
                holder.num_likes.setVisibility(View.VISIBLE);
                holder.num_likes.setText(comments.get(position).getLikes().size() + " like");
            }
            else {
                holder.num_likes.setVisibility(View.VISIBLE);
                holder.num_likes.setText(comments.get(position).getLikes().size() + " likes");
            }

            if(comments.get(position).getLikes().contains(mAuth.getUid())){
                holder.liked.setVisibility(View.VISIBLE);
                holder.like.setVisibility(View.GONE);
            }
            else{
                holder.liked.setVisibility(View.GONE);
                holder.like.setVisibility(View.VISIBLE);
            }
        }
        holder.date.setText(comments.get(position).getDate_created());
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
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

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pushLike = database.getReference("user_photos/"+user+"/"+post_id+"/comments/"+comments.get(holder.getAbsoluteAdapterPosition()).getComment_id());
                if(pushLike != null){
                    if(comments.get(holder.getAbsoluteAdapterPosition()).getLikes() == null){
                        pushLike.child("likes/0").setValue(mAuth.getUid());
                    }
                    else{
                        pushLike.child("likes/"+comments.get(holder.getAbsoluteAdapterPosition()).getLikes().size()).setValue(mAuth.getUid());
                    }
                }
                else {
                    pushLike.child("likes/0").setValue(mAuth.getUid());
                }
                holder.like.setVisibility(View.GONE);
                holder.liked.setVisibility(View.VISIBLE);
            }
        });
        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference pushLike = database.getReference("user_photos/"+user+"/"+post_id+"/comments/"+comments.get(holder.getAbsoluteAdapterPosition()).getComment_id()+"/likes");
                pushLike.child(comments.get(holder.getAbsoluteAdapterPosition()).getLikes().indexOf(mAuth.getUid())+"").removeValue();
                holder.like.setVisibility(View.VISIBLE);
                holder.liked.setVisibility(View.GONE);
            }
        });

        holder.full_comment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DatabaseReference deleteComment = database.getReference("user_photos/"+user+"/"+post_id+"/comments");
                                deleteComment.child(comments.get(holder.getAbsoluteAdapterPosition()).getComment_id()).removeValue();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete this comment?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return false;
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
        ImageButton liked;
        RelativeLayout full_comment;
        public commentsViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            num_likes = itemView.findViewById(R.id.num_likes);
            like = itemView.findViewById(R.id.like);
            liked = itemView.findViewById(R.id.liked);
            full_comment = itemView.findViewById(R.id.full_comment);

        }
    }
}
