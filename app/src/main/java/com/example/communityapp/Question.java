package com.example.communityapp;

import java.util.HashMap;
import java.util.Map;

public class Question {

    String title;

    Question(){};

    Question(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String,Object> QMap(){
        HashMap<String,Object> ques = new HashMap<>();
        ques.put("title",title);
        return ques;
    }
}
