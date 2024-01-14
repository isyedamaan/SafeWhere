package com.cyk29.safewhere.dataclasses;

/**
 * Represents a report's information in the system.
 */
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


    /**
     * Default constructor required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public Report(){
    }

    /**
     * Constructs a new Report with the specified attributes.
     *
     * @param id The unique identifier of the report.
     * @param uid The unique identifier of the user who created the report.
     * @param userName The name of the user who created the report.
     * @param type The type of the report.
     * @param userDescription The description of the report.
     * @param date The date the report was created.
     * @param latitude The latitude of the report.
     * @param longitude The longitude of the report.
     */
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

    /**
     * @return the id
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public String getId() { return id; }
    /**
     * @param id the id to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public void setId(String id) { this.id = id; }

    /**
     * @return the uid
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public String getUid() { return uid; }
    /**
     * @param uid the uid to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setUid(String uid) { this.uid = uid; }

    /**
     * @return the userName
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public String getUserName() { return userName; }
    /**
     * @param userName the userName to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setUserName(String userName) { this.userName = userName; }

    /**
     * @return the type
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public String getType() { return type; }
    /**
     * @param type the type to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setType(String type) { this.type = type; }

    /**
     * @return the userDescription
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public String getUserDescription() { return userDescription; }
    /**
     * @param userDescription the userDescription to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setUserDescription(String userDescription) { this.userDescription = userDescription; }

    /**
     * @return the date
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public String getDate() { return date; }
    /**
     * @param date the date to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setDate(String date) { this.date = date; }

    /**
     * @return the latitude
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public String getLatitude() { return latitude; }
    /**
     * @param latitude the latitude to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setLatitude(String latitude) { this.latitude = latitude; }

    /**
     * @return the longitude
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public String getLongitude() { return longitude; }
    /**
     * @param longitude the longitude to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setLongitude(String longitude) { this.longitude = longitude; }

    /**
     * @return the upVotes
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public int getUpVotes() { return upVotes; }
    /**
     * @param upVotes the upVotes to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setUpVotes(int upVotes) { this.upVotes = upVotes; }

    /**
     * @return the downVotes
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    public int getDownVotes() { return downVotes; }
    /**
     * @param downVotes the downVotes to set
     * required for calls to DataSnapshot.getValue(Report.class)
     */
    @SuppressWarnings("unused")
    public void setDownVotes(int downVotes) { this.downVotes = downVotes; }

}
