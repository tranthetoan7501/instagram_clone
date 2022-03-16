package com.hcmus.mobilappsocialnetworkingimage;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import utils.Permissions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hcmus.mobilappsocialnetworkingimage.fragment.galleryFragment;
import com.hcmus.mobilappsocialnetworkingimage.fragment.photoFragment;
import com.hcmus.mobilappsocialnetworkingimage.utils.sectionsPagerAdapter;

import java.util.Map;
import com.hcmus.mobilappsocialnetworkingimage.adapter.*;
import com.hcmus.mobilappsocialnetworkingimage.fragment.*;
public class MainActivity extends FragmentActivity implements View.OnClickListener {

    // constants
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    Fragment activeFragment;
    FragmentManager fragmentManager = getSupportFragmentManager();
    accountFragment _accountFragment = new accountFragment();
    Fragment homeFragment = new homeFragment();
    Fragment searchFragment = new searchFragment();
    Fragment activityFragment = new activityFragment();
    Fragment postFragment = new postFragment();
    String previousFragment ="";
    View appbar;
    View appbar2;
    View appbar3;
    View appbar4;
    View appbar5;
    thumbnailsAdapter thumbnailsAdapter;
    private ImageButton upItemBtn;
    LinearLayout logout;

    ImageButton upItemBtn1;
    ImageButton previous;
    ImageButton cameraBtn;

    FirebaseUser account;
    FirebaseFirestore _document;

    private LinearLayout layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    ImageButton settingBtn;
    private LinearLayout layoutSettingBottomSheet;
    private BottomSheetBehavior settingBottomSheetBehavior;

    NetworkChangeListener networkChangeListener=new NetworkChangeListener();
    public static Activity fa;
    UserInfor user;
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
        appbar5 = findViewById(R.id.post_appbar);
        upItemBtn = findViewById(R.id.add_button);
        upItemBtn1 = findViewById(R.id.add_button_account);
        settingBtn = findViewById(R.id.setting_button_account);
        previous = findViewById(R.id.previous);
        cameraBtn = findViewById(R.id.camera);

        logout = findViewById(R.id.logout);

        layoutBottomSheet= findViewById(R.id.bottomSheetContainer);
        layoutSettingBottomSheet = findViewById(R.id.settingBottomSheetContainer);

        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        settingBottomSheetBehavior = BottomSheetBehavior.from( layoutSettingBottomSheet);

        upItemBtn.setOnClickListener(this);
        upItemBtn1.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        previous.setOnClickListener(this);
        logout.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                findViewById(R.id.container).setAlpha((float) 1.5 - slideOffset);
            }
        });

        settingBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
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
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() ==null) {
            startActivity(new Intent(this,login.class));
        }
        else{
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://social-media-f92fc-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("account").child(mAuth.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> value = (Map<String, Object>)dataSnapshot.getValue();

                    UserInfor userInfor=new UserInfor(value.get("username").toString()
                                                    ,value.get("email").toString()
                                                    ,value.get("about").toString()
                                                    ,value.get("avatar").toString());
                    _accountFragment.setUserInfo(userInfor,mAuth);

                    //In appbar 2
                    TextView textViewInAppbar2=appbar2.findViewById(R.id.username);
                    textViewInAppbar2.setText(userInfor.getUsername());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
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
        fragmentManager.beginTransaction().add(R.id.fragment_layout, searchFragment, searchFragment.getTag()).hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_layout, activityFragment, activeFragment.toString()).hide(activityFragment).commit();
//        fragmentManager.beginTransaction().add(R.id.fragment_layout, postFragment, "5").hide(postFragment).commit();
        appbar.setVisibility(View.VISIBLE);
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
                    appbar.setVisibility(View.VISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    appbar4.setVisibility(View.INVISIBLE);
                    appbar5.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;

                case R.id.searchFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.VISIBLE);
                    appbar4.setVisibility(View.INVISIBLE);
                    appbar5.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                    activeFragment = searchFragment;
                    return true;

                case R.id.accountFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }

                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.VISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    appbar4.setVisibility(View.INVISIBLE);
                    appbar5.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(_accountFragment).commit();
                    activeFragment = _accountFragment;
                    return true;
                case R.id.favoriteFragment:
                    if(fragmentManager != null) {
                        fragmentManager.popBackStack(postFragment.toString(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                    appbar.setVisibility(View.INVISIBLE);
                    appbar2.setVisibility(View.INVISIBLE);
                    appbar3.setVisibility(View.INVISIBLE);
                    appbar4.setVisibility(View.VISIBLE);
                    appbar5.setVisibility(View.INVISIBLE);
                    fragmentManager.beginTransaction().hide(activeFragment).show(activityFragment).commit();
                    activeFragment = activityFragment;
                    return true;
            }
            return false;
        }
    };

    public void turnOnFragment(String fragment, Bundle bundle){
        Bundle bundle1 = bundle;
        if(fragment.equals("postFragment")){
            Fragment f = getSupportFragmentManager().findFragmentByTag("accountFragment");
//            Fragment i = getSupportFragmentManager().findFragmentByTag("searchFragment");
            if(f != null && f.isVisible()){
                previousFragment = "accountFragment";
            }
            else {
                previousFragment = "searchFragment";
            }
            appbar5.setVisibility(View.VISIBLE);
            appbar.setVisibility(View.INVISIBLE);
            appbar2.setVisibility(View.INVISIBLE);
            appbar3.setVisibility(View.INVISIBLE);
            appbar4.setVisibility(View.INVISIBLE);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_layout,postFragment);
            fragmentTransaction.addToBackStack("postFragment");
            fragmentTransaction.commit();
            return;
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_button:
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    findViewById(R.id.container).setAlpha((float) 0.5);
                    settingBtn.setEnabled(false);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    settingBtn.setEnabled(true);
                }

                break;
            case R.id.add_button_account:
                if (bottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    settingBtn.setEnabled(false);
                }else{
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    settingBtn.setEnabled(true);
                }
                break;
            case R.id.setting_button_account:
                if (settingBottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    upItemBtn.setEnabled(false);
                    upItemBtn1.setEnabled(false);

                }else{
                    settingBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    upItemBtn.setEnabled(true);
                    upItemBtn1.setEnabled(true);
                }
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,login.class);
                startActivity(intent);
                finish();

                break;
            case R.id.previous:
                getSupportFragmentManager().popBackStack("postFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                appbar5.setVisibility(View.INVISIBLE);
                if(previousFragment.equals("accountFragment")){
                    appbar2.setVisibility(View.VISIBLE);
                }
                else{
                    appbar3.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.camera:
                setContentView(R.layout.activity_share);
                if(checkPermissionsArray(Permissions.PERMISSIONS)) {
                    setupViewPager();
                } else {
                    verifyPermissions(Permissions.PERMISSIONS);
                }
                break;
        }
    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    private boolean checkPermissionsArray(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];
            if (!checkPermissions(check)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPermissions(String permission) {
        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;
    }

    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    private void setupViewPager() {
        sectionsPagerAdapter adapter = new sectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new galleryFragment());
        adapter.addFragment(new photoFragment());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("GALLERY");
        tabLayout.getTabAt(1).setText("PHOTO");

    }
}