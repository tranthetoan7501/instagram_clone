package com.hcmus.mobilappsocialnetworkingimage.model;

import java.util.List;

public class postsModel {
    String title;
    List<String> likes;
    List<String> comments;
    String date;
    List<String> images;

    public postsModel(String title, List<String> likes, List<String> comments, String date, List<String> images) {
        this.title = title;
        this.likes = likes;
        this.comments = comments;
        this.date = date;
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
