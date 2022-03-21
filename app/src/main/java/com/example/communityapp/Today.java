package com.example.communityapp;

import java.util.HashMap;
import java.util.Map;

public class Today {

    String title;
    String name;
    String time;

    Today(){};

    Today(String title,String name, String time){
        this.time=time;
        this.title=title;
        this.name=name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String,Object> TMap(){
        HashMap<String,Object> every = new HashMap<>();
        every.put("name",name);
        every.put("title",title);
        every.put("time",time);
        return every;
    }
}
