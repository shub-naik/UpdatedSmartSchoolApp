package com.pragyatitsolutions.SchoolManagement;

import java.io.Serializable;

public class Teacher implements Serializable {

    String name, email, password, phone;
    String imageuri;

    public Teacher() {
    }

    public Teacher(String name, String email, String password, String phone, String imageuri) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.imageuri = imageuri;
    }

    public String getImageuri() {
        return imageuri;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }
}
