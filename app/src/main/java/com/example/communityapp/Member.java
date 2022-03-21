package com.example.communityapp;

import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class Member {

    public String mail;
    public String name;
    public String grade;
    public String number;

    Member(){}

    Member(String name, String number, String grade, String mail){
        this.name=name;
        this.grade=grade;
        this.number=number;
        this.mail=mail;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    /*public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }*/

    public Map<String,Object> toMap(){
        HashMap<String,Object> res = new HashMap<>();
        //res.put("url",url);
        res.put("name",name);
        res.put("grade",grade);
        res.put("number",number);
        res.put("mail",mail);
        return res;
    }
}
