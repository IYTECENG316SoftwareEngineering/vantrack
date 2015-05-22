package com.example.zanabucinca.vantrackv10.Presenters;

import com.example.zanabucinca.vantrackv10.Operators.MyRoutesDatabaseHelper;

/**
 * Created by Yasin on 15.5.2015.
 */
public interface MyRoutesTapPresenter {
    public void createCursor();
    public MyRoutesDatabaseHelper.MyRoutesCursor getCursor();
}
