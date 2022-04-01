package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.activity.mainActivity;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.fragment.postFragment;
import com.hcmus.mobilappsocialnetworkingimage.model.notificationsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class activitiesAdapter extends RecyclerView.Adapter<activitiesAdapter.activitiesViewHolder> {
    List<notificationsModel> notification;
    Context context;

    public activitiesAdapter(List<notificationsModel> notification, Context context) {
        this.notification = notification;
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
        if(notification.isEmpty()) return;
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");

        DatabaseReference myInfor = database.getReference().child("user_account_settings/"+notification.get(holder.getAbsoluteAdapterPosition()).getUser_id());
        myInfor.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Picasso.get().load(task.getResult().child("profile_photo").getValue().toString()).into(holder.avatar);
                holder.des.setText(Html.fromHtml("<b>"+task.getResult().child("username").getValue() + "</b> "+notification.get(holder.getAbsoluteAdapterPosition()).getContent()));
            }
        });

        Picasso.get().load(notification.get(position).getImage_paths().get(0)).into(holder.preview);


        holder.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postFragment postFragment = new postFragment();
                Bundle bundle = new Bundle();
                bundle.putString("user_id",mAuth.getUid());
                bundle.putString("post_id",notification.get(holder.getAbsoluteAdapterPosition()).getPost_id());
                bundle.putStringArrayList("image_paths",notification.get(holder.getAbsoluteAdapterPosition()).getImage_paths());
                postFragment.setArguments(bundle);
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,postFragment).addToBackStack("postFragment").commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return notification.size();
    }

    class activitiesViewHolder extends RecyclerView.ViewHolder{
        CircleImageView avatar;
        TextView des;
        ImageView preview;
        RelativeLayout notification;
        public activitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            des = itemView.findViewById(R.id.content);
            preview = itemView.findViewById(R.id.preview);
            notification = itemView.findViewById(R.id.notification);
        }
    }
}
