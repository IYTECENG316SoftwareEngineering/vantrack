package com.example.zanabucinca.vantrackv10.Presenters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Operators.MyRoutesContract;

/**
 * Created by Yasin on 15.5.2015.
 */
public class NewRouteTabPresenterImpl implements NewRouteTabPresenter {
    private final String Log_TAG = "NewRouteTabPresenterImpl";
    private Context context;
    public NewRouteTabPresenterImpl(Context context){
        this.context = context;
    }
    public boolean insertDb(String route){
        ContentValues values = new ContentValues();
        values.put(MyRoutesContract.ROUTE_NAME, route);

        try{
            context.getContentResolver().insert(MyRoutesContract.CONTENT_URI, values);

        }catch(SQLiteConstraintException e){

            Log.i(Log_TAG, "The route is already part of MyRoutes.");
            return false;

        }
        return true;
    }
}
