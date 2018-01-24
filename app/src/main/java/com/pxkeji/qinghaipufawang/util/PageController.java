package com.pxkeji.qinghaipufawang.util;

/**
 * Created by Administrator on 2018/1/11.
 */

public class PageController {

    private int currentPage = 1;
    private int totalPages = 1;


    public int getCurrentPage() {
        return currentPage;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void firstPage() {
        currentPage = 1;
        totalPages = 1;
    }

    public boolean hasNextPage() {
        if (currentPage < totalPages) {
            return true;
        } else {
            return false;
        }

    }

    /**
     *
     * @return true: 下一页有内容
     * false: 已到达尾页
     */
    public void nextPage() {
        currentPage++;
    }
}
