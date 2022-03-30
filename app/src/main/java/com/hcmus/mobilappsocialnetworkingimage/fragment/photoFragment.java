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
import com.hcmus.mobilappsocialnetworkingimage.activity.mainActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.nextActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.shareActivity;
import com.hcmus.mobilappsocialnetworkingimage.utils.filterImage;
import com.hcmus.mobilappsocialnetworkingimage.utils.permissions;

public class photoFragment extends Fragment {

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int CAMERA_REQUEST_CODE = 5;
    private filterImage filterImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((shareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    if (((shareActivity) getActivity()).checkPermissions(permissions.CAMERA_PERMISSION[0])) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(getActivity(), mainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Bitmap bitmap;
            bitmap = (Bitmap) data.getExtras().get("data");
            filterImage = new filterImage(getContext(), bitmap);
            filterImage.sendToEdit();
            // switch to next Activity
            Intent intent = new Intent(getActivity(), nextActivity.class);
            bitmap = Bitmap.createScaledBitmap(filterImage.getmBitmap(),200,200,true);
            // save bitmap to image and get image path
            ((shareActivity) getActivity()).saveBitmapToImage(bitmap);
            intent.putExtra(getString(R.string.selected_image), bitmap);
            startActivity(intent);
        }
    }
}
