package com.example.transfertme;
public class HelperClass {

    String name, email, birth, username, password, profileImageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public HelperClass(String name, String email, String birth, String username, String password) {
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.username = username;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public HelperClass() {
        // Constructeur sans argument requis pour la désérialisation depuis Firebase Database
    }
}
