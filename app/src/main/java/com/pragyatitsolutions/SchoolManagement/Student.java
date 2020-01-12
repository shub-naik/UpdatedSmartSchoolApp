package com.pragyatitsolutions.SchoolManagement;

public class Student {

    String token, imageuri, sname, sphone, sclass, ssection, sroll, saddress, smothername, email, password;

    public Student() {
    }

    public Student(String token, String imageuri, String sname, String sphone, String sclass, String ssection, String sroll, String email, String saddress, String smothername, String password) {
        this.password = password;
        this.token = token;
        this.imageuri = imageuri;
        this.sname = sname;
        this.sphone = sphone;
        this.sclass = sclass;
        this.ssection = ssection;
        this.email = email;
        this.sroll = sroll;
        this.saddress = saddress;
        this.smothername = smothername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSphone() {
        return sphone;
    }

    public void setSphone(String sphone) {
        this.sphone = sphone;
    }

    public String getSclass() {
        return sclass;
    }

    public void setSclass(String sclass) {
        this.sclass = sclass;
    }

    public String getSsection() {
        return ssection;
    }

    public void setSsection(String ssection) {
        this.ssection = ssection;
    }

    public String getSroll() {
        return sroll;
    }

    public void setSroll(String sroll) {
        this.sroll = sroll;
    }

    public String getSaddress() {
        return saddress;
    }

    public void setSaddress(String saddress) {
        this.saddress = saddress;
    }

    public String getSmothername() {
        return smothername;
    }

    public void setSmothername(String smothername) {
        this.smothername = smothername;
    }
}
