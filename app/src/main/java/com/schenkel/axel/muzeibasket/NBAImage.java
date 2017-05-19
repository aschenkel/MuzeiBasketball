package com.schenkel.axel.muzeibasket;

/**
 * Created by axel on 04/05/17.
 */

public class NBAImage {
    private String url;
    private String name;
    private String description;

    //For Firebase DataBase incomming object
    public NBAImage(){

    }

    public NBAImage(String url, String name, String description) {
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
