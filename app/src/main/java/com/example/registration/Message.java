package com.example.registration;

import android.net.Uri;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Message implements Serializable {
    private String text;
    private String user;
    private String time;
    private Uri image;

    public Message (String text, String user, Uri image) {
        this.text = text;
        this.user = user;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss (dd-MM)",
                new Locale("ru", "RU"));
        this.time = sdf.format(new Date());
        this.image = image;
    }

    public Message () {

    }

    //getters and setters for Firebase compatibility
    public String getText() {return text;}
    public void setText (String text) {this.text = text;}

    public String getUser() {return user;}
    public void setUser (String user) {this.user = user;}

    public String getTime() {return time;}
    public void setTime (String time) {this.time = time;}

    public Uri getImage() {return image;}
    public void setImage (Uri image) {this.image = image;}

}
