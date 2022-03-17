package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hcmus.mobilappsocialnetworkingimage.R;


public class changePassword extends Fragment  implements View.OnClickListener{
    ImageButton previous;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.previous:
                getActivity().finish();
                break;
        }
    }
}