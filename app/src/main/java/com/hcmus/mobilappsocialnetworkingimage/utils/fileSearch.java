package com.hcmus.mobilappsocialnetworkingimage.utils;

import java.io.File;
import java.util.ArrayList;

public class fileSearch {

    public static ArrayList<String> getDirectoryPaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].isDirectory() && !listFiles[i].isHidden()) {
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }

        return pathArray;
    }

    public static ArrayList<String> getFilePaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File listFile : listFiles) {
                if (listFile.isFile()) {
                    pathArray.add(listFile.getAbsolutePath());
                }
            }
        }

        return pathArray;
    }
}
