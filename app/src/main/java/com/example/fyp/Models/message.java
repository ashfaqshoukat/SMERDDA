package com.example.fyp.Models;

public class message {
    String token;
    android android;


    public message( String token,String body, String title, String image ) {


        this.token = token;
        android=new android(body,title,image);
    }


}
 class notification{
     String body,title,image;

     public notification(String body, String title, String image) {
         this.body=body;
         this.title=title;
         this.image=image;
     }

 }

  class android{
     notification notification;

      public android(String body, String title, String image) {
          notification=new notification(body,title,image);
      }

 }