package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;

import java.util.ArrayList;
import java.util.List;

import com.hcmus.mobilappsocialnetworkingimage.model.UserInfor;
import com.hcmus.mobilappsocialnetworkingimage.adapter.*;

public class searchFragment extends Fragment {
    thumbnailsAdapter thumbnailsAdapter;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    private static final String TAG = "searchFragment";
    List<UserInfor> list = new ArrayList<UserInfor>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        if (container != null) {
//            container.removeAllViews();
//        }
        recyclerView = view.findViewById(R.id.grid);
        getData();


        return view;
    }
    public void setFilter(String querry){
        userAdapter.getFilter().filter(querry);
    }

//    public void getListUserFromDB(Query query){
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    UserInfor user = dataSnapshot.getValue(UserInfor.class);
//                    list.add(user);
//                }
//                userAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    void getData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");

        Query queryAllAccount = database.getReference("account").orderByChild("username");

        queryAllAccount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserInfor user = new UserInfor(dataSnapshot.getKey(),
                                                    dataSnapshot.child("email").getValue().toString(),
                                                    dataSnapshot.child("about").getValue().toString(),
                                                    dataSnapshot.child("avatar").getValue().toString());
                   
                    list.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userAdapter = new UserAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);




//        List<String> image = new ArrayList<>();
//        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
//        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
//        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
//        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
//        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
//        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
//        thumbnailsAdapter = new thumbnailsAdapter(image,getContext(),1);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        recyclerView.setAdapter(thumbnailsAdapter);
//        thumbnailsAdapter.notifyDataSetChanged();

    }
}