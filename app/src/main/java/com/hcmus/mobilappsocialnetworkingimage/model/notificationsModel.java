package com.hcmus.mobilappsocialnetworkingimage.model;

public class notificationsModel {
    String notification_id;
    String user_id;
    String post_id;
    String content;
    String date;
    Boolean seen;


    public notificationsModel(String notification_id, String user_id, String post_id, String content, String date, Boolean seen) {
        this.notification_id = notification_id;
        this.user_id = user_id;
        this.post_id = post_id;
        this.content = content;
        this.date = date;
        this.seen = seen;
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
}
