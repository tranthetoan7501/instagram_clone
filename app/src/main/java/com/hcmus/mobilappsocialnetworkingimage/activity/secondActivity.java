package com.hcmus.mobilappsocialnetworkingimage.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.fragment.*;

public class secondActivity extends FragmentActivity {
    commentFragment commentFragment = new commentFragment();
    changePassword changePassword = new changePassword();
    profileFragment profileFragment = new profileFragment();

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
        }else if(bundle.get("type").equals("profile")){
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

}