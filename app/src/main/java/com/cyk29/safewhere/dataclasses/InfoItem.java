package com.cyk29.safewhere.dataclasses;

/**
 * Represents a notification's information in the system.
 */
public class InfoItem {
    private String title;
    private String description;
    private String link;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    @SuppressWarnings("unused")
    public InfoItem() {
    }

    /**
     * Constructs a new NotificationItem with the specified attributes.
     *
     * @param title The title of the notification.
     * @param description The description of the notification.
     */
    @SuppressWarnings("unused") public InfoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    /**
     * Constructs a new NotificationItem with the specified attributes.
     *
     * @param title The title of the notification.
     * @param description The description of the notification.
     * @param link The link of the notification.
     */
    @SuppressWarnings("unused") public InfoItem(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    /**
     *
     * @param title the title of the notification
     */
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @param description the description of the notification
     */
    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @param link the link of the notification
     */
    @SuppressWarnings("unused")
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the title
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the link
     * required for calls to DataSnapshot.getValue(NotificationItem.class)
     */
    public String getLink() {
        return link;
    }
}

