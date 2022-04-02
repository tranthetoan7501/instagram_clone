package com.hcmus.mobilappsocialnetworkingimage.model;

public class likeModel {
    String user_id;
    String id;

    public likeModel() {
    }

    public likeModel(String user_id, String id) {
        this.user_id = user_id;
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
