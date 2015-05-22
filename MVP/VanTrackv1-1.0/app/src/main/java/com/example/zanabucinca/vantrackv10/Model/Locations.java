package com.example.zanabucinca.vantrackv10.Model;

import android.location.Location;

import java.util.HashMap;

/**
 * Created by Yasin on 3.5.2015.
 */
public class Locations {
    private HashMap<String,Location> locations;
    public Locations(){
        init();
    }

    // for control vans status(approximatly),and we chechk van passed or not
    public void init(){
        locations = new HashMap<String,Location>() ;
        locations.put("URLA",createLoc(38.400132,26.739578));
        locations.put("ZEYTINALANI",createLoc(38.370343,26.842574));
        locations.put("ISKELE",createLoc(38.3757,26.791));
        locations.put("IZMIR",createLoc(38.26,27.09));
        locations.put("IYTE",createLoc(38.32549,26.63004));
    }/*
    public HashMap<String,Location> getLocations(){
        return locations;
    }*/
    private Location createLoc(Double latitude,Double longitude){
            Location newLoc = new Location("");
        newLoc.setLongitude(longitude);
        newLoc.setLatitude(latitude);
        return newLoc;
    }
    //pass controler
    public boolean isPass(String key,User newUser,String latitude,String longitude){
        float[] resultForUser = new float[1];
        float[] resultForDriver = new float[1];
        Location.distanceBetween(newUser.getLatitude(), newUser.getLongitude(), locations.get(key).getLatitude(),locations.get(key).getLongitude(), resultForUser);
        Location.distanceBetween(Double.parseDouble(latitude), Double.parseDouble(longitude), locations.get(key).getLatitude(),locations.get(key).getLongitude(), resultForDriver);
        if(resultForUser[0]>=resultForDriver[0])
            return true;
        else
            return false;
    }
}
