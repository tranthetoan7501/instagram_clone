package com.hcmus.mobilappsocialnetworkingimage.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.shareActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.hcmus.mobilappsocialnetworkingimage.adapter.*;
import com.hcmus.mobilappsocialnetworkingimage.model.postsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.squareup.picasso.Picasso;

import adapter.storiesAdapter;


public class homeFragment extends Fragment {
    RecyclerView stories;
    adapter.storiesAdapter storiesAdapter;
    RecyclerView posts;
    postsAdapter postsAdapter;
    ImageButton upItemBtn;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView header_Arrow_Image;
    private LinearLayout post_layout;
    RecyclerView post_recyclerView;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        stories = view.findViewById(R.id.stories);
        posts = view.findViewById(R.id.posts);
        upItemBtn = view.findViewById(R.id.add_button);
        post_recyclerView = view.findViewById(R.id.posts);
        getDataStories();
        getDataPosts();
        setBottomSheetBehavior();
        return view;
    }

    void setBottomSheetBehavior(){
        layoutBottomSheet= getActivity().findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        header_Arrow_Image = getActivity().findViewById(R.id.header_Arrow_Image);
        post_layout = getActivity().findViewById(R.id.post_layout);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                getActivity().findViewById(R.id.container).setAlpha((float) 1.5 - slideOffset);
            }
        });

        upItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        header_Arrow_Image.setOnClickListener(v -> {
            if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        post_layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), shareActivity.class);
            startActivity(intent);
        });
    }

    void getDataStories(){
        List<String> name = new ArrayList<>();
        List<String> image = new ArrayList<>();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        storiesAdapter = new storiesAdapter(image,name,getContext());
        stories.setLayoutManager(linearLayout);
        stories.setAdapter(storiesAdapter);
        name.add("Sơn Thanh");
        name.add("Vinh Quang");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        name.add("Sơn Thanh");
        name.add("Vinh Quang");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        name.add("Sơn Thanh");
        name.add("Vinh Quang");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        storiesAdapter.notifyDataSetChanged();

    }


    void getDataPosts(){
        List<postsModel> post = new ArrayList<>();
        postsAdapter = new postsAdapter(post,getContext());
        post_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        post_recyclerView.setAdapter(postsAdapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myFollowing = database.getReference("following/"+mAuth.getUid());
                myFollowing.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> key = new ArrayList<>();
                        key.add(mAuth.getUid());
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            key.add(snapshot.getKey());
                        }

                            DatabaseReference myPosts = database.getReference("user_photos");
                           myPosts.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   post.clear();
                                   for(DataSnapshot data : snapshot.getChildren()){
                                       if(key.contains(data.getKey())){
                                           for(DataSnapshot p : data.getChildren()){
                                               post.add(p.getValue(postsModel.class));
                                           }
                                       }
                                   }
                                   postsAdapter.notifyDataSetChanged();
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {

                               }
                           });
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}