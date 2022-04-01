package com.hcmus.mobilappsocialnetworkingimage.fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.activitiesAdapter;
import com.hcmus.mobilappsocialnetworkingimage.adapter.thumbnailsAdapter;
import com.hcmus.mobilappsocialnetworkingimage.model.notificationsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class activityFragment extends Fragment {
    com.hcmus.mobilappsocialnetworkingimage.adapter.activitiesAdapter activitiesAdapter;
    RecyclerView recyclerView;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.activity);
        getData();
        return view;
    }

    void getData() {
        List<notificationsModel> notification = new ArrayList<>();
        activitiesAdapter = new activitiesAdapter(notification, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(activitiesAdapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");

        if (mAuth.getCurrentUser() != null) {
            DatabaseReference data = database.getReference().child("notification/"+mAuth.getUid());
            data.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    notification.clear();
                    for(DataSnapshot data : snapshot.getChildren()){
                        notification.add(data.getValue(notificationsModel.class));
                    }
                    activitiesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}