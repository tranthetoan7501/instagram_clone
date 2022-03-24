package com.hcmus.mobilappsocialnetworkingimage.model;

import java.util.List;

public class commentsModel {
    String comment;
    List<String> likes;
    String date_created;
    String user_id;

    public commentsModel() {
    }

    public commentsModel(String comment, List<String> likes, String date_created, String user_id) {
        this.comment = comment;
        this.likes = likes;
        this.date_created = date_created;
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
