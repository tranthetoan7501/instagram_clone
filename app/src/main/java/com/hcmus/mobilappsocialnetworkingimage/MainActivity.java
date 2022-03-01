package com.hcmus.mobilappsocialnetworkingimage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.mobilappsocialnetworkingimage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment accountFragment = new accountFragment();
    Fragment homeFragment = new homeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavegationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;
                case R.id.accountFragment:
                    fragmentManager.beginTransaction().hide(activeFragment).show(accountFragment).commit();
                    activeFragment = accountFragment;
                    return true;
            }
            return false;
        }
    };
}