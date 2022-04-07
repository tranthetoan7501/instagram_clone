package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.editProfileActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.mainActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.nextActivity;
import com.hcmus.mobilappsocialnetworkingimage.activity.shareActivity;
import com.hcmus.mobilappsocialnetworkingimage.photoEditor.EditImageActivity;
import com.hcmus.mobilappsocialnetworkingimage.utils.permissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class photoFragment extends Fragment {

    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int CAMERA_REQUEST_CODE = 5;
    private static final int NEXT_ACTIVITY_REQUEST_CODE = 2;
    public final String APP_TAG = "MyCustomApp";
    public String photoFileName = "photo.jpg";
    File photoFile;
    Bitmap bitmap;

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
                        photoFile = getPhotoFileUri(photoFileName);
                        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.hcmus.mobilappsocialnetworkingimage.provider", photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                            startActivityForResult(cameraIntent, editProfileActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), mainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        
        return view;
    }
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == editProfileActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                editPicture();
            } else if (requestCode == NEXT_ACTIVITY_REQUEST_CODE) {
//                bitmap = EditImageActivity.byteToBitmap(data.getByteArrayExtra("imagePath"));
//                Intent intent = new Intent(getActivity(), nextActivity.class);
//                intent.putExtra("imageBitmap", bitmap);
//                startActivity(intent);

                String path = data.getStringExtra("imagePath");
                Log.d("path", path);
                ArrayList<String> imagePaths = new ArrayList<>();
                imagePaths.add(path);
                Intent intent = new Intent(getActivity(), nextActivity.class);
                intent.putExtra(getString(R.string.selected_image), imagePaths);
                startActivity(intent);
            }
        }
    }

    private void editPicture() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytesArrayBmp = baos.toByteArray();
        Intent intent= new Intent(getActivity(),EditImageActivity.class);
        Bundle bundle=new Bundle();
        bundle.putByteArray("ImagePath",bytesArrayBmp);
        intent.putExtras(bundle);
        startActivityForResult(intent, NEXT_ACTIVITY_REQUEST_CODE);
    }
}
