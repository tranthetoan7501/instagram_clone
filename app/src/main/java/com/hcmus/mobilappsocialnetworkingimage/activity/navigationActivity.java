package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.fragment.*;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;

import java.util.ArrayList;

public class navigationActivity extends FragmentActivity {
    commentFragment commentFragment = new commentFragment();
    changePassword changePassword = new changePassword();
    profileFragment profileFragment = new profileFragment();
    postFragment postFragment = new postFragment();
    Bundle bundle = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        bundle = getIntent().getExtras();

        if(bundle.get("type").equals("comment")){
            commentFragment.setArguments(bundle);
            replaceFragment(commentFragment);
        }
        else if(bundle.get("type").equals("change password")){
            Bundle bundle1=new Bundle();
            bundle1.putString("email", bundle.getString("email"));
            changePassword.setArguments(bundle1);
            replaceFragment(changePassword);
        } else if (bundle.get("type").equals("profile")) {
            profileFragment.setArguments(bundle);
            replaceFragment(profileFragment);
        }
    }

    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    public void turnOnFragment(String fragment, thumbnailsModel post){
        if(fragment.equals("postFragment")){
            Fragment f = getSupportFragmentManager().findFragmentByTag("accountFragment");
//            if(f!=null && f.isVisible()){
//                previousFragment = "accountFragment";
//            }
//            else{
//                previousFragment = "favoriteFragment";
//            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("post_id",post.getPost_id());
            bundle.putSerializable("user_id",post.getUser_id());
            bundle.putStringArrayList("image_paths",(ArrayList<String>) post.getImage_paths());
            postFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout,postFragment);
            fragmentTransaction.addToBackStack("postFragment");
            fragmentTransaction.commit();
            return;
        }
    }

}