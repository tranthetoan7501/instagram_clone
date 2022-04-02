package com.hcmus.mobilappsocialnetworkingimage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class postModel {
    String caption;
    ArrayList<likeModel> likes;
    Map<String,Object> comments;
    String date_created;
    List<String> image_paths;
    String user_id;
    String tags;
    String post_id;

    public postModel(){

    }

    public postModel(String caption, ArrayList<likeModel> likes, String date_created, List<String> image_paths, String user_id, String post_id) {
        this.likes = likes;
        this.caption = caption;
        this.date_created = date_created;
        this.image_paths = image_paths;
        this.user_id = user_id;
        this.post_id = post_id;
    }

    public postModel(String caption, String date_created, List<String> image_paths, String tags, String post_id, String user_id) {
        this.caption = caption;
        this.date_created = date_created;
        this.user_id = user_id;
        this.tags = tags;
        this.post_id = post_id;
        this.image_paths = image_paths;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ArrayList<likeModel> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<likeModel> likes) {
        this.likes = likes;
    }

    public Map<String, Object> getComments() {
        return comments;
    }

    public void setComments(Map<String, Object> comments) {
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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }
}
