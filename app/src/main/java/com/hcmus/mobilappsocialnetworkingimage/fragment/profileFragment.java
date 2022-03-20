package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.squareup.picasso.Picasso;

public class profileFragment extends Fragment implements View.OnClickListener{

    ImageButton previous;
    ImageView avatar;
    TextView username;
    Bundle bundle = new Bundle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        previous = view.findViewById(R.id.previous);
        previous.setOnClickListener(this);
        avatar = view.findViewById(R.id.avatar);
        username = view.findViewById(R.id.username);
        bundle = getArguments();
        Picasso.get().load(bundle.get("avatar").toString()).into(avatar);
        username.setText(bundle.get("username").toString());

        getData();
        return view;
    }

    void getData(){


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.previous:
                getActivity().finish();
                break;
        }
    }
}
