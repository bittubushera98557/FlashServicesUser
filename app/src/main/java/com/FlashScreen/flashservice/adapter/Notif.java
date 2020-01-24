package com.FlashScreen.flashservice.adapter;

/**
 * Created by indiaweb on 11/16/2017.
 */
public class Notif
{
    private  String Id;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private  String Title;
    private  String Message;
    private  String Date;
    private  String Image;

    public String getVendor_Id() {
        return vendor_Id;
    }

    public void setVendor_Id(String vendor_Id) {
        this.vendor_Id = vendor_Id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    private String vendor_Id ;
    private String vendor_name ;
    private String category_Id;
    private String category_name ;
}
