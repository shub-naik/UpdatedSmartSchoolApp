package com.example.SchoolManagement;

public class EmergencyLeave {

    String ImageUrl, phone, password, email, token;

    public EmergencyLeave(String ImageUrl, String phone, String password, String email, String token) {
        this.ImageUrl = ImageUrl;
        this.phone = phone;
        this.password = password;
        this.email = email;
        this.token = token;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public EmergencyLeave() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
