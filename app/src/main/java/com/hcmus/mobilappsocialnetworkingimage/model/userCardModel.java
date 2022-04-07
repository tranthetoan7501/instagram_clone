package com.hcmus.mobilappsocialnetworkingimage.model;

public class userCardModel {
    private String user_id;
    private String username;
    private String avatar;

    public userCardModel() {

    }

    public userCardModel(String user_id, String username, String avatar) {
        this.user_id = user_id;
        this.username = username;
        this.avatar = avatar;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public String toString() {
        return "userCardModel{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
