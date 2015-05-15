package com.example.zanabucinca.vantrackv10;

import android.location.Location;


public abstract class User {

    private String mode;
    private Location location;
    private String route;
	//Urla's default location
	private double defLat=38.3227279;
	private double defLong=26.7633122;

    public void setRoute(String route){
        this.route=route;
    }
    public String getRoute(){
        return route;
    }
    public void setLocation(Location location){
        this.location=location;
    }

    public double getLatitude()
    {	if(location!=null)
       		return location.getLatitude();
		else
			return defLat;

    }

    public double getLongitude(){

		if(location!=null)
			return location.getLatitude();
		else
			return defLong;
    }
    public abstract String getMode();

}
