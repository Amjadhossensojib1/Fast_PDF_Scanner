package com.amjad.fastpdfscanner.model;

public class Pdflist {

    private String imageUrl;
    private String title;
    private String size;
    private String path;
    public Pdflist(String imageUrl, String title, String size, String path) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.size = size;
        this.path = path;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }
}