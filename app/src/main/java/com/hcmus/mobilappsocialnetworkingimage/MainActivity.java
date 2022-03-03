package com.hcmus.mobilappsocialnetworkingimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment accountFragment = new accountFragment();
    Fragment homeFragment = new homeFragment();
    Fragment searchFragment = new searchFragment();
    View appbar;
    View appbar2;
    View appbar3;
    View back_dim_layout;
    private ImageButton upItemBtn;
    ImageButton upItemBtn1;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appbar = findViewById(R.id.home_appbar);
        appbar2 = findViewById(R.id.account_appbar);
        appbar3 = findViewById(R.id.search_appbar);
        upItemBtn = findViewById(R.id.add_button);
        upItemBtn1 = findViewById(R.id.add_button_account);
        layoutBottomSheet= findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        upItemBtn.setOnClickListener(this);
        upItemBtn1.setOnClickListener(this);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.container).setAlpha((float) 1.5 - slideOffset);
            }
        });
        setUpNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, login.class));
        }
//        else {
//            mAuth.signOut();
//        }
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
        fragmentManager.beginTransaction().add(R.id.fragment_layout, accountFragment, "4").hide(accountFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, homeFragment, "1").commit();
        appbar.setVisibility(View.VISIBLE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavegationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    appbar.setVisibility(View.VISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;
                case R.id.accountFragment:
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.VISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(accountFragment).commit();
                    activeFragment = accountFragment;
                    return true;
                case R.id.searchFragment:
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                    activeFragment = searchFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_button:
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    findViewById(R.id.container).setAlpha((float) 0.5);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case R.id.add_button_account:
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
        }
    }
}