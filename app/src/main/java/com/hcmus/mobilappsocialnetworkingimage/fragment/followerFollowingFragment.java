package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.userAdapter;
import com.hcmus.mobilappsocialnetworkingimage.model.*;

import java.util.ArrayList;

public class followerFollowingFragment extends Fragment {
    ImageButton previous;
    ArrayList<userCardModel> userCardModelArrayList = new ArrayList<>();
    RecyclerView recycler_view;
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    userAdapter userAdapter;
    TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_follower_following, container, false);
        previous = view.findViewById(R.id.previous);
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        userAdapter = new userAdapter(userCardModelArrayList,getContext());
        recycler_view.setAdapter(userAdapter);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        title = view.findViewById(R.id.title);
        title.setText(this.getArguments().getString("key"));
        getData();
        return view;
    }

    void getData(){
        DatabaseReference myRef = mFirebaseDatabase.getReference().child(this.getArguments().getString("key")+"/"+mAuth.getUid());
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> key = new ArrayList<>();
                for(DataSnapshot data : task.getResult().getChildren()){
                    key.add(data.getKey());
                }
                DatabaseReference myInfor = mFirebaseDatabase.getReference().child("user_account_settings");
                myInfor.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task1) {
                        for(DataSnapshot t : task1.getResult().getChildren()){
                            if(key.contains(t.getKey())){
                                userCardModel userCardModel = new userCardModel(t.getKey(),t.child("username").getValue().toString(),t.child("profile_photo").getValue().toString());
                                userCardModelArrayList.add(userCardModel);
                            }
                        }
                        userAdapter.notifyDataSetChanged();

                    }
                });
            }
        });
    }
}