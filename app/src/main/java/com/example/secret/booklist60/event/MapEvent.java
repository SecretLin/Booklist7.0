package com.example.secret.booklist60.event;

import java.util.Map;

/**
 * Created by Secret on 2016/11/19.
 * 消息提醒事件
 */

public class MapEvent {

    private Map<String,String> map;

    public MapEvent(Map<String,String> map) {

        this.map = map;

    }

    private String s;
    public MapEvent(String s){
        this.s = s;
    }


    public Map<String, String> getMap() {
        return map;
    }

    public String getS() {
        return s;
    }
}
