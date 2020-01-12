package com.pragyatitsolutions.SchoolManagement;

public class EmergencyLeave {

    String ImageUrl, phone, name, remarks, token;

    public EmergencyLeave(String ImageUrl, String phone, String name, String remarks, String token) {
        this.ImageUrl = ImageUrl;
        this.phone = phone;
        this.name = name;
        this.remarks = remarks;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
