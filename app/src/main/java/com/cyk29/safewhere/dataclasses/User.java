package com.cyk29.safewhere.dataclasses;

public class User {
    private String uid;
    private String email;
    private String name;
    private String phone;
    private String ecName;
    private String ecPhone;
    private String ecEmail;
    private String geofencing_contact_phone;
    private String profile_picture_url;

    public User(){
    }

    public User(String uid, String name,String email, String phone, String ecName, String ecPhone, String ecEmail) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.ecName = ecName;
        this.ecPhone = ecPhone;
        this.ecEmail = ecEmail;
    }

    // write all getter and setters
    public String getUid() {
            return uid;
    }
    public void setUid(String uid) {
            this.uid = uid;
    }

    public String getEmail() {
            return email;
    }
    public void setEmail(String email) {
            this.email = email;
    }

    public String getName() {
            return name;
    }
    public void setName(String name) {
            this.name = name;
    }

    public String getPhone() {
            return phone;
    }
    public void setPhone(String phone) {
            this.phone = phone;
    }

    public String getEcName() {
            return ecName;
    }
    public void setEcName(String ecName) {
            this.ecName = ecName;
    }

    public String getEcPhone() {
        return ecPhone;
    }
    public void setEcPhone(String ecPhone) {
        this.ecPhone = ecPhone;
    }

    public String getEcEmail() {
        return ecEmail;
    }
    public void setEcEmail(String ecEmail) {
        this.ecEmail = ecEmail;
    }

    public String getGeofencing_contact_phone() {
            return geofencing_contact_phone;
    }
    public void setGeofencing_contact_phone(String geofencing_contact_phone) {
            this.geofencing_contact_phone = geofencing_contact_phone;
    }

    public String getProfile_picture_url() {
            return profile_picture_url;
    }
    public void setProfile_picture_url(String profile_picture_url) {
            this.profile_picture_url = profile_picture_url;
    }

    @Override
    public String toString(){
        return name +" "+ phone +" "+ ecName +" "+ ecPhone;
    }
}
