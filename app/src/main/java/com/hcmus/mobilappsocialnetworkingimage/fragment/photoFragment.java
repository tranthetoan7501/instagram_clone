package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.nextActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.shareActivity;

public class photoFragment extends Fragment {

    private static final int PHOTO_FRAGMENT_NUM = 1;
    String mCurrentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        
        Button btnLaunchCamera = view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(view1 -> {
            if (((shareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                if (((shareActivity) getActivity()).checkPermissions(utils.permissions.CAMERA_PERMISSION[0])) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 0);

                } else {
                    Intent intent = new Intent(getActivity(), shareActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");
            Intent intent = new Intent(getActivity(), nextActivity.class);
            intent.putExtra("imageBitmap", bitmap);
            startActivity(intent);
        }
    }
}
