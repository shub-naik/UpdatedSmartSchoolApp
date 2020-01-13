package com.pragyatitsolutions.SchoolManagement;

public class EmergencyLeave {

    String Status, ImageUrl, phone, name, remarks, parentsphone;

    public EmergencyLeave(String Status, String ImageUrl, String phone, String name, String remarks, String parentsphone) {
        this.ImageUrl = ImageUrl;
        this.phone = phone;
        this.name = name;
        this.remarks = remarks;
        this.parentsphone = parentsphone;
        this.Status = Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getParentsphone() {
        return parentsphone;
    }

    public void setParentsphone(String parentsphone) {
        this.parentsphone = parentsphone;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public EmergencyLeave() {
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
