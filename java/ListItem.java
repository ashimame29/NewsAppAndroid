package com.example.newsapp_1;

public class ListItem {
    private String head;
    private String date;
    private String imageUrl;

    public ListItem(String head, String date, String imageUrl) {
        this.head = head;
        this.date = date;
        this.imageUrl = imageUrl;

    }

    public String getDate() {
        return date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHead() {
        return head;
    }
}
