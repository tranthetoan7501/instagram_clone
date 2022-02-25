package com.hcmus.mobilappsocialnetworkingimage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.hcmus.mobilappsocialnetworkingimage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new homeFragment());
        binding.navigation.setOnNavigationItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.homeFragment:
                    replaceFragment(new accountFragment());
                    break;
                case R.id.accountFragment:
                    replaceFragment(new homeFragment());
                    break;
            }
            return true;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,login.class));
        }
        else {
            mAuth.signOut();
        }
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout,fragment);
        fragmentTransaction.commit();
    }
}