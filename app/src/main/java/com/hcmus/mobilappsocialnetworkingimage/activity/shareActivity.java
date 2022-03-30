package com.hcmus.mobilappsocialnetworkingimage.activity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.fragment.*;
import com.hcmus.mobilappsocialnetworkingimage.utils.*;

public class shareActivity extends AppCompatActivity {

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        if(checkPermissionsArray(permissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            verifyPermissions(permissions.PERMISSIONS);
        }
    }

    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    private void verifyPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                shareActivity.this,
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
        int permissionRequest = ActivityCompat.checkSelfPermission(shareActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else
            return true;
    }

    private void setupViewPager() {
        sectionsPagerAdapter adapter = new sectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new galleryFragment());
        adapter.addFragment(new photoFragment());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText("GALLERY");
        tabLayout.getTabAt(1).setText("PHOTO");

    }

    public void saveBitmapToImage(Bitmap bitmap) {
        // save image to gallery
        //imageUtils.saveImageToGallery(shareActivity.this, bitmap);
    }
}
