package com.example.zanabucinca.vantrackv10.Services;

import android.os.AsyncTask;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;
import com.example.zanabucinca.vantrackv10.Presenters.ActivityPresenter;
import com.example.zanabucinca.vantrackv10.Model.User;

/**
 * Created by Yasin on 14.5.2015.
 */
public class InsertUserTask  extends AsyncTask<ApiConnector,Void, Boolean> {
    private String LOG_TAG = "InsertUserTask";
    private User newUser;
    private ActivityPresenter presenter;
    public InsertUserTask(User newUser,ActivityPresenter presenter){
        this.newUser = newUser;
        this.presenter = presenter;
    }
    @Override
    protected Boolean doInBackground(ApiConnector... params) {
        return params[0].insertUser(newUser);
    }

    @Override
    protected void onPostExecute(Boolean isDone) {
        if(isDone) {
            //new GetAllUsersTask().execute(apiConnector);
            //presenter.setIsAsycnTaskDone(true);
            presenter.toStartGetAllUsersTask();
        }
        else{
            // Toast.makeText(context, "Could not connect to the db", Toast.LENGTH_SHORT).show();
         //   presenter.setIsAsycnTaskDone(false);
            Log.e(LOG_TAG+" Error Message","Could not connect to the db");
           // Log.d(LOG_TAG,"insertUserTask false");
        }

    }

}
