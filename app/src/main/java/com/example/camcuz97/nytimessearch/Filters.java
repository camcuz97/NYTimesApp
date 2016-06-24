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
    String sort;
    String begin;

    public String getSort() {
        return sort;
    }

    public String getBegin() {
        return begin;
    }

    public Filters(){
        arts = false;
        style = false;
        sports = false;
        sort = "Newest";
        begin = "";
    }

    public Filters(boolean arts, boolean style, boolean sports, String sort, String begin) {
        this.arts = arts;
        this.style = style;
        this.sports = sports;
        this.sort = sort;
        this.begin = begin;
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
