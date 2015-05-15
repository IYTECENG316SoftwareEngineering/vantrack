package com.example.zanabucinca.vantrackv10.Presenters;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Model.Locations;
import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;
import com.example.zanabucinca.vantrackv10.Services.GetAllUsersFromDbTask;
import com.example.zanabucinca.vantrackv10.Services.LocationsUpdateService;
import com.example.zanabucinca.vantrackv10.Services.UpdateUserTask;
import com.example.zanabucinca.vantrackv10.Model.User;
import com.example.zanabucinca.vantrackv10.Views.MapsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yasin on 14.5.2015.
 */
public class MapsActivityPresenterImpl implements MapsActivityPresenter {

    private final String DEFAULT_UPDATE_INTERVAL = "15000";
    private String LOG_TAG = "MapsActivityPresenterImpl";
    private User newUser;
    private ArrayList<ArrayList<String>> infoAllUsers;
    private Context context;
    private ApiConnector apiConnector;
    private Locations locations;
    private MapsView mapsView;

    public MapsActivityPresenterImpl(MapsView mapsView, Context context){
        this.context = context;
        this.mapsView = mapsView;
    }
    //for broadcast to mapsActıvıty about internet connection
    public Context getContext(){
        return context;
    }

    //getting extras
    public void getExtras(Bundle extras){
        infoAllUsers=(ArrayList<ArrayList<String>>) extras.getSerializable("info");
        newUser=(User) extras.getSerializable("newUser");
        apiConnector =new ApiConnector();
      //  Log.d("info", allInfo.toString());
      //  Log.d(LOG_TAG+" id", newUser.getId());
    }
    //setting default locations for user missed van or not missed
    public void setDefaultLocation(){
        locations = new Locations();
    }
    //startActionupdate for alarm service
    public void startActionUpdateAlarm(){
        LocationsUpdateService.startActionUpdate(context);
    }
    //setserviceAlarim time and context
    public void setServiceAlarm() {
        int updateInterval = Integer.parseInt( DEFAULT_UPDATE_INTERVAL);
        LocationsUpdateService.setServiceAlarm(context, updateInterval);
    }
    //control of content of setupping map
    public ArrayList<List<String>> setUpMap(){
        ArrayList<List<String>> users = new ArrayList<List<String>>();
        if(newUser != null ){
            if(newUser.getMode().equals("User")){
                for (List<String> row : infoAllUsers) {
                    if((!newUser.getId().equals(row.get(0)))) {
                        if(row.get(3).equals("DRIVER")&& newUser.getRoute().equalsIgnoreCase(row.get(2))) {
                            boolean isPass=false;
                            String[] parts =newUser.getRoute().split("-");
                            isPass = locations.isPass(parts[1], newUser, row.get(5), row.get(6));
                            if(isPass) {
                                row.add("missed van");
                                users.add(row);
                            }
                            else{
                                row.add("comming van");
                                users.add(row);
                            }
                        }
                        else if (!row.get(3).equals("DRIVER")){
                            row.add("public one");
                            users.add(row);
                        }
                    }
                }
            }
            //Driver
            else{
                for (List<String> row : infoAllUsers) {
                    if (row.get(3).equals("DRIVER") && (!newUser.getId().equals(row.get(0)))) {
                        row.add("van");
                        users.add(row);
                    } else {
                        row.add("public one");
                        users.add(row);
                    }
                }
            }
        }
        return users;
    }
    //calculating van comming time
    public String calculateTime(String latitude,String longtitude,String speed){
        float[] result = new float[1];
        Location.distanceBetween(newUser.getLatitude(), newUser.getLongitude(), Double.parseDouble(latitude), Double.parseDouble(longtitude), result);
        return ((Float.parseFloat(speed) / 60) < 0)
                ? "calculating. . ."
                : ((((int) (result[0] / Float.parseFloat(speed))) / 60 == 0) ?
                "A few minutes"
                : ((int) (result[0] / Float.parseFloat(speed))) / 60 + "minutes");

    }
    //return current user for using location
    public User getNewUser(){
        return newUser;
    }
    //turn off alarm
    public boolean turnOffAlarm(){
        if (LocationsUpdateService.isServiceAlarmOn(context)) {
           // Log.d(LOG_TAG,"alarm is off");
            LocationsUpdateService.unsetServiceAlarm(context);
            return true;
        }
        else
            return false;
    }
    //setting location for current user
    public void setNewUserLocation(Location location){
        newUser.setLocation(location);
    }
    //setting speed for newUser if newUser is Driver
    public void setSpeed(Double latitude , Double longtitude, long differenceTime){
        if(newUser.getMode().equals("Driver")){
            float[] result = new float[1];
            Location.distanceBetween(newUser.getLatitude(),newUser.getLongitude(),latitude, longtitude, result);
            if(differenceTime>0&&result[0]>0)//(differenceTime/1000)
                newUser.setSpeed(result[0]/(differenceTime/1000));
            else
                newUser.setSpeed(0);
           // Log.d("findik speeeed:",""+newUser.getSpeed());
        }
    }
    //Db updating current user location or others info
    public void updateNewUser(){
       new UpdateUserTask(newUser).execute(apiConnector);
    }
    //alarms call this function for reload the map define time period according to db
    public void toStartGetAllUsersTask(){
        new GetAllUsersFromDbTask(this).execute(apiConnector);
    }
    //asyntask update infoAllUsers
    public void setInfoAllUsers(JSONArray jsonArray){
        infoAllUsers = new ArrayList<ArrayList<String>>();
        //Log.d("findik","yasin");
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject json = null;
            try {
                //  Log.d("jsonArrayyyyy",jsonArray.toString());
                json = jsonArray.getJSONObject(i);

                ArrayList<String> loc = new ArrayList<String>();
                loc.add((json.getString("id")));
                loc.add(json.getString("isPublic"));
                loc.add(json.getString("route"));
                loc.add(json.getString("mode"));
                loc.add(json.getString("speed"));
                loc.add((json.getString("latitude")));
                loc.add((json.getString("longitude")));

                infoAllUsers.add(i, loc);

            } catch (JSONException e) {

                Log.e(LOG_TAG, "json exeption occured");
                //   e.printStackTrace();
            } catch (NullPointerException e) {

                Log.e(LOG_TAG, "NullPointerException occured");

            }
        }
       // Log.d(LOG_TAG,""+jsonArray);
    }
    //all asynTask is finish to own job then  map will setUp again updated infoalluser
    public void doneAsynTask(){
        mapsView.setUpMap();
    }
}
