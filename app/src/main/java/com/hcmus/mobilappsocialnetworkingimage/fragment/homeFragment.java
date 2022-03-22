package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.shareActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.postsAdapter;
import adapter.storiesAdapter;


public class homeFragment extends Fragment implements View.OnClickListener {
    RecyclerView stories;
    adapter.storiesAdapter storiesAdapter;
    RecyclerView posts;
    adapter.postsAdapter postsAdapter;
    ImageButton upItemBtn;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    ImageButton cameraButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if (container != null) {
//            container.removeAllViews();
//        }
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        stories = view.findViewById(R.id.stories);
        posts = view.findViewById(R.id.posts);
        upItemBtn = view.findViewById(R.id.add_button);
        cameraButton = view.findViewById(R.id.camera);
        cameraButton.setOnClickListener(this);
        getDataStories();
        getDataPosts();
        setBottomSheetBehavior();
        return view;
    }

    void setBottomSheetBehavior(){
        layoutBottomSheet= getActivity().findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                getActivity().findViewById(R.id.container).setAlpha((float) 1.5 - slideOffset);
            }
        });
        upItemBtn.setOnClickListener(this);
    }



    void getDataStories(){
        List<String> name = new ArrayList<>();
        List<String> image = new ArrayList<>();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        storiesAdapter = new storiesAdapter(image,name,getContext());
        stories.setLayoutManager(linearLayout);
        stories.setAdapter(storiesAdapter);
        name.add("Sơn Thanh");
        name.add("Vinh Quang");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        name.add("Sơn Thanh");
        name.add("Vinh Quang");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        name.add("Sơn Thanh");
        name.add("Vinh Quang");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        storiesAdapter.notifyDataSetChanged();

    }

    void getDataPosts(){
        List<String> name = new ArrayList<>();
        List<String> image = new ArrayList<>();
        List<String> description = new ArrayList<>();
        List<String> date = new ArrayList<>();
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        postsAdapter = new postsAdapter(name,image,description,date,getContext());
        posts.setLayoutManager(linearLayout);
        posts.setAdapter(postsAdapter);
        name.add("Sơn Thanh");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        description.add("Anh em nhóm 5 thánh bú liếm");
        date.add("27/10/2001");
        name.add("Sơn Thanh");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        description.add("Anh em nhóm 5 thánh bú liếm");
        date.add("27/10/2001");
        name.add("Sơn Thanh");
        image.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU");
        description.add("Anh em nhóm 5 thánh bú liếm");
        date.add("27/10/2001");
        postsAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_button:
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.camera:
                Intent shareIntent = new Intent(getContext(), shareActivity.class);
                startActivity(shareIntent);
                break;
        }
    }
}