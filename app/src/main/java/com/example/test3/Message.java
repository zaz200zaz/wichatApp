package com.example.test3;

public class Message {
    private String send;//người gửi
    private String receiver;//người nhận
    private String conten;//nội dung
    private String imageView;
    private String Time;
    private String Year;

    public Message() {
    }

    public Message(String send, String receiver, String conten, String imageView, String time, String year) {
        this.send = send;
        this.receiver = receiver;
        this.conten = conten;
        this.imageView = imageView;
        Time = time;
        Year = year;
    }

    public String getImageView() {
        return imageView;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getSend() {
        return send;
    }
    public void setSend(String send) {
        this.send = send;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getConten() {
        return conten;
    }

    public void setConten(String conten) {
        this.conten = conten;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
