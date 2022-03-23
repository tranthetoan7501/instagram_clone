package com.hcmus.mobilappsocialnetworkingimage.model;

public class commentsModel {
    String comment;
    String count_likes;
    String date_created;
    String username;
    String avatar;
    String user_id;

    public commentsModel(String comment, String count_likes, String date_created, String username, String avatar, String user_id) {
        this.comment = comment;
        this.count_likes = count_likes;
        this.date_created = date_created;
        this.username = username;
        this.avatar = avatar;
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCount_likes() {
        return count_likes;
    }

    public void setCount_likes(String count_likes) {
        this.count_likes = count_likes;
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
