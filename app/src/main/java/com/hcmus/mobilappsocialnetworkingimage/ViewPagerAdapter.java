package com.hcmus.mobilappsocialnetworkingimage;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments = new ArrayList<>();
    List<String> fragmentTitle = new ArrayList<>();
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return fragmentTitle.get(position);
//    }

    public void addFragment(Fragment fragment){
        fragments.add(fragment);
//        fragmentTitle.add(title);
    }
}
