package com.pxkeji.qinghaipufawang.data;

/**
 * Created by Administrator on 2018/1/11.
 */

public class News {

    private int id;
    private String picUrl;
    private String title;
    private String source;
    private int hits;

    private News(int id, String picUrl, String title, String source, int hits) {
        this.id = id;
        this.picUrl = picUrl;
        this.title = title;
        this.source = source;
        this.hits = hits;
    }

    public int getId() {
        return id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSource() {
        return source;
    }

    public int getHits() {
        return hits;
    }

    public static class Builder {
        private int mId;
        private String mPicUrl;
        private String mTitle;
        private String mSource;
        private int mHits;

        public Builder id(int id) {
            this.mId = id;
            return this;
        }

        public Builder picUrl(String picUrl) {
            this.mPicUrl = picUrl;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder source(String source) {
            this.mSource = source;
            return this;
        }

        public Builder hits(int hits) {
            this.mHits = hits;
            return this;
        }

        public News build() {

            return new News(mId, mPicUrl, mTitle, mSource, mHits);
        }
    }
}
