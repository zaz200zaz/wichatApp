package com.example.test3;

import java.io.Serializable;

public class User implements Serializable {
    private String usernameAcount;
    private String email;
    private String password;
    private int verificationCode;
    private String profilePicture;
    private String userName;
    private String nickName;

    public User() {
    }


    public User(String usernameAcount, String email, String password, int verificationCode, String profilePicture, String userName, String nickName) {
        this.usernameAcount = usernameAcount;
        this.email = email;
        this.password = password;
        this.verificationCode = verificationCode;
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //    public User(String usernameAcount, String email, String password, int verificationCode, String profilePicture, String endTime, String endMessage) {
//        this.usernameAcount = usernameAcount;
//        this.email = email;
//        this.password = password;
//        this.verificationCode = verificationCode;
//        this.profilePicture = profilePicture;
//        this.endTime = endTime;
//        this.endMessage = endMessage;
//    }

    public String getUsernameAcount() {
        return usernameAcount;
    }

    public void setUsernameAcount(String usernameAcount) {
        this.usernameAcount = usernameAcount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
