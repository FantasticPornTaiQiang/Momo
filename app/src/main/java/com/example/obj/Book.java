package com.example.obj;


import java.util.List;

public class Book {

    private String name;
    public List<String> sectionList;
    private int imageId;
    private String bookUrl;

    public Book(String name, int imageId, String bookUrl){
        this.name=name;
        this.imageId=imageId;
        this.bookUrl=bookUrl;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getBookUrl() {
        return bookUrl;
    }
}
