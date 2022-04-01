package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.fragment.accountFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.activityFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.homeFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.postFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.searchFragment;
import com.hcmus.mobilappsocialnetworkingimage.model.thumbnailsModel;
import com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener;

import java.util.ArrayList;


public class mainActivity extends FragmentActivity  {

    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    accountFragment _accountFragment = new accountFragment();
    Fragment homeFragment = new homeFragment();
    searchFragment _searchFragment = new searchFragment();
    Fragment activityFragment = new activityFragment();
    Fragment postFragment = new postFragment();

    String previousFragment = "";

    com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener networkChangeListener=new networkChangeListener();
    public static Activity fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fa=this;
        setUpNavigation();
    }

    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        mAuth=FirebaseAuth.getInstance();
        super.onStart();
        if (mAuth.getCurrentUser()==null) {
            startActivity(new Intent(this, loginActivity.class));
        }
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
    void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    void setUpNavigation() {
        activeFragment = homeFragment;
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavegationItemSelectedListener);
        fragmentManager.beginTransaction().add(R.id.fragment_layout, _accountFragment, "accountFragment").hide(_accountFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, homeFragment, homeFragment.toString()).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, _searchFragment, _searchFragment.getTag()).hide(_searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, activityFragment, activeFragment.toString()).hide(activityFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavegationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack("postFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentManager.popBackStack("editpostFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;

                case R.id.searchFragment:
                    mAuth.signOut();
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack("postFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(_searchFragment).commit();
                    activeFragment = _searchFragment;
                    return true;

                case R.id.accountFragment:
                    fragmentManager.beginTransaction().hide(activeFragment).show(_accountFragment).commit();
                    activeFragment = _accountFragment;

                    return true;
                case R.id.favoriteFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack("postFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(activityFragment).commit();
                    activeFragment = activityFragment;
                    return true;
            }
            return false;
        }
    };

    public void turnOnFragment(String fragment, thumbnailsModel post){
        if(fragment.equals("postFragment")){
//[            Fragment f = fragmentManager.findFragmentByTag("accountFragment");
//            if(f!=null && f.isVisible()){
//                previousFragment = "accountFragment";
//            }
//            else{
//                previousFragment = "favoriteFragment";
//            }]
            Bundle bundle = new Bundle();
            bundle.putSerializable("post_id",post.getPost_id());
            bundle.putSerializable("user_id",post.getUser_id());
            bundle.putStringArrayList("image_paths",(ArrayList<String>) post.getImage_paths());
            postFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout,postFragment);
            fragmentTransaction.addToBackStack("postFragment");
            fragmentTransaction.commit();
            return;
        }
    }
}