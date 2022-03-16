package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hcmus.mobilappsocialnetworkingimage.R;


public class postFragment extends Fragment {
    View appbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post, container, false);
//        appbar = view.findViewById(R.id.post_appbar);
//        appbar.setVisibility(View.VISIBLE);
        return view;
    }
}