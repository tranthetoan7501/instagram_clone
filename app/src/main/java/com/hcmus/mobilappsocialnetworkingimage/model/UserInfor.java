package com.hcmus.mobilappsocialnetworkingimage.model;

import java.io.Serializable;
import java.util.List;

public class UserInfor implements Serializable {
    private String username;
    private String email;
    private String about;
    private String avatar;
    private List<String> follower;
    private List<String> following;

    public UserInfor(String username, String email, String about, String avatar) {
        this.username = username;
        this.email = email;
        this.about = about;
        this.avatar = avatar;
    }

    public UserInfor(String username, String email, String about, String avatar, List<String> follower, List<String> following) {
        this.username = username;
        this.email = email;
        this.about = about;
        this.avatar = avatar;
        this.follower = follower;
        this.following = following;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getFollower() {
        return follower;
    }

    public void setFollower(List<String> follower) {
        this.follower = follower;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }
}
