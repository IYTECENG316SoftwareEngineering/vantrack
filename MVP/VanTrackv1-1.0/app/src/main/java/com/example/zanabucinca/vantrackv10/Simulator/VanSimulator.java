package com.example.zanabucinca.vantrackv10.Simulator;

import android.location.Location;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Model.User;

/**
 * Created by Yasin on 22.5.2015.
 */
public class VanSimulator {
    private String LOG_TAG = "VanSimulator";
    private User van;
    private double dest[][]={{38.32381,26.63090,8},{38.323202,26.630757,8},{38.32235,26.63089,8},{38.322345,26.632065,8},{38.323578,26.632355,8},{38.32483,26.63276,8},
                            {38.32493,26.63393,8},{38.324647,26.634925,8},{38.32435,26.635869,8},{38.324056,26.63677,8},{38.323761,26.637634,8},{38.32356,26.63982,8},
                            {38.32323,26.63976,8},{38.32279,26.63965,8},{38.322387,26.639575,8},{38.3219626,26.639484,8},{38.321541,26.639398,8},{38.321226,26.639323,8},
                            {38.320746,26.63920,8},{38.3202455,26.639103,8},{38.3197615,26.6389588,8},{38.31925,26.63886,8},{38.31892,26.63879,8},{38.318482,26.6386959,8}};
    private static int i;
    private boolean isGoing;
    public VanSimulator(User van,boolean isGoing){
        this.van = van;
        this.isGoing = isGoing;
        if(isGoing) {
            this.van.setRoute("IYTE-URLA");
            i = 0;
        }
        else{
            this.van.setRoute("URLA-IYTE");
            i = dest.length-1;
        }
    }
    public void moveVan(){
        Log.d(LOG_TAG,"isgoing: "+isGoing);
        if(isGoing) {
            Location location = new Location("");
            location.setLatitude(dest[i][0]);
            location.setLongitude(dest[i][1]);
            van.setSpeed(Float.parseFloat("" + dest[i][2]));
            van.setLocation(location);
            i++;
            if(i==dest.length){
                this.van.setRoute("URLA-IYTE");
                isGoing=false;
                i = dest.length-1;
            }
        }
        else{
            Location location = new Location("");
            location.setLatitude(dest[i][0]);
            location.setLongitude(dest[i][1]);
            van.setSpeed(Float.parseFloat("" + dest[i][2]));
            van.setLocation(location);
            i--;
            if(i==-1){
                isGoing=true;
                this.van.setRoute("IYTE-URLA");
                i = 0;
            }
        }

    }
    public User getVan(){
        return van;
    }
}
