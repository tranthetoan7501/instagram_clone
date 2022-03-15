package com.hcmus.mobilappsocialnetworkingimage;

import java.io.Serializable;

public class UserInfor implements Serializable {
    private String username;
    private String email;
    private String about;
    private String avatar;

    public UserInfor(String username, String email, String about, String avatar) {
        this.username = username;
        this.email = email;
        this.about=about;
        this.avatar=avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAbout() {
        return about;
    }

    public String getAvatar() { return avatar; }
}
