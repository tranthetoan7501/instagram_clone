package com.hcmus.mobilappsocialnetworkingimage.model;

import java.util.ArrayList;

public class photoModel {
    private String caption;
    private ArrayList<commentsModel> comments;
    private String date_created;
    private ArrayList<String> likes;
    private String photo_id;
    private String tags;
    private String user_id;

    public photoModel(String caption, String date_created, String tags, String user_id) {
        this.caption = caption;
        this.comments = comments;
        this.date_created = date_created;
        this.likes = likes;
        this.tags = tags;
        this.user_id = user_id;
    }

    public photoModel() {

    }
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ArrayList<commentsModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<commentsModel> comments) {
        this.comments = comments;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getPost_id() {
        return photo_id;
    }

    public void setPost_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
