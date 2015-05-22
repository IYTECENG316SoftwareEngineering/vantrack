package com.example.zanabucinca.vantrackv10.Presenters;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by Yasin on 14.5.2015.
 */
public interface ActivityPresenter {
    public void setInfoAllUsers(JSONArray jsonArray);
    public void toStartGetAllUsersTask();
    public void doneAsynTask();
    public boolean turnOffAlarm();
}
