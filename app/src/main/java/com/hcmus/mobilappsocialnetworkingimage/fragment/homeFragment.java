package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.shareActivity;

import java.util.ArrayList;
import java.util.List;

import adapter.postsAdapter;
import adapter.storiesAdapter;


public class homeFragment extends Fragment {
    RecyclerView stories;
    adapter.storiesAdapter storiesAdapter;
    RecyclerView posts;
    adapter.postsAdapter postsAdapter;
    ImageButton upItemBtn;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView header_Arrow_Image;
    private LinearLayout post_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        stories = view.findViewById(R.id.stories);
        posts = view.findViewById(R.id.posts);
        upItemBtn = view.findViewById(R.id.add_button);

        getDataStories();
        getDataPosts();
        setBottomSheetBehavior();
        return view;
    }

    void setBottomSheetBehavior(){
        layoutBottomSheet= getActivity().findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        header_Arrow_Image = getActivity().findViewById(R.id.header_Arrow_Image);
        post_layout = getActivity().findViewById(R.id.post_layout);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                getActivity().findViewById(R.id.container).setAlpha((float) 1.5 - slideOffset);
            }
        });

        upItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        header_Arrow_Image.setOnClickListener(v -> {
            if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        post_layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), shareActivity.class);
            startActivity(intent);
        });
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
}