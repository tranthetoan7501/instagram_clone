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
import androidx.fragment.app.FragmentManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class editpostFragment extends Fragment implements View.OnClickListener {
    ImageButton previous;
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
        getData();
        return view;
    }

    void getData(){
        Picasso.get().load(bundle.getString("profile_photo")).into(avatar);
        username.setText(bundle.getString("username"));
        ArrayList<String> myImages = (ArrayList<String>) bundle.get("image_paths");
        List<SlideModel> imageList = new ArrayList<SlideModel>();
        for(String s: myImages){
            imageList.add(new SlideModel(s));
        }
        imageSlider.setImageList(imageList,false);
        edit_caption.setText(bundle.getString("caption"));
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.previous:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }
}
