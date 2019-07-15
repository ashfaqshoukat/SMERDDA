package com.example.fyp;

public class COMMENT
{
    private String message,comapnayName,comapanyId;
    private int rating;
    private  long date;

    public COMMENT() {
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComapnayName() {
        return comapnayName;
    }

    public void setComapnayName(String comapnayName) {
        this.comapnayName = comapnayName;
    }

    public String getComapanyId() {
        return comapanyId;
    }

    public void setComapanyId(String comapanyId) {
        this.comapanyId = comapanyId;
    }
}
