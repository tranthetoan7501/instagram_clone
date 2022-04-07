package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class editpostFragment extends Fragment implements View.OnClickListener {
    ImageButton previous;
    ImageButton done;
    Bundle bundle = new Bundle();
    CircleImageView avatar;
    TextView username;
    ImageSlider imageSlider;
    EditText edit_caption;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.edit_post, container, false);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        bundle = this.getArguments();
        avatar = view.findViewById(R.id.avatar);
        username = view.findViewById(R.id.up_name);
        imageSlider = view.findViewById(R.id.image_slider);
        edit_caption = view.findViewById(R.id.edit_caption);
        done = view.findViewById(R.id.done);
        done.setOnClickListener(this);
        getData();
        return view;
    }

    void getData(){
        Picasso.get().load(bundle.getString("profile_photo")).into(avatar);
        username.setText(bundle.getString("username"));
        ArrayList<String> myImages = (ArrayList<String>) bundle.get("image_paths");
        List<SlideModel> imageList = new ArrayList<SlideModel>();
        for(String s: myImages){
            imageList.add(new SlideModel(s, ScaleTypes.FIT));
        }
        imageSlider.setImageList(imageList);
        edit_caption.setText(bundle.getString("caption"));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.done:
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference editPost = database.getReference("user_photos/"+bundle.get("user_id")+"/"+bundle.get("post_id"));
                editPost.child("caption").setValue(edit_caption.getText().toString());
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
