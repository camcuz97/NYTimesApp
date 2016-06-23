package com.example.camcuz97.nytimessearch;

import org.parceler.Parcel;

/**
 * Created by camcuz97 on 6/22/16.
 */
@Parcel
public class Filters{
    boolean arts;
    boolean style;
    boolean sports;


    public Filters(){
        arts = false;
        style = false;
        sports = false;
    }

    public Filters(boolean arts, boolean style, boolean sports) {
        this.arts = arts;
        this.style = style;
        this.sports = sports;
    }

    public boolean isArts() {
        return arts;
    }

    public boolean isStyle() {
        return style;
    }

    public boolean isSports() {
        return sports;
    }
}
