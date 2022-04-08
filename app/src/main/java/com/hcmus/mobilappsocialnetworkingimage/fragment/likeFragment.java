package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.userAdapter;
import com.hcmus.mobilappsocialnetworkingimage.model.likeModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userCardModel;

import java.util.ArrayList;
import java.util.List;

public class likeFragment extends Fragment {
    RecyclerView recyclerView;
    userAdapter userAdapter;
    List<userCardModel> list = new ArrayList<userCardModel>();
    ImageButton previous;
    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app");
    DatabaseReference myRef = mFirebaseDatabase.getReference();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_like, container, false);
//        if (container != null) {
//            container.removeAllViews();
//        }
        recyclerView = view.findViewById(R.id.grid_card);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        getData();


        return view;
    }
    public void setFilter(String querry){
        userAdapter.getFilter().filter(querry);
    }

    void getData(){
        String id_post = getArguments().getString("post_id");
        String id_user = getArguments().getString("user_id");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");

        Query queryAllAccount = database.getReference("user_photos").child(id_user).child(id_post).child("likes");

        userAdapter = new userAdapter(list, getContext());

        queryAllAccount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    likeModel likeModel = dataSnapshot.getValue(com.hcmus.mobilappsocialnetworkingimage.model.likeModel.class);

                    Query query = database.getReference("user_account_settings");
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                if (dataSnapshot.getKey().equals(likeModel.getUser_id())) {
                                    userAccountSettingsModel userAccountSettings = dataSnapshot.getValue(userAccountSettingsModel.class);
                                    userCardModel userCardModel = new userCardModel(likeModel.getUser_id(), userAccountSettings.getUsername(), userAccountSettings.getProfile_photo());
                                    list.add(userCardModel);
                                    userAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        userAdapter = new userAdapter(list, getContext());
//

//
//        myRef.child("user_photos").child(FirebaseAuth.getInstance().getUid()).child(id_post).child("likes").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//               // list.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    likeModel likeModel = ds.getValue(likeModel.class);
//                    myRef.child("user_account_settings").child(likeModel.getUser_id()).get();
//
//                    myRef.child("user_account_settings").child(likeModel.getUser_id()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            userCardModel userCard = new userCardModel();
//                            userAccountSettingsModel userAccountSettingsModel = dataSnapshot.getValue(userAccountSettingsModel.class);
//                            userCard.setUsername(userAccountSettingsModel.getUsername());
//                            userCard.setAvatar(userAccountSettingsModel.getProfile_photo());
//                            userCard.setUser_id(likeModel.getUser_id());
//                            list.add(userCard);
//                            System.out.println(list.size());
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                //userAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        for (int i = 0; i < list.size(); i++)
//            System.out.println(list.get(i).getUsername());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.search_appbar_input && !hasFocus) {

                InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        }
    }
}