package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.editProfileActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.loginActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.navigationActivity;
import com.hcmus.mobilappsocialnetworkingimage.adapter.thumbnailsAdapter;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;
import com.hcmus.mobilappsocialnetworkingimage.model.userAccountSettingsModel;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class accountFragment extends Fragment implements View.OnClickListener {
    ImageView avatar;
    Button edit_pf;
    TextView usernameInAppbar,about;
    RecyclerView recyclerView;
    thumbnailsAdapter thumbnailsAdapter;
    ImageButton all_pictures;
    ImageButton video;
    ImageButton tag;
    FirebaseAuth mAuth;
    userAccountSettingsModel userAccountSettingsModel;
    TextView follower_numbers;
    TextView following_numbers;
    TextView post_numbers;
    ImageButton upItemBtn1;
    ImageButton settingBtn;
    LinearLayout logout;
    LinearLayout change_password;
    View appbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        avatar = view.findViewById(R.id.avatar);
        usernameInAppbar = view.findViewById(R.id.usernameInAppbar);
        about = view.findViewById(R.id.description);
        edit_pf =view.findViewById(R.id.edit_pf);
        follower_numbers = view.findViewById(R.id.follower_numbers);
        following_numbers = view.findViewById(R.id.following_numbers);
        post_numbers = view.findViewById(R.id.post_numbers);
        upItemBtn1 = view.findViewById(R.id.add_button_account);
        settingBtn = view.findViewById(R.id.setting_logout);
        upItemBtn1.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        edit_pf.setOnClickListener(view1 -> {

            Intent intent=new Intent(getContext(), editProfileActivity.class);
            Bundle bundle=new Bundle();
            //bundle.putString("_username",_username);
            bundle.putSerializable("userAccountSettings", (Serializable) userAccountSettingsModel);
            intent.putExtras(bundle);

            startActivity(intent);
        });

        if (container != null) {
            container.removeAllViews();
        }

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
    void setBottomSheet(){

    }

    void getData(int i){
        List<thumbnailsModel> thumbails = new ArrayList<>();
        thumbnailsAdapter = new thumbnailsAdapter(thumbails,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(thumbnailsAdapter);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");

        DatabaseReference userRef = database.getReference("user_account_settings");

        if (userRef != null && mAuth.getCurrentUser() != null) {

            userRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userAccountSettingsModel = dataSnapshot.getValue(userAccountSettingsModel.class);
                    usernameInAppbar.setText(userAccountSettingsModel.getUsername());
                    about.setText(userAccountSettingsModel.getDescription());
                    follower_numbers.setText(String.valueOf(userAccountSettingsModel.getFollowers()));
                    following_numbers.setText(String.valueOf(userAccountSettingsModel.getFollowing()));

                    Picasso.get().load(userAccountSettingsModel.getProfile_photo()).into(avatar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }

        if(i==1){
            DatabaseReference myPosts = database.getReference("user_photos/"+mAuth.getUid());
            myPosts.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    thumbails.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("image_paths").getValue() != null) {
                            thumbails.add(new thumbnailsModel((ArrayList<String>) snapshot.child("image_paths").getValue(), snapshot.child("user_id").getValue().toString(), snapshot.child("post_id").getValue().toString()));
                        }
                    }
                    post_numbers.setText(String.valueOf(thumbails.size()));
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
            case R.id.all_pictures:
                getData(1);
                break;
            case R.id.video:
//                Intent intent2 = new Intent();
//                intent2.setType("image/*");
//                intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent2.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent2,"Select Picture"), 1);
                getData(2);
                break;
//            case R.id.add_button_account:
//                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    settingBtn.setEnabled(false);
//                }else{
//                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    settingBtn.setEnabled(true);
//                }
//                break;
//            case R.id.setting_button_account:
//                if (settingBottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
//                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    upItemBtn1.setEnabled(false);
//
//                }else{
//                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    upItemBtn1.setEnabled(true);
//                }
//                break;
            case R.id.change_password:
                Bundle bundle = new Bundle();
                bundle.putSerializable("type","change password");
//                bundle.putString("email", mAuth.getCurrentUser().getEmail());
                Intent intent1 = new Intent(getContext(), navigationActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.setting_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), loginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;

        }
    }
}