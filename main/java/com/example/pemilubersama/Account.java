package com.example.pemilubersama;
public class Account {
    private String nik;
    private String fullName;
    private String origin;
    private String birthDate;
    private String address;
    private String occupation;
    private String gender;
    private String phoneNumber;

    public Account(String nik, String fullName,String origin, String birthDate,String address, String occupation, String gender, String phoneNumber) {
        this.nik = nik;
        this.fullName = fullName;
        this.origin = origin;
        this.address = address;
        this.occupation = occupation;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    public String getNik() {
        return nik;
    }
    public String getFullName() {
        return fullName;
    }
    public String getOrigin() {
        return origin;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getAddress() {
        return address;
    }
    public String getOccupation() {
        return occupation;
    }
    public String getGender() {
        return gender;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
