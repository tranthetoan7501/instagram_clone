package com.hcmus.mobilappsocialnetworkingimage.model;

import java.util.List;

public class thumbnailsModel {
    List<String> image_paths;
    String user_id;
    String post_id;

    public thumbnailsModel(List<String> image_paths, String user_id, String post_id) {
        this.image_paths = image_paths;
        this.user_id = user_id;
        this.post_id = post_id;
    }

    public List<String> getImage_paths() {
        return image_paths;
    }

    public void setImage_paths(List<String> image_paths) {
        this.image_paths = image_paths;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
