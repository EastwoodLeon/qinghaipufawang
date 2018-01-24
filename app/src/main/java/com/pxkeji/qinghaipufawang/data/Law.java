package com.pxkeji.qinghaipufawang.data;

/**
 * Created by Administrator on 2018/1/15.
 */

public class Law {

    private int id;
    private String title;
    private String date;

    public Law(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }
}
