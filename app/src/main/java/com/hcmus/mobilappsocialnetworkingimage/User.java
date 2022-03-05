package com.hcmus.mobilappsocialnetworkingimage;

import java.io.Serializable;

class User implements Serializable {
    private String username;
    private String email;
    private String about;

    public User(String username, String email, String about) {
        this.username = username;
        this.email = email;
        this.about=about;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
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
}
