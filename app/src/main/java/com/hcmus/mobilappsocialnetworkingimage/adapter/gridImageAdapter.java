package com.hcmus.mobilappsocialnetworkingimage.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.hcmus.mobilappsocialnetworkingimage.R;
import com.hcmus.mobilappsocialnetworkingimage.utils.squareImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class gridImageAdapter extends ArrayAdapter {

    private Activity mContext;
    private int layoutResourceId;
    private ArrayList<String> imgURLs;
    public List<Integer> selectedPositions = new ArrayList<>();


    public gridImageAdapter(Activity mContext, int layoutResourceId, ArrayList<String> imgURLs) {
        super(mContext, layoutResourceId, imgURLs);
        this.mContext = mContext;
        this.imgURLs = imgURLs;
        this.layoutResourceId = layoutResourceId;
    }

    private static class ViewHolder{
        squareImageView image;
        ProgressBar mProgressBar;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layoutResourceId, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (squareImageView) convertView.findViewById(R.id.gridImageView);
            viewHolder.mProgressBar = (ProgressBar) convertView.findViewById(R.id.gridImageProgressbar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String imgURL = (String) getItem(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage("file://" + imgURL, viewHolder.image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (viewHolder.mProgressBar != null) {
                    viewHolder.mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (viewHolder.mProgressBar != null) {
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (viewHolder.mProgressBar != null) {
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (viewHolder.mProgressBar != null) {
                    viewHolder.mProgressBar.setVisibility(View.GONE);
                }
            }
        });
        return convertView;
    }
}