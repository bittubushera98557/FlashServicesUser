package com.FlashScreen.flashservice.adapter;

/**
 * Created by indiaweb on 11/15/2017.
 */
public class Contacts
{
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    private  String Id;
    private  String Name;
    private  String Phone;
}
