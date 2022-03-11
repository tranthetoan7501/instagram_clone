package com.hcmus.mobilappsocialnetworkingimage;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import fragment.homeFragment;
import fragment.searchFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener,MainCallbacks {
    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    accountFragment _accountFragment = new accountFragment();
    Fragment homeFragment = new homeFragment();
    Fragment searchFragment = new searchFragment();
    Fragment activityFragment = new activityFragment();
    View appbar;
    View appbar2;
    View appbar3;
    View appbar4;
    private ImageButton upItemBtn;
    ImageButton upItemBtn1;
    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    public static Activity fa;
    User user;
    MainActivity main;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fa=this;


        appbar = findViewById(R.id.home_appbar);
        appbar2 = findViewById(R.id.account_appbar);
        appbar3 = findViewById(R.id.search_appbar);
        appbar4 = findViewById(R.id.favorite_appbar);

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
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
//        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance(" https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app");
//        DatabaseReference databaseReference=firebaseDatabase.getReference("ALO");
//        databaseReference.setValue(new User("ALO","AC","123"));
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() ==null) {
            startActivity(new Intent(this,login.class));
        }
        else{
            FirebaseFirestore db=FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("account").document(mAuth.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            user=new User(document.get("username").toString()
//                                    ,document.get("email").toString()
//                                    ,document.get("about").toString());
                            user=new User(document.get("username").toString()
                                    ,document.get("email").toString());
                            _accountFragment.onMsgFromMainToFragment(user);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
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
        fragmentManager.beginTransaction().add(R.id.fragment_layout, _accountFragment, "4").hide(_accountFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, homeFragment, "1").commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, searchFragment, "2").hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, activityFragment, "3").hide(activityFragment).commit();
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
                    appbar4.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;

                case R.id.searchFragment:
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.VISIBLE);
                    appbar4.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                    activeFragment = searchFragment;
                    return true;

                case R.id.accountFragment:
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.VISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    appbar4.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(_accountFragment).commit();
                    activeFragment = _accountFragment;
                    return true;
                case R.id.favoriteFragment:
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    appbar4.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(activityFragment).commit();
                    activeFragment = activityFragment;
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

    @Override
    public void onMsgFromFragtoMain(User user) {
        _accountFragment.onMsgFromMainToFragment(user);
    }
}