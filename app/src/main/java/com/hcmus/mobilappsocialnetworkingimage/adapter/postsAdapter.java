package com.hcmus.mobilappsocialnetworkingimage.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.navigationActivity;
import com.hcmus.mobilappsocialnetworkingimage.model.postsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class postsAdapter extends RecyclerView.Adapter<postsAdapter.postsViewHolder> {
    List<postsModel> post;
    Context context;
    LinearLayout layoutSettingBottomSheet;

    public postsAdapter(List<postsModel> post, Context context) {
        this.post = post;
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
        if (post.isEmpty()) return;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        holder.date.setText(post.get(position).getDate_created());
        holder.description.setText(post.get(position).getCaption());
        if(post.get(position).getLikes() != null ){
            if(post.get(position).getLikes().contains(mAuth.getUid())){
                holder.like.setVisibility(View.INVISIBLE);
                holder.liked.setVisibility(View.VISIBLE);
            }
            holder.num_likes.setVisibility(View.VISIBLE);
            if(post.get(position).getLikes().size() > 1 ){
                holder.num_likes.setText(Html.fromHtml("<b>" +post.get(position).getLikes().size() + " likes</b>" ));
            }
            else{
                holder.num_likes.setText(Html.fromHtml("<b>" +post.get(position).getLikes().size() + " like</b>" ));
            }
        }
        else{
            holder.num_likes.setVisibility(View.GONE);
        }

        ArrayList<SlideModel> image_paths = new ArrayList<>();
        for(String s : post.get(position).getImage_paths()){
            image_paths.add(new SlideModel(s));
        }
        holder.image.setImageList(image_paths,false);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myPosts = database.getReference("user_account_settings/"+post.get(position).getUser_id());
        myPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("profile_photo").getValue().toString()).into(holder.avatar);
                holder.up_name.setText(dataSnapshot.child("username").getValue().toString());
                holder.below_name.setText(dataSnapshot.child("username").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myLike = database.getReference("user_photos/"+post.get(holder.getAdapterPosition()).getUser_id()+"/"+post.get(holder.getAdapterPosition()).getPost_id()+"/"+"likes");
                if(myLike != null){
                    if(post.get(holder.getAbsoluteAdapterPosition()).getLikes() !=null)
                        myLike.child(post.get(holder.getAbsoluteAdapterPosition()).getLikes().size()+"").setValue(mAuth.getUid());
                    else{
                        myLike.child("0").setValue(mAuth.getUid());
                    }
                }
                else{
                    myLike.child("likes/0").setValue(mAuth.getUid());
                }
                holder.like.setVisibility(View.INVISIBLE);
                holder.liked.setVisibility(View.VISIBLE);
            }
        });

        holder.liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myLike1 = database.getReference("user_photos/"+post.get(holder.getAbsoluteAdapterPosition()).getUser_id()+"/"+post.get(holder.getAbsoluteAdapterPosition()).getPost_id()+"/"+"likes");
                myLike1.child(post.get(holder.getAbsoluteAdapterPosition()).getLikes().indexOf(mAuth.getUid())+"").removeValue();
                holder.like.setVisibility(View.VISIBLE);
                holder.liked.setVisibility(View.INVISIBLE);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("type","comment");
                bundle.putSerializable("post_id",post.get(holder.getAbsoluteAdapterPosition()).getPost_id());
                bundle.putSerializable("user_id",post.get(holder.getAbsoluteAdapterPosition()).getUser_id());
                Intent intent = new Intent(context, navigationActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getUid().equals(post.get(holder.getAbsoluteAdapterPosition()).getUser_id())){
                    BottomSheetBehavior settingBottomSheetBehavior = BottomSheetBehavior.from(layoutSettingBottomSheet);
                    if (settingBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED || settingBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING){
                        settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }else{
                        settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return post.size();
    }

    class postsViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView up_name;
        Button setting;
        ImageSlider image;
        ImageButton like;
        ImageButton liked;
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
            liked = itemView.findViewById(R.id.liked);
            comment = itemView.findViewById(R.id.comment);
            share = itemView.findViewById(R.id.share);
            num_likes = itemView.findViewById(R.id.num_likes);
            below_name = itemView.findViewById(R.id.below_name);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
        }
    }
}
