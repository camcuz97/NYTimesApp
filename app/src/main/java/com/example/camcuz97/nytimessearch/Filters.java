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
    String unchangedDate;

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
        unchangedDate = "";
        begin = "";
    }

    public Filters(boolean arts, boolean style, boolean sports, String sort, String begin) {
        this.arts = arts;
        this.style = style;
        this.sports = sports;
        this.sort = sort;
        this.begin = begin;
    }

    public void setArts(boolean arts) {
        this.arts = arts;
    }

    public void setStyle(boolean style) {
        this.style = style;
    }

    public void setSports(boolean sports) {
        this.sports = sports;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setUnchangedDate(String unchangedDate) {
        this.unchangedDate = unchangedDate;
    }

    public String manipulateDate(){
        begin = unchangedDate.substring(0);
        if(!begin.equals("")){
            if(begin.charAt(6) == '-'){
                begin = begin.substring(0,5) + "0" + begin.substring(5);
            }
            if(begin.length() != 10){
                begin = begin.substring(0,8) + "0" + begin.substring(8);
            }
            begin = begin.replace("-","");
        }
        return begin;
    }

    public String getUnchangedDate() {
        return unchangedDate;
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
