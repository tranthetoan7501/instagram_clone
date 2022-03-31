package com.hcmus.mobilappsocialnetworkingimage.fragment;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.hcmus.mobilappsocialnetworkingimage.model.notificationsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class postFragment extends Fragment implements View.OnClickListener {
    Bundle bundle = new Bundle();
    ImageSlider imageSlider;
    TextView num_likes;
    TextView description;
    TextView date;
    ImageButton comment;
    View appbar;
    ImageButton previous;
    CircleImageView avatar;
    TextView username;
    TextView username1;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    ImageButton like;
    ImageButton liked;
    String post_id;
    Button setting;
    ArrayList<String> likes = new ArrayList<>();
    private LinearLayout layoutSettingBottomSheet;
    private BottomSheetBehavior settingBottomSheetBehavior;
    LinearLayout modify;
    LinearLayout post_owner;
    Bundle bundle2 = new Bundle();
    LinearLayout delete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        bundle = this.getArguments();
        imageSlider = view.findViewById(R.id.image_slider);
        num_likes = view.findViewById(R.id.num_likes);
        description = view.findViewById(R.id.description);
        date = view.findViewById(R.id.date);
        comment = view.findViewById(R.id.comment);
        comment.setOnClickListener(this);
        appbar = view.findViewById(R.id.appbar_post);
        appbar.setVisibility(View.VISIBLE);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        avatar = view.findViewById(R.id.avatar);
        username = view.findViewById(R.id.up_name);
        username1 = view.findViewById(R.id.below_name);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        like = view.findViewById(R.id.like);
        like.setOnClickListener(this);
        liked = view.findViewById(R.id.liked);
        liked.setOnClickListener(this);
        layoutSettingBottomSheet= getActivity().findViewById(R.id.post_setting);
        setting = view.findViewById(R.id.setting_button);
        setting.setOnClickListener(this);
        if(bundle.getString("user_id").equals(mAuth.getUid())){
            post_owner = getActivity().findViewById(R.id.post_owner);
            post_owner.setVisibility(View.VISIBLE);
            modify = getActivity().findViewById(R.id.modify);
            modify.setOnClickListener(this);
            delete = getActivity().findViewById(R.id.delete);
            delete.setOnClickListener(this);
        }
        getData();
        setBottomSheet();
        return view;
    }

    void getData(){
        ArrayList<String> myImages = (ArrayList<String>) bundle.get("image_paths");
        List<SlideModel> imageList = new ArrayList<SlideModel>();
        for(String s: myImages){
            imageList.add(new SlideModel(s));
        }
        bundle2.putStringArrayList("image_paths", myImages);
        imageSlider.setImageList(imageList,false);
        post_id = bundle.getString("post_id");
        DatabaseReference postDetails = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id"));
        postDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    return;
                likes.clear();
                date.setText(dataSnapshot.child("date_created").getValue().toString());
                description.setText(dataSnapshot.child("caption").getValue().toString());
                bundle2.putString("caption",description.getText().toString());
                if(dataSnapshot.child("likes").getChildrenCount() != 0 ){
                    likes = (ArrayList<String>) dataSnapshot.child("likes").getValue();
                    if(likes.contains(mAuth.getUid())){
                        like.setVisibility(View.INVISIBLE);
                        liked.setVisibility(View.VISIBLE);
                    }
                    num_likes.setVisibility(View.VISIBLE);
                    if(likes.size() > 1 ){
                        num_likes.setText(Html.fromHtml("<b>" +likes.size() + " likes</b>" ));
                    }
                    else{
                        num_likes.setText(Html.fromHtml("<b>" +likes.size() + " like</b>" ));
                    }
                }
                else{
                    num_likes.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        DatabaseReference myPosts = database.getReference("user_account_settings/"+bundle.get("user_id"));
        myPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bundle2.putString("profile_photo",dataSnapshot.child("profile_photo").getValue().toString());
                Picasso.get().load(dataSnapshot.child("profile_photo").getValue().toString()).into(avatar);
                bundle2.putString("username",dataSnapshot.child("username").getValue().toString());
                username.setText(dataSnapshot.child("username").getValue().toString());
                username1.setText(dataSnapshot.child("username").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    void setBottomSheet(){
        settingBottomSheetBehavior = BottomSheetBehavior.from(layoutSettingBottomSheet);

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


    @Override
    public void onClick(View view) {
        String key = UUID.randomUUID().toString();

        switch (view.getId()){
            case R.id.comment:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("type","comment");
                bundle1.putSerializable("post_id",bundle.getString("post_id"));
                bundle1.putSerializable("user_id",bundle.getString("user_id"));
                Intent intent = new Intent(getContext(), navigationActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            case R.id.like:
                DatabaseReference myLike = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id")+"/"+"likes");
                if(myLike != null){
                    if(likes !=null)
                        myLike.child(likes.size()+"").setValue(mAuth.getUid());
                    else{
                        myLike.child("0").setValue(mAuth.getUid());
                    }
                }
                else{
                    myLike.child("likes/0").setValue(mAuth.getUid());
                }
                like.setVisibility(View.INVISIBLE);
                liked.setVisibility(View.VISIBLE);
                if(!mAuth.getUid().equals(bundle.getString("user_id"))) {
                    DatabaseReference myNotification = database.getReference();
                    myNotification.child("notification").child(bundle.getString("user_id")).child(key).setValue(new notificationsModel(key,mAuth.getUid(),bundle.getString("post_id"),"liked your post.","22/3/2022",false));
                }
                break;
            case R.id.liked:
                DatabaseReference myLike1 = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id")+"/"+"likes");
                myLike1.child(likes.indexOf(mAuth.getUid())+"").removeValue();
                like.setVisibility(View.VISIBLE);
                liked.setVisibility(View.INVISIBLE);
                if(!mAuth.getUid().equals(bundle.getString("user_id"))) {
                    DatabaseReference myNotification = database.getReference();
                    myNotification.child("notification").child(bundle.getString("user_id")+"/"+key).removeValue();
                }
                break;
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack("postFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.setting_button:
                if (settingBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED || settingBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING){
                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.modify:
                bundle2.putString("post_id",bundle.getString("post_id"));
                bundle2.putString("user_id",bundle.getString("user_id"));
                editpostFragment editpostFragment = new editpostFragment();
                editpostFragment.setArguments(bundle2);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, editpostFragment).addToBackStack("editpostFragment").commit();
                settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.delete:
                settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                DatabaseReference deletePost = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id"));
                                deletePost.removeValue();
                                getActivity().getSupportFragmentManager().popBackStack("postFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;


        }
    }
}