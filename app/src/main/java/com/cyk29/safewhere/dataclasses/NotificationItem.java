package com.cyk29.safewhere.dataclasses;

public class NotificationItem {
    private String id;
    private String message;
    private String time;
    private String type; // Added field

    // Empty Constructor
    @SuppressWarnings("unused")
    public NotificationItem() { }

    // Full Constructor
    public NotificationItem(String id, String message, String time, String type) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.type = type; // Initialize the new field
    }

    // Getters
    public String getId() { return id; }
    public String getMessage() { return message; }
    public String getTime() { return time; }
    public String getType() { return type; } // Getter for type

    // Setters
    public void setId(String id) { this.id = id; }
    @SuppressWarnings("unused")
    public void setMessage(String message) { this.message = message; }
    @SuppressWarnings("unused")
    public void setTime(String time) { this.time = time; }
    @SuppressWarnings("unused")
    public void setType(String type) { this.type = type; } // Setter for type
}