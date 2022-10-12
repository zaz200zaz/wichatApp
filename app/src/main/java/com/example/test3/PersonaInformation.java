package com.example.test3;

public class PersonaInformation {
    private String fullName;
    private String fullName2;
    private String howToReadName;
    private String howToReadName2;
    private String sex;
    private String birthDay;
    private String country;
    private String postCode;
    private String address;
    private String address2;
    private String address3;
    private String address4;
    private String phoneNumber;

    public PersonaInformation(String fullName, String fullName2, String howToReadName, String howToReadName2, String sex, String birthDay, String country, String postCode, String address, String address2, String address3, String address4, String phoneNumber) {
        this.fullName = fullName;
        this.fullName2 = fullName2;
        this.howToReadName = howToReadName;
        this.howToReadName2 = howToReadName2;
        this.sex = sex;
        this.birthDay = birthDay;
        this.country = country;
        this.postCode = postCode;
        this.address = address;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.phoneNumber = phoneNumber;
    }

    public PersonaInformation(String fullName, String howToReadName, String sex, String birthDay, String country, String postCode, String address, String address2, String address3, String address4, String phoneNumber) {
        this.fullName = fullName;
        this.howToReadName = howToReadName;
        this.sex = sex;
        this.birthDay = birthDay;
        this.country = country;
        this.postCode = postCode;
        this.address = address;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.phoneNumber = phoneNumber;
    }

    public PersonaInformation(String fullName, String howToReadName, String sex, String birthDay, String country, String postCode, String address, String phoneNumber) {
        this.fullName = fullName;
        this.howToReadName = howToReadName;
        this.sex = sex;
        this.birthDay = birthDay;
        this.country = country;
        this.postCode = postCode;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName2() {
        return fullName2;
    }

    public void setFullName2(String fullName2) {
        this.fullName2 = fullName2;
    }

    public String getHowToReadName2() {
        return howToReadName2;
    }

    public void setHowToReadName2(String howToReadName2) {
        this.howToReadName2 = howToReadName2;
    }

    public PersonaInformation() {
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHowToReadName() {
        return howToReadName;
    }

    public void setHowToReadName(String howToReadName) {
        this.howToReadName = howToReadName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
