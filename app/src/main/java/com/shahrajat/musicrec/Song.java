package com.shahrajat.musicrec;

/**
 * Created by rajatshah on 11/1/16.
 */

public class Song {
    public String name;
    public String author;
    public String year;
    public String time;

    @Override
    public String toString() {
        return name + " " + author + " " + year + " " + time;
    }
}
