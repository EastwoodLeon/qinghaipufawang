package com.pxkeji.qinghaipufawang.data;

/**
 * Created by Administrator on 2018/1/12.
 */

public class ServiceItem {

    private int id;
    private String name;
    private int category;

    public ServiceItem(int id, String name, int category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategory() {
        return category;
    }
}
