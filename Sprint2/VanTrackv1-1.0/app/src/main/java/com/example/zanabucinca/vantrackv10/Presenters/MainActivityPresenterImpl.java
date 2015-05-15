package com.example.zanabucinca.vantrackv10.Presenters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.zanabucinca.vantrackv10.Views.MapsActivity;
import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;
import com.example.zanabucinca.vantrackv10.Services.LocationsUpdateService;
import com.example.zanabucinca.vantrackv10.Views.MainActivity;
import com.example.zanabucinca.vantrackv10.Views.PrivacyPreferenceFragment;
import com.example.zanabucinca.vantrackv10.Services.DeleteUserTask;
import com.example.zanabucinca.vantrackv10.Services.GetAllUsersFromDbTask;
import com.example.zanabucinca.vantrackv10.Services.InsertUserTask;
import com.example.zanabucinca.vantrackv10.Model.User;
import com.example.zanabucinca.vantrackv10.Views.UserPreferenceFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yasin on 14.5.2015.
 */
public class MainActivityPresenterImpl implements MainActivityPresenter {
    private String LOG_TAG = "MainActivityPresenterImpl";
    private MainActivity context;
    private User newUser ;
    private ApiConnector apiConnector;
    private ArrayList<ArrayList<String>> infoAllUsers;


    //take mainView in Constructor
    public MainActivityPresenterImpl(MainActivity context){
     //   this.mainActivity = mainActivity;
        this.context = context ;
    }
    //control of Gps for location
    public boolean isGpsOn(){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public void turnOnGps(){
        boolean isGPSEnabled = false;
        try {
            isGPSEnabled = isGpsOn();
        } catch (Exception ex) {}
        if (!isGPSEnabled){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(
                    "Your GPS module is disabled. Would you like to enable it ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // Sent user to GPS settings screen
                                    final Intent intent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    context.startActivityForResult(intent, 1);
                                    dialog.dismiss();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                    context.onBackPressed();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    //if something is wrong,have to turn of alarm in mainActivity
    public boolean turnOffAlarm(){
        if (LocationsUpdateService.isServiceAlarmOn(context)) {
            LocationsUpdateService.unsetServiceAlarm(context);
            return true;
        }
        else
            return false;
    }
    //creating user according to him or his preference
    public boolean createUser(){
        int isPublic;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String mode = sp.getString(UserPreferenceFragment.PREFERENCE_MODE, MainActivity.DEFAULT_MODE);
        boolean publicLocation = sp.getBoolean(PrivacyPreferenceFragment.PREFERENCE_PUBLIC_LOCATION, MainActivity.DEFAULT_PUBLIC_LOCATION);
        boolean isUse3g = sp.getBoolean(UserPreferenceFragment.PREFERENCE_USE_3G, MainActivity.DEFAULT_USE_3G);
        isPublic= (publicLocation)? 1 : 0;

        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String id = tManager.getDeviceId();
       // Log.d(LOG_TAG, "" + isPublic);
        if(newUser==null){
            newUser=new User(id,isPublic,mode,isUse3g);
            apiConnector =new ApiConnector();
        }
        else {
            newUser.setIsUse3g(isUse3g);
            newUser.setMode(mode);
            newUser.setIsPublic(isPublic);
        }
        return true;
    }
    //setting choosen route to newUser
    public boolean setRouteOnUser(String route){
        newUser.setRoute(route);
        return true;
    }
    //checking user can access internet or not
    public boolean checkInternetCondition(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni= connectivityManager.getActiveNetworkInfo();
        if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
            Log.d(LOG_TAG, "Network " + ni.getTypeName() + " connected");
            if(ni.getTypeName().contains("mobile")&&newUser.getIsUse3g()) {
               // Log.d(LOG_TAG, "mobile");
                return true;
            }
            else if(ni.getTypeName().equalsIgnoreCase("WIFI")) {
                //Log.d(LOG_TAG, "Wifi");
                return true;
            }
            //option of 3g is off so user shouldn't connect to internet. User must be turn on 3g option
            else {
                Toast.makeText(context, "You should open your 3g from preference on settigins vantrack ", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        else {
            Toast.makeText(context, "No internet",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    //taking all public user info from asycntasks
    public void setInfoAllUsers(JSONArray jsonArray){
        infoAllUsers = new ArrayList<ArrayList<String>>();
        //   Log.d("findikfindikfindik","findik");
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject json=null;
            try{
                //  Log.d("jsonArrayyyyy",jsonArray.toString());
                json=jsonArray.getJSONObject(i);

                ArrayList<String> loc=new ArrayList<String>();
                loc.add((json.getString("id")));
                loc.add(json.getString("isPublic"));
                loc.add(json.getString("route"));
                loc.add(json.getString("mode"));
                loc.add(json.getString("speed"));
                loc.add((json.getString("latitude")));
                loc.add((json.getString("longitude")));

                infoAllUsers.add(i, loc);

            }catch(JSONException e)
            {

                Log.e(LOG_TAG,"json exeption occured");
             //   e.printStackTrace();
            }catch (NullPointerException e){

                Log.e(LOG_TAG, "NullPointerException occured");
            }
        }
    }
    //if closed activite ,should delete newUser on Db
    public boolean deleteNewUser(){
        if(newUser !=null ){
            new DeleteUserTask(newUser).execute(apiConnector);
            return true;
        }
        return false;
    }
    //if user select route, then  maps activity will start after newUser inserted db  and other public user getting on db
    public void toStartMapsActivity(){
        new InsertUserTask(newUser,this).execute(apiConnector);
    }
    public void toStartGetAllUsersTask(){
        new GetAllUsersFromDbTask(this).execute(apiConnector);
    }
    //asynTask is done then must start maps Activity
    public void doneAsynTask(){
        Intent intent = new Intent(context , MapsActivity.class);
        intent.putExtra("info",infoAllUsers);
        intent.putExtra("newUser",(Serializable) newUser);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
