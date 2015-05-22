package com.example.zanabucinca.vantrackv10.Presenters;

import android.content.ContentResolver;
import android.content.Context;

import com.example.zanabucinca.vantrackv10.Operators.MyRoutesContract;
import com.example.zanabucinca.vantrackv10.Operators.MyRoutesDatabaseHelper;

/**
 * Created by Yasin on 15.5.2015.
 */
public class MyRoutesTapPresenterImpl implements MyRoutesTapPresenter {
    private MyRoutesDatabaseHelper.MyRoutesCursor cursor;
    private Context context;
    public MyRoutesTapPresenterImpl(Context context){
        this.context = context;
    }
    public void createCursor(){
        ContentResolver cr =context.getContentResolver();
        cursor = new MyRoutesDatabaseHelper.MyRoutesCursor(cr.query(MyRoutesContract.CONTENT_URI,
                MyRoutesContract.ALL, null, null, null));
    }
    public MyRoutesDatabaseHelper.MyRoutesCursor getCursor(){
        return cursor;
    }
    
}
