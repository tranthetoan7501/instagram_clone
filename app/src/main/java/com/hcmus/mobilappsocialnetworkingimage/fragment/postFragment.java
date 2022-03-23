package com.hcmus.mobilappsocialnetworkingimage.fragment;

import static android.content.ContentValues.TAG;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.model.postsModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post, container, false);
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
        getData();
        return view;
    }

    void getData(){
        bundle = this.getArguments();
        ArrayList<String> myImages = (ArrayList<String>) bundle.get("image_paths");
        List<SlideModel> imageList = new ArrayList<SlideModel>();
        for(String s: myImages){
            imageList.add(new SlideModel(s));
        }
        imageSlider.setImageList(imageList,false);

//        ArrayList<String> likes = (ArrayList<String>) bundle.get("num_likes");
//        num_likes.setText(likes.size() +"");
        date.setText(bundle.get("date_created").toString());
        description.setText(bundle.get("caption").toString());

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myPosts = database.getReference("user_account_settings/"+mAuth.getUid());
        myPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.get().load(dataSnapshot.child("profile_photo").getValue().toString()).into(avatar);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("comments",bundle1.get("comments").toString());
                break;
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack("postFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}