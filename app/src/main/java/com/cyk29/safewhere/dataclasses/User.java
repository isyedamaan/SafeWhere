package com.cyk29.safewhere.dataclasses;

/**
 * Represents a user's data in the system.
 */
public class User {
    private String uid;
    private String email;
    private String name;
    private String phone;
    private String ecName;
    private String ecPhone;
    private String ecEmail;


    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings(value = "unused")
    public User(){
    }

    /**
     * Constructs a new User with the specified attributes.
     *
     * @param uid The unique identifier of the user.
     * @param name The name of the user.
     * @param email The email of the user.
     * @param phone The phone number of the user.
     * @param ecName The emergency contact name of the user.
     * @param ecPhone The emergency contact phone number of the user.
     * @param ecEmail The emergency contact email of the user.
     */
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
    /**
     * @param uid the uid to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings("unused")
    public void setUid(String uid) {
            this.uid = uid;
    }

    /**
     * @return the email
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public String getEmail() {
            return email;
    }
    /**
     * @param email the email to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public void setEmail(String email) {
            this.email = email;
    }

    /**
     * @return the name
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public String getName() {
            return name;
    }
    /**
     * @param name the name to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public void setName(String name) {
            this.name = name;
    }

    /**
     * @return the phone
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public String getPhone() {
            return phone;
    }
    /**
     * @param phone the phone to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings("unused")
    public void setPhone(String phone) {
            this.phone = phone;
    }

    /**
     * @return the ecName
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public String getEcName() {
            return ecName;
    }
    /**
     * @param ecName the ecName to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings("unused")
    public void setEcName(String ecName) {
            this.ecName = ecName;
    }

    /**
     * @return the ecPhone
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public String getEcPhone() {
        return ecPhone;
    }
    /**
     * @param ecPhone the ecPhone to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings("unused")
    public void setEcPhone(String ecPhone) {
        this.ecPhone = ecPhone;
    }

    /**
     * @return the ecEmail
     * required for calls to DataSnapshot.getValue(User.class)
     */
    public String getEcEmail() {
        return ecEmail;
    }

    /**
     * @param ecEmail the ecEmail to set
     * required for calls to DataSnapshot.getValue(User.class)
     */
    @SuppressWarnings("unused")
    public void setEcEmail(String ecEmail) {
        this.ecEmail = ecEmail;
    }
}
