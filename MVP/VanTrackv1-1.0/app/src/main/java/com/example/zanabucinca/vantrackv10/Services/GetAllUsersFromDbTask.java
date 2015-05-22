package com.example.zanabucinca.vantrackv10.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;
import com.example.zanabucinca.vantrackv10.Presenters.ActivityPresenter;

import org.json.JSONArray;

/**
 * Created by Yasin on 14.5.2015.
 */
public class GetAllUsersFromDbTask extends AsyncTask <ApiConnector,Long, JSONArray> {
    private String LOG_TAG = "InsertUserTask";
    private ActivityPresenter presenter;
    public GetAllUsersFromDbTask(ActivityPresenter presenter){
        this.presenter = presenter;
    }
    @Override
    protected JSONArray doInBackground(ApiConnector... params) {

        return params[0].getAllUsers();
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        if(jsonArray!=null){
          //  presenter.setIsAsycnTaskDone(true);
            presenter.setInfoAllUsers(jsonArray);
            presenter.doneAsynTask();
        }
        else{
           // presenter.setIsAsycnTaskDone(false);
            Log.e(LOG_TAG+" Error Message","Could not connect to the db");
           // Log.d(LOG_TAG,"getAllUsersTask false");
        }
        /*
        Intent intent = new Intent( context, MapsActivity.class);
        intent.putExtra("info", setLocations(jsonArray));
        intent.putExtra("newUser",(Serializable)newUser);
        //intent.putExtra("apiConnector",(Serializable)apiConnector);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
    }
}
