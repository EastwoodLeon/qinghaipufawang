package com.pxkeji.qinghaipufawang.data;

/**
 * Created by Administrator on 2018/1/23.
 */

public class Message {

    private int type;
    private double amount;
    private String source;
    private String date;

    public Message(int type, double amount, String source, String date) {
        this.type = type;
        this.amount = amount;
        this.source = source;
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getSource() {
        return source;
    }

    public String getDate() {
        return date;
    }

    public static class Builder {
        private int type;
        private double amount;
        private String source;
        private String date;

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Message build() {
            return new Message(type, amount, source, date);
        }
    }
}
