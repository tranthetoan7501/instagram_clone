package com.hcmus.mobilappsocialnetworkingimage.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.adapter.gridImageAdapter;
import com.hcmus.mobilappsocialnetworkingimage.nextActivity;
import com.hcmus.mobilappsocialnetworkingimage.utils.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import java.util.ArrayList;

public class galleryFragment extends Fragment {

    // Constants
    private static final int NUM_GRID_COL = 3;

    // Widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    // vars
    private ArrayList<String> directories;
    private final String mAppend = "file:/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        }

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        galleryImage = (ImageView) view.findViewById(R.id.galleryImageView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        directorySpinner = (Spinner) view.findViewById(R.id.spinnerDirectory);
        mProgressBar.setVisibility(View.GONE);

        ImageView shareClose = (ImageView) view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(view1 -> getActivity().finish());

        TextView nextScreen = (TextView) view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(view12 -> {
            Intent intent = new Intent(getActivity(), nextActivity.class);
//            intent.putExtra(getString(R.string.selected_image), galleryImage.getTag().toString());
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

        directories.add(mFilePaths.CAMERA);
        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            // sub string /Pictures/Zalo
            String dir1 = directories.get(i).split("/")[directories.get(i).split("/").length - 1];
            String dir2 = directories.get(i).split("/")[directories.get(i).split("/").length - 2];
            directoryNames.add(dir2 + "/" + dir1);
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
        final ArrayList<String> imgURLs = fileSearch.getFilePaths(selectedDirectory);

        // set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COL;
        gridView.setColumnWidth(imageWidth);

        gridImageAdapter adapter = new gridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        // setup grid adapter
        if (imgURLs.size() > 0)
            setImage(imgURLs.get(0), galleryImage, mAppend);

        gridView.setOnItemClickListener((adapterView, view, i, l) -> setImage(imgURLs.get(i), galleryImage, mAppend));

    }

    private void setImage(String imgURL, ImageView image, String append) {
        ImageLoader imageLoader = ImageLoader.getInstance();


        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
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