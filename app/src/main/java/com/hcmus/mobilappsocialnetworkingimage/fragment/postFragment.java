package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.hcmus.mobilappsocialnetworkingimage.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class postFragment extends Fragment implements View.OnClickListener {
    Bundle bundle = new Bundle();
    ImageSlider imageSlider;
    TextView num_likes;
    TextView description;
    TextView date;
    ImageButton comment;
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
        getData();
        return view;
    }

    void getData(){
        bundle = this.getArguments();
        List<String> myList = new ArrayList<String>(Arrays.asList(bundle.get("images").toString().split(",")));
        List<SlideModel> imageList = new ArrayList<SlideModel>();
        for(String s: myList){
            imageList.add(new SlideModel(s));
        }
        imageSlider.setImageList(imageList,false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment:
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("comments",bundle1.get("comments").toString());
                break;
        }
    }
}