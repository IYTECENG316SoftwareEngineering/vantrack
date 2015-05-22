package com.example.zanabucinca.vantrackv10.Model;

import android.location.Location;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zanabucinca on 16/04/15.
 */
public class User implements Serializable{

    private Double longitude;
    private Double latitude;
    private String route;
    private String mode;
    private String id;
    private float speed;
    private int isPublic;
    private boolean isUse3g;

    public User(String id,int isPublic,String mode,boolean isUse3g){
        this.id = id;
        this.isPublic = isPublic;
        this.mode = mode;
        this.isUse3g = isUse3g;
    }
    public void setIsUse3g(boolean isUse3g){this.isUse3g = isUse3g;}
    public void setIsPublic(int isPublic){
        this.isPublic  = isPublic;
    }
    public void setRoute(String route){
        this.route=route;
    }
    public void setMode(String mode){
        this.mode = mode;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setLocation(Location location) {
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public boolean getIsUse3g(){return isUse3g;}
    public int getIsPublic(){return isPublic;}
    public float getSpeed(){
        return speed;
    }
    public Double getLongitude(){
        return longitude;
    }
    public Double getLatitude(){
        return latitude;
    }
    public String getMode(){
        return mode;
    }
    public String getId(){
        return id;
    }
    public String getRoute(){
        return route;
    }

    //for saving to db current user
    public ArrayList<NameValuePair> getNameValuePairs(){
        ArrayList<NameValuePair> nameValuePairs= new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id",""+id));
        nameValuePairs.add(new BasicNameValuePair("isPublic",""+isPublic));
        nameValuePairs.add(new BasicNameValuePair("mode",""+mode));
        nameValuePairs.add(new BasicNameValuePair("route",route));
        nameValuePairs.add(new BasicNameValuePair("speed",""+speed));
        nameValuePairs.add(new BasicNameValuePair("longitude",""+longitude));
        nameValuePairs.add(new BasicNameValuePair("latitude",""+latitude));
        return nameValuePairs;

    }
}
