package com.example.zanabucinca.vantrackv10.Presenters;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.example.zanabucinca.vantrackv10.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasin on 14.5.2015.
 */
public interface MapsActivityPresenter extends ActivityPresenter {
    public void getExtras(Bundle extras);
    public void setDefaultLocation();
    public void startActionUpdateAlarm();
    public void setServiceAlarm();
    public ArrayList<List<String>> setUpMap();
    public String calculateTime(String latitude,String longtitude,String speed);
    public User getNewUser();
    public void setNewUserLocation(Location location);
    public void setSpeed(Double latitude , Double longtitude, long differenceTime);
    public void updateNewUser();
    public Context getContext();
}
