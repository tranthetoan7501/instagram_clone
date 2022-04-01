package com.hcmus.mobilappsocialnetworkingimage.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class notificationsModel {
    String notification_id;
    String user_id;
    String post_id;
    String content;
    String date;
    Boolean seen;
    ArrayList<String> image_paths;

    public notificationsModel() {
    }


    public notificationsModel(String notification_id, String user_id, String post_id, String content, String date, Boolean seen, ArrayList<String> image_paths) {
        this.notification_id = notification_id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.content = content;
        this.date = date;
        this.seen = seen;
        this.image_paths = image_paths;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public ArrayList<String> getImage_paths() {
        return image_paths;
    }

    public void setImage_paths(ArrayList<String> image_paths) {
        this.image_paths = image_paths;
    }
}
