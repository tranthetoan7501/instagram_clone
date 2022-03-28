package com.hcmus.mobilappsocialnetworkingimage.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.editProfileActivity;
import com.hcmus.mobilappsocialnetworkingimage.adapter.thumbnailsAdapter;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class profileFragment extends Fragment implements View.OnClickListener{

    ImageButton previous;
    ImageView avatar;
    TextView username;
    Bundle bundle = new Bundle();
    RecyclerView recyclerView;
    thumbnailsAdapter thumbnailsAdapter;
    userAccountSettingsModel userAccountSettingsModel;
    TextView about;
    TextView follower_numbers;
    TextView following_numbers;
    TextView post_numbers;

    Button follow;
    String id;
    Integer authFollower = 0;
    Integer clientFollowing = 0;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        avatar = view.findViewById(R.id.avatar);
        username = view.findViewById(R.id.username);
        bundle = getArguments();
        Picasso.get().load(bundle.get("avatar").toString()).into(avatar);
        username.setText(bundle.get("username").toString());
        follow =view.findViewById(R.id.set_follow);
        follow.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        bundle = getArguments();
        id = bundle.get("id").toString();
        if(mAuth.getUid().equals(id)){
            follow.setText("Edit Profile");
        }

        about = view.findViewById(R.id.description);
        follower_numbers = view.findViewById(R.id.follower_numbers);
        following_numbers = view.findViewById(R.id.following_numbers);
        post_numbers = view.findViewById(R.id.post_numbers);
        recyclerView = view.findViewById(R.id.grid);

        getData(1);
        return view;
    }

    void getData(int i){
        DatabaseReference checkFollowing = database.getReference("following").child(mAuth.getUid());

        checkFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(dataSnapshot.getKey().toString().equals(id)){
                        follow.setText("Unfollow");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        List<thumbnailsModel> thumbails = new ArrayList<>();
        thumbnailsAdapter = new thumbnailsAdapter(thumbails,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(thumbnailsAdapter);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("user_account_settings").child(bundle.get("id").toString());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userAccountSettingsModel = new userAccountSettingsModel(dataSnapshot.child("description").getValue().toString()
                            , dataSnapshot.child("display_name").getValue().toString()
                            , Integer.parseInt(dataSnapshot.child("followers").getValue().toString())
                            , Integer.parseInt(dataSnapshot.child("following").getValue().toString())
                            , Integer.parseInt(dataSnapshot.child("posts").getValue().toString())
                            , dataSnapshot.child("profile_photo").getValue().toString()
                            , dataSnapshot.child("username").getValue().toString()
                            , dataSnapshot.child("website").getValue().toString());
                    about.setText(userAccountSettingsModel.getDescription());
                    post_numbers.setText(dataSnapshot.child("posts").getValue().toString());
                    follower_numbers.setText(String.valueOf(userAccountSettingsModel.getFollowers()));
                    following_numbers.setText(String.valueOf(userAccountSettingsModel.getFollowing()));
                    Picasso.get().load(userAccountSettingsModel.getProfile_photo()).into(avatar);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
            if(i==1){
                DatabaseReference myPosts = database.getReference("user_photos/"+bundle.getString("id"));
                myPosts.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        thumbails.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            thumbails.add(new thumbnailsModel((ArrayList<String>) snapshot.child("image_paths").getValue(),snapshot.child("user_id").getValue().toString(),snapshot.child("post_id").getValue().toString()));
                        }
                        post_numbers.setText(thumbails.size() + "");
                        thumbnailsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous:
                getActivity().finish();
                break;
            case R.id.set_follow:
                if(mAuth.getUid().equals(id)){
                    Intent intent=new Intent(getContext(), editProfileActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("userAccountSettings", (Serializable) userAccountSettingsModel);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{

                    if(follow.getText().toString().equals("Follow")){
                        String authId = mAuth.getUid();
                        DatabaseReference myRef = database.getReference("following").child(authId).child(id).child("user_id");
                        myRef.setValue(id);
                        DatabaseReference anotherRef = database.getReference("followers").child(id).child(authId).child("user_id");
                        anotherRef.setValue(authId);


                        DatabaseReference userSetting = database.getReference("user_account_settings");

                        userSetting.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer authFollower = Integer.parseInt(snapshot.child(authId).child("following").getValue().toString())+1;
                                Integer clientFollowing = Integer.parseInt(snapshot.child(id).child("followers").getValue().toString())+1;
                                userSetting.child(authId).child("following").setValue(authFollower);
                                userSetting.child(id).child("followers").setValue(clientFollowing);
                                follow.setText("Unfollow");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }else if(follow.getText().toString().equals("Unfollow")){
                        String authId = mAuth.getUid();
                        DatabaseReference myRef = database.getReference("following").child(authId+"/"+id);
                        myRef.removeValue();
                        DatabaseReference anotherRef = database.getReference("followers").child(id+"/"+authId);
                        anotherRef.removeValue();
                        DatabaseReference userSetting = database.getReference("user_account_settings");
                        userSetting.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer authFollower = Integer.parseInt(snapshot.child(authId).child("following").getValue().toString())-1;
                                Integer clientFollowing = Integer.parseInt(snapshot.child(id).child("followers").getValue().toString())-1;
                                userSetting.child(authId).child("following").setValue(authFollower);
                                userSetting.child(id).child("followers").setValue(clientFollowing);
                                follow.setText("Follow");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                break;
        }
    }
}
