package com.example.communityapp;

import java.util.HashMap;
import java.util.Map;

public class listitem {

    String title;
    String user;
    String day;
    String color;
    String cont;

    listitem(){}

    listitem(String title, String user, String day, String color, String cont){
        this.title=title;
        this.user=user;
        this.day=day;
        this.color=color;
        this.cont=cont;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public Map<String,Object> LMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("title",title);
        map.put("user",user);
        map.put("day",day);
        map.put("color",color);
        map.put("cont",cont);
        return map;
    }
}
