package com.hcmus.mobilappsocialnetworkingimage;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.hcmus.mobilappsocialnetworkingimage.fragment.*;

public class secondActivity extends FragmentActivity {
    commentFragment commentFragment = new commentFragment();
    changePassword changePassword = new changePassword();

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
            replaceFragment(changePassword);
        }
    }

    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

}