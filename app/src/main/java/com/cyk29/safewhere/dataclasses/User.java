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


    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings(value = "unused")
    public User(){
    }

    public User(String uid, String name,String email, String phone, String ecName, String ecPhone, String ecEmail) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.ecName = ecName;
        this.ecPhone = ecPhone;
        this.ecEmail = ecEmail;
    }

    /**
     * @return the uid
     * required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings("unused")
    public String getUid() {
            return uid;
    }
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public void setPhone(String phone) {
            this.phone = phone;
    }

    public String getEcName() {
            return ecName;
    }
    @SuppressWarnings("unused")
    public void setEcName(String ecName) {
            this.ecName = ecName;
    }

    public String getEcPhone() {
        return ecPhone;
    }
    @SuppressWarnings("unused")
    public void setEcPhone(String ecPhone) {
        this.ecPhone = ecPhone;
    }

    public String getEcEmail() {
        return ecEmail;
    }
    @SuppressWarnings("unused")
    public void setEcEmail(String ecEmail) {
        this.ecEmail = ecEmail;
    }

    @SuppressWarnings("unused")
    public String getGeofencing_contact_phone() {
            return geofencing_contact_phone;
    }
    @SuppressWarnings("unused")
    public void setGeofencing_contact_phone(String geofencing_contact_phone) {
            this.geofencing_contact_phone = geofencing_contact_phone;
    }

    @SuppressWarnings("unused")
    public String getProfile_picture_url() {
            return profile_picture_url;
    }

    @SuppressWarnings("unused")
    public void setProfile_picture_url(String profile_picture_url) {
            this.profile_picture_url = profile_picture_url;
    }
}
