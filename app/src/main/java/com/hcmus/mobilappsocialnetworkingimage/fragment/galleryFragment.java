package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.nextActivity;
import com.hcmus.mobilappsocialnetworkingimage.adapter.gridImageAdapter;
import com.hcmus.mobilappsocialnetworkingimage.photoEditor.EditImageActivity;
import com.hcmus.mobilappsocialnetworkingimage.utils.filePaths;
import com.hcmus.mobilappsocialnetworkingimage.utils.fileSearch;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class galleryFragment extends Fragment {

    private static final int NUM_GRID_COL = 4;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 2;
    private static final int NEXT_ACTIVITY_REQUEST_CODE = 3;

    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    private ArrayList<String> directories;
    private ArrayList<String> mSelectedImage = new ArrayList<>();
    private ArrayList<String> mSelectedImageAfterEdit = new ArrayList<>();
    ArrayList<String> imagePaths = new ArrayList<>();
    int count = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        }

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = view.findViewById(R.id.gridView);
        galleryImage = view.findViewById(R.id.galleryImageView);
        mProgressBar = view.findViewById(R.id.progressBar);
        directorySpinner = view.findViewById(R.id.spinnerDirectory);
        mProgressBar.setVisibility(View.GONE);

        ImageView shareClose = view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(view1 -> getActivity().finish());

        TextView nextScreen = view.findViewById(R.id.tvNext);

        nextScreen.setOnClickListener(view12 -> {
                // run edit pictute function and after all picture is edited, go to next screen
                editPicture();
        });

        init();

        return view;
    }

    private void init() {
        filePaths mFilePaths = new filePaths();
        if (fileSearch.getDirectoryPaths(mFilePaths.PICTURES) != null) {
            directories = fileSearch.getDirectoryPaths(mFilePaths.PICTURES);
        }

        directories.add(mFilePaths.PICTURES);
        directories.add(mFilePaths.CAMERA);

        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            directoryNames.add("/" + directories.get(i).split("/")[directories.get(i).split("/").length - 1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupGridView(directories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupGridView(String selectedDirectory) {
        ArrayList<String> imgURLs = fileSearch.getFilePaths(selectedDirectory);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COL;
        gridView.setColumnWidth(imageWidth);

        gridImageAdapter adapter = new gridImageAdapter(getActivity(), R.layout.layout_grid_imageview, imgURLs);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, v, position, id) -> {

            int selectedIndex = adapter.selectedPositions.indexOf(position);
            if (selectedIndex > -1) {
                v.setAlpha(1f);
                adapter.selectedPositions.remove(selectedIndex);
                mSelectedImage.remove(imgURLs.get(position));
                if (mSelectedImage.size() == 0) {
                    // remove gallery view
                    galleryImage.setVisibility(View.GONE);
                } else {
                    setImage(mSelectedImage.get(mSelectedImage.size() - 1), galleryImage);
                }

            } else {
                galleryImage.setVisibility(View.VISIBLE);
                v.setAlpha(0.5f);
                setImage(imgURLs.get(position), galleryImage);
                mSelectedImage.add(imgURLs.get(position));
                adapter.selectedPositions.add(position);
            }
        });

    }

    private void setImage(String imgURL, ImageView image) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage("file:/" + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUrl, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUrl, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUrl, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUrl, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == NEXT_ACTIVITY_REQUEST_CODE) {
                String path = data.getStringExtra("imagePath");
                Log.d("filePath", path);
                imagePaths.add(path);
                if (count == mSelectedImage.size()) {
                    Intent intent = new Intent(getActivity(), nextActivity.class);
                    intent.putExtra(getString(R.string.selected_image), imagePaths);
                    startActivity(intent);
                }
            }
        }
    }

    private void editPicture() {
        for (int i = 0; i < mSelectedImage.size(); i++) {
            Log.d("mSelectedImage", mSelectedImage.get(i));
            count++;
            Bitmap bitmap = BitmapFactory.decodeFile(mSelectedImage.get(i));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytesArrayBmp = baos.toByteArray();
            Intent intent = new Intent(getActivity(), EditImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putByteArray("ImagePath", bytesArrayBmp);
            intent.putExtras(bundle);
            startActivityForResult(intent, NEXT_ACTIVITY_REQUEST_CODE);
        }
    }
}