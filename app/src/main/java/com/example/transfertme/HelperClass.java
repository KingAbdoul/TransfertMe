package com.example.transfertme;

public class HelperClass {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String profileImageUrl; // Optionnel : si tu veux stocker l'URL d'une photo de profil

    // Constructeur sans argument (nécessaire pour Firebase / Firestore)
    public HelperClass() {
        // Vide
    }

    // Constructeur principal
    public HelperClass(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        // profileImageUrl reste null par défaut
    }

    // Getters et setters
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
