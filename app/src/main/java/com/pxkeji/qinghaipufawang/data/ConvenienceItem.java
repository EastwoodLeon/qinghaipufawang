package com.pxkeji.qinghaipufawang.data;

/**
 * Created by Administrator on 2018/1/18.
 */

public class ConvenienceItem {

    private String title;
    private String url;
    private int img;

    public ConvenienceItem(String title, String url, int img) {
        this.title = title;
        this.url = url;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
