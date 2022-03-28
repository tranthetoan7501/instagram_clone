package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.activity.nextActivity;
import com.hcmus.mobilappsocialnetworkingimage.adapter.gridImageAdapter;
import com.hcmus.mobilappsocialnetworkingimage.utils.filePaths;
import com.hcmus.mobilappsocialnetworkingimage.utils.fileSearch;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class galleryFragment extends Fragment {

    private static final int NUM_GRID_COL = 3;

    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    private ArrayList<String> directories;
    private ArrayList<String> mSelectedImage = new ArrayList<>();

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
            Intent intent = new Intent(getActivity(), nextActivity.class);
            intent.putExtra(getString(R.string.selected_image), mSelectedImage);
            startActivity(intent);
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
            Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();

            int selectedIndex = adapter.selectedPositions.indexOf(position);
            if (selectedIndex > -1) {
                View tv = gridView.getChildAt(position);
                tv.setBackgroundColor(Color.RED);
                adapter.selectedPositions.remove(selectedIndex);
            } else {
                View tv = gridView.getChildAt(position);
                tv.setBackgroundColor(Color.BLUE);
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
}