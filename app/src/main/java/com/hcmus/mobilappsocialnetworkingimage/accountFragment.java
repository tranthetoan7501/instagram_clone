package com.hcmus.mobilappsocialnetworkingimage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.api.Distribution;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

public class accountFragment extends Fragment implements FragmentCallbacks{
    ImageView avatar;
    Button edit_pf;
    TextView username,about;
    User user;
    public static String USER="USER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
       avatar = view.findViewById(R.id.avatar);
       username=view.findViewById(R.id.username);
       about=view.findViewById(R.id.description);
        edit_pf =view.findViewById(R.id.edit_pf);
        edit_pf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), edit_profile.class);
                intent.putExtra(USER,user);
                startActivity(intent);
            }
        });
//        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAkfPHSBKmkBxQOjAQPB3jvYaBaQ9a6bh_rA&usqp=CAU").into(avatar);
        if (container != null) {
            container.removeAllViews();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
           }



    @Override
    public void onMsgFromMainToFragment(User user) {
        this.user=user;
        username.setText(user.getUsername());
        about.setText(user.getAbout());
    }
}