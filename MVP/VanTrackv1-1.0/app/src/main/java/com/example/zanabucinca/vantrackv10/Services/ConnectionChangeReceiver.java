package com.example.zanabucinca.vantrackv10.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Views.MapsActivity;

//if internet connection changed, android system send broadcaset. Then this class recieved this broadcast.
public class ConnectionChangeReceiver extends BroadcastReceiver{
    private String LOG_TAG = "ConnectionChangeReceiver";
    public static final String ACTION_INTERNET_CONNECTION ="com.example.zanabucinca.vantrackv10. ACTION_INTERNET_CONNECTION";

    //for getting broad cast, and sending broad cast to maps activity to know that internet is available
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Network connectivity change");
        Intent intentForActivity = new Intent(ACTION_INTERNET_CONNECTION);

        if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
            Log.d(LOG_TAG, "There's no network connectivity");
            intentForActivity.putExtra("isConnected",false);
        }
        else{
            intentForActivity.putExtra("isConnected", true);
            if(intent.getExtras()!=null) {
                NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                    Log.i(LOG_TAG, "Network " + ni.getTypeName() + " connected");
                    intentForActivity.putExtra("type", ni.getTypeName());
                }
                if(ni==null){
                    Log.d(LOG_TAG, "There's no network connectivity");
                    intentForActivity.putExtra("isConnected",false);
                }

           }

        }

        if (MapsActivity.mapsActivityPresenter !=null)
            MapsActivity.mapsActivityPresenter.getContext().sendBroadcast(intentForActivity);

    }
}