package com.hcmus.mobilappsocialnetworkingimage.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class filePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

    public static Bitmap getBitmap(String s) {
        return BitmapFactory.decodeFile(s);
    }
}
