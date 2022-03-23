package com.hcmus.mobilappsocialnetworkingimage.model;

import java.util.ArrayList;
import java.util.List;

public class postsModel {
    String caption;
    List<String> likes;
    List<String> comments;
    String date_created;
    List<String> image_paths;
    String user_id;
    String tags;

    public postsModel(String caption, List<String> likes, ArrayList<String> comments, String date_created, List<String> image_paths, String user_id, String tags) {
        this.caption = caption;
        this.likes = likes;
        this.comments = comments;
        this.date_created = date_created;
        this.image_paths = image_paths;
        this.user_id = user_id;
        this.tags = tags;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
