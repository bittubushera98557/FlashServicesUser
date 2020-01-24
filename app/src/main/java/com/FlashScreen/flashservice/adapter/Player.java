package com.FlashScreen.flashservice.adapter;

/**
 * Created by indiaweb on 10/7/2017.
 */
public class Player
{
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage_upload() {
        return image_upload;
    }

    public void setImage_upload(String image_upload) {
        this.image_upload = image_upload;
    }

    private String category_Id ;
   private String category_name ;
   private String image_upload;
   private String date ;
}
