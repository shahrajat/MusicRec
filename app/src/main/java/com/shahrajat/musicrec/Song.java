package com.shahrajat.musicrec;

/**
 * Created by rajatshah on 11/1/16.
 */

public class Song {
    public int id;
    public String name;
    public String author;
    public String year;
    public String time;
    public String genre;

    Song(){
        id = 0;
    }

    @Override
    public String toString() {
        return name + " " + author + " " + year + " " + time;
    }
}
