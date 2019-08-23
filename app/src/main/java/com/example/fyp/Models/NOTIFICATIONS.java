package com.example.fyp.Models;

public class NOTIFICATIONS {
    private String title,image,id;
    private String time;

    public NOTIFICATIONS() {
    }

    public NOTIFICATIONS(String id, String title, String time, String image) {
        this.id=id;
        this.title=title;
        this.image=image;
        this.time=time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
