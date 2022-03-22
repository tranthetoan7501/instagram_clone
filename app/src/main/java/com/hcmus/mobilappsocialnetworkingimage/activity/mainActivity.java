package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.thumbnailsAdapter;
import com.hcmus.mobilappsocialnetworkingimage.fragment.accountFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.activityFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.homeFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.postFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.searchFragment;
import com.hcmus.mobilappsocialnetworkingimage.model.userModel;
import com.hcmus.mobilappsocialnetworkingimage.model.postsModel;
import com.hcmus.mobilappsocialnetworkingimage.utils.networkChangeListener;


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
        mAuth=FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() ==null) {
            startActivity(new Intent(this, loginActivity.class));
        }
        else{

//            FirebaseFirestore db=FirebaseFirestore.getInstance();
//            DocumentReference docRef = db.collection("account").document(mAuth.getUid());
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        account = FirebaseAuth.getInstance().getCurrentUser();
//                        _document = FirebaseFirestore.getInstance();
//                        _accountFragment.setUserInfo(account,_document);
//                        if (document.exists()) {
//                            user=new User(document.get("username").toString()
//                                    ,document.get("email").toString()
//                                    ,document.get("about").toString());
//
//                        } else {
//                            Log.d(TAG, "No such document");
//                        }
//                    } else {
//                        Log.d(TAG, "get failed with ", task.getException());
//                    }
//                }
//            });
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter =new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
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
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;

                case R.id.searchFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(_searchFragment).commit();
                    activeFragment = _searchFragment;
                    return true;

                case R.id.accountFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(_accountFragment).commit();
                    activeFragment = _accountFragment;

                    return true;
                case R.id.favoriteFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    fragmentManager.beginTransaction().hide(activeFragment).show(activityFragment).commit();
                    activeFragment = activityFragment;
                    return true;
            }
            return false;
        }
    };

    public void turnOnFragment(String fragment, postsModel post){
        if(fragment.equals("postFragment")){
            Fragment f = fragmentManager.findFragmentByTag("accountFragment");
            if(f!=null && f.isVisible()){
                previousFragment = "accountFragment";
            }
            else{
                previousFragment = "favoriteFragment";
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("title",post.getTitle());
//            Gson gson = new Gson();
//            bundle.putSerializable("comments", gson.toJson(post.getComments()));
//            bundle.putSerializable("likes",gson.toJson(post.getLikes()));
            bundle.putSerializable("images",post.getImages().toString());
            postFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout,postFragment);
            fragmentTransaction.addToBackStack(postFragment.toString());
            fragmentTransaction.commit();
            return;
        }
    }
}