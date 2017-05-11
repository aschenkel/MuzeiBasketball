package com.example.axel.nbamuzei;

/**
 * Created by axel on 04/05/17.
 */

public class NBAImage {
    public String url;
    String name;
    String description;

    //For DataBase incomming object
    public NBAImage(){

    }

    public NBAImage(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }
}
