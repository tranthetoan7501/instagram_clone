package com.hcmus.mobilappsocialnetworkingimage.utils;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class squareImageView extends AppCompatImageView {
    public squareImageView(Context context) {
        super(context);
    }

    public squareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public squareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}