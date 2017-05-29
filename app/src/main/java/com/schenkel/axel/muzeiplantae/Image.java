package com.schenkel.axel.muzeiplantae;

/**
 * Created by axel on 04/05/17.
 */

public class Image {
    private String url;
    private String name;
    private String description;

    //For Firebase DataBase incomming object
    public Image(){

    }

    public Image(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }

    public String getUrl() {
        return url;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }
}
