package com.example.zanabucinca.vantrackv10.Services;

import android.os.AsyncTask;

import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;
import com.example.zanabucinca.vantrackv10.Model.User;

/**
 * Created by Yasin on 14.5.2015.
 */
public class DeleteUserTask extends AsyncTask<ApiConnector,Void, Void> {
    private User newUser;
    public DeleteUserTask(User newUser){
        this.newUser = newUser;
    }
    @Override

    protected Void doInBackground(ApiConnector... params) {
        params[0].deleteUser(newUser);
        return null;
    }
}
