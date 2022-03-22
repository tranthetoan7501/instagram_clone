package com.hcmus.mobilappsocialnetworkingimage.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.loginActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.navigationActivity;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;
import com.hcmus.mobilappsocialnetworkingimage.activity.editProfileActivity;
import com.hcmus.mobilappsocialnetworkingimage.model.postsModel;

import java.util.ArrayList;
import java.util.List;

import com.hcmus.mobilappsocialnetworkingimage.adapter.*;

public class accountFragment extends Fragment implements View.OnClickListener {
    ImageView avatar;
    Button edit_pf;
    TextView username,about;
    RecyclerView recyclerView;
    thumbnailsAdapter thumbnailsAdapter;
    ImageButton all_pictures;
    ImageButton video;
    ImageButton tag;
    FirebaseAuth mAuth;
    userModel userInfor;
    TextView follower_numbers;
    TextView following_numbers;
    TextView post_numbers;
    String _username;
    ImageButton upItemBtn1;
    ImageButton settingBtn;
    private LinearLayout layoutSettingBottomSheet;
    private BottomSheetBehavior settingBottomSheetBehavior;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    LinearLayout logout;
    LinearLayout change_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        avatar = view.findViewById(R.id.avatar);
        username = view.findViewById(R.id.username);
        about = view.findViewById(R.id.description);
        edit_pf =view.findViewById(R.id.edit_pf);
        follower_numbers = view.findViewById(R.id.follower_numbers);
        following_numbers = view.findViewById(R.id.following_numbers);
        post_numbers = view.findViewById(R.id.post_numbers);
        upItemBtn1 = view.findViewById(R.id.add_button_account);
        settingBtn = view.findViewById(R.id.setting_button_account);
        upItemBtn1.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        edit_pf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), editProfileActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("_username",_username);
                intent.putExtras(bundle);
                //intent.putExtra("userInfor", );
                startActivity(intent);
            }
        });

        if (container != null) {
            container.removeAllViews();
        }
        layoutBottomSheet= getActivity().findViewById(R.id.bottomSheetContainer);
        layoutSettingBottomSheet = getActivity().findViewById(R.id.settingBottomSheetContainer);

        logout = getActivity().findViewById(R.id.logout);
        change_password = getActivity().findViewById(R.id.change_password);
        all_pictures = view.findViewById(R.id.all_pictures);
        video = view.findViewById(R.id.video);
        tag = view.findViewById(R.id.tag);
        recyclerView = view.findViewById(R.id.grid);
        video.setOnClickListener(this);
        all_pictures.setOnClickListener(this);

        logout.setOnClickListener(this);
        change_password.setOnClickListener(this);
        getData(1);
        setBottomSheet();
        return view;
    }
//    public void setUserInfo(UserInfor user,FirebaseAuth mAuth){
//            this.mAuth=mAuth;
//            this.userInfor=user;
//            username.setText(userInfor.getUsername());
//            about.setText(userInfor.getAbout());
//            Picasso.get().load(userInfor.getAvatar()).into(avatar);
//
//
//        if(user==null){
//            return;
//        }
////        String id = user.getUid();
////        Uri photoUrl = user.getPhotoUrl();
////
////        DocumentReference doc = document.collection("account").document(id);
////        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
////                if (task.isSuccessful()) {
////                    DocumentSnapshot document = task.getResult();
////                    if (document.exists()) {
////                        username.setText(document.getString("username"));
////                        about.setText(document.getString("about"));
////                    } else {
////                        Log.d(TAG, "No such document");
////                    }
////                } else {
////                    Log.d(TAG, "get failed with ", task.getException());
////                }
////            }
////        });
////
////
////        Glide.with(this).load(photoUrl).error(R.drawable.default_avatar);
//
//    }

    void setBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        settingBottomSheetBehavior = BottomSheetBehavior.from( layoutSettingBottomSheet);

        settingBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                getActivity().findViewById(R.id.container).setAlpha((float) 1.5 - slideOffset);
            }
        });
    }

    void getData(int i){
        List<postsModel> posts = new ArrayList<>();
        thumbnailsAdapter = new thumbnailsAdapter(posts,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(thumbnailsAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("account");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("id").getValue().equals(mAuth.getUid())){
                        userInfor=new userModel(snapshot.getKey()
                                ,snapshot.child("email").getValue().toString()
                                ,snapshot.child("about").getValue().toString()
                                ,snapshot.child("avatar").getValue().toString());

                       // Picasso.get().load(userInfor.getAvatar()).into(avatar);
                        username.setText(userInfor.getUsername());
                      //  about.setText(userInfor.getAbout());
                        List<String> follower = (List<String>) dataSnapshot.child("follower").getValue();

                        if(follower != null){
                            follower_numbers.setText("" + follower.size());
                        }
                        else{
                            follower_numbers.setText("0");
                        }

                        List<String> following = (List<String>) dataSnapshot.child("following").getValue();

                        if(following == null){
                            following_numbers.setText("0");
                        }
                        else {
                            following_numbers.setText("" + following.size());
                        }
                        break;
                    }
                }

                //In appbar 2
                TextView textViewInAppbar2=getActivity().findViewById(R.id.username);
                textViewInAppbar2.setText(username.getText());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.all_pictures:
                getData(1);
                break;
            case R.id.video:
                getData(2);
                break;
            case R.id.add_button_account:
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    settingBtn.setEnabled(false);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    settingBtn.setEnabled(true);
                }
                break;
            case R.id.setting_button_account:
                if (settingBottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    upItemBtn1.setEnabled(false);

                }else{
                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    upItemBtn1.setEnabled(true);
                }
                break;
            case R.id.change_password:
                Bundle bundle = new Bundle();
                bundle.putSerializable("type","change password");
//                bundle.putString("email", user.getEmail());
                Intent intent1 = new Intent(getContext(), navigationActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), loginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;

        }
    }
}