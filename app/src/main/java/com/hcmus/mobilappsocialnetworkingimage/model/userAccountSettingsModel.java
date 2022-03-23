package com.hcmus.mobilappsocialnetworkingimage.model;

import java.io.Serializable;

public class userAccountSettingsModel implements Serializable {

    private String description;
    private String display_name;
    private long followers;
    private long followings;
    private long posts;
    private String profile_photo;
    private String username;
    private String website;

    public userAccountSettingsModel() {
    }

    public userAccountSettingsModel(String description, String display_name, long followers, long followings, long posts, String profile_photo, String username, String website) {
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.followings = followings;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.username = username;
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return followings;
    }

    public void setFollowing(long followings) {
        this.followings = followings;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
