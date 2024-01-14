package com.cyk29.safewhere.dataclasses;

/**
 * Represents a notification's information in the system.
 */
public class NotificationItem {
    private String id;
    private String message;
    private String time;
    private String type;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    @SuppressWarnings("unused")
    public NotificationItem() { }

    /**
     * Constructs a new NotificationItem with the specified attributes.
     *
     * @param id The unique identifier of the notification.
     * @param message The message of the notification.
     * @param time The time the notification was created.
     * @param type The type of the notification.
     */
    public NotificationItem(String id, String message, String time, String type) {
        this.id = id;
        this.message = message;
        this.time = time;
        this.type = type; // Initialize the new field
    }

    /**
     * @return the id
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getId() { return id; }
    /**
     * @return the message
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getMessage() { return message; }
    /**
     * @return the time
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getTime() { return time; }
    /**
     * @return the type
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getType() { return type; } // Getter for type

    /**
     * @param id the id to set
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public void setId(String id) { this.id = id; }
    /**
     * @param message the message to set
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    @SuppressWarnings("unused")
    public void setMessage(String message) { this.message = message; }
    /**
     * @param time the time to set
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    @SuppressWarnings("unused")
    public void setTime(String time) { this.time = time; }
    /**
     * @param type the type to set
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    @SuppressWarnings("unused")
    public void setType(String type) { this.type = type; } // Setter for type
}