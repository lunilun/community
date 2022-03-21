package com.example.communityapp;

import java.util.HashMap;
import java.util.Map;

public class Article {

    String title;
    String con;

    Article(){};

    Article(String title, String con){
        this.title = title;
        this.con = con;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con;
    }

    public Map<String,Object> Map(){
        HashMap<String,Object> art = new HashMap<>();
        art.put("title",title);
        art.put("con",con);
        return art;
    }
}
