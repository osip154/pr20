package com.Osipov.Pr20_Osipov;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String sName;
    public String mail;
    public String phone;
    public String city;
    public String age;

    public User() {
    }

    public User(String name, String sName, String mail, String phone, String city, String age) {
        this.name = name;
        this.sName = sName;
        this.mail = mail;
        this.phone = phone;
        this.city = city;
        this.age = age;
    }
}

