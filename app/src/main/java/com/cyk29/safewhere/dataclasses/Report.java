package com.cyk29.safewhere.dataclasses;


public class Report {
    private String id;
    private String uid;
    private String userName;
    private String type;
    private String userDescription;
    private String date;
    private String latitude;
    private String longitude;
    private int upVotes;
    private int downVotes;

    public Report(){
    }
    public Report(String id, String uid, String userName, String type, String userDescription, String date, String latitude, String longitude) {
        this.id = id;
        this.uid = uid;
        this.userName = userName;
        this.type = type;
        this.userDescription = userDescription;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.upVotes = 0;
        this.downVotes = 0;
    }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUserDescription() { return userDescription; }
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
    public int getUpVotes() { return upVotes; }
    public void setUpVotes(int upVotes) { this.upVotes = upVotes; }
    public int getDownVotes() { return downVotes; }
    public void setDownVotes(int downVotes) { this.downVotes = downVotes; }

}
