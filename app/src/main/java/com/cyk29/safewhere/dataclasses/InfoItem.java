package com.cyk29.safewhere.dataclasses;

public class InfoItem {
    private String title;
    private String description;
    private String link;

    @SuppressWarnings("unused")
    public InfoItem() {
    }

    @SuppressWarnings("unused") public InfoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }
    @SuppressWarnings("unused") public InfoItem(String title, String description, String link) {
        this.title = title;
        this.description = description;
        this.link = link;
    }

    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }

    @SuppressWarnings("unused")
    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}

