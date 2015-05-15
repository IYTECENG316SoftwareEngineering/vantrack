package com.example.zanabucinca.vantrackv10.Operators;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyRoutesDatabaseHelper extends SQLiteOpenHelper {


	private static final String DB_NAME = "myroutes.sqlite";
	private static final int VERSION = 1;

	MyRoutesDatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder("create table " + MyRoutesContract.MY_ROUTES + " (");
		sb.append(MyRoutesContract.ID + " integer, ");
		sb.append(MyRoutesContract.ROUTE_NAME + " text primary key)");
		db.execSQL(sb.toString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Upgrade database, in the simplest case just drop all old data
		db.execSQL("DROP TABLE IF EXISTS " + MyRoutesContract.MY_ROUTES);
		onCreate(db);
	}


	public long insertRoute(ContentValues cv) {
		return getWritableDatabase().insertOrThrow(MyRoutesContract.MY_ROUTES, null, cv);
	}
    /*
	public long insertRoute(String route) {
		ContentValues cv = new ContentValues();
		cv.put(MyRoutesContract.ROUTE_NAME, route);
		return insertRoute(cv);
	}


	public MyRoutesCursor queryMyRoutes() {
		Cursor wrapped = getReadableDatabase().query(MyRoutesContract.MY_ROUTES, null, null, null, null, null, null);
		return new MyRoutesCursor(wrapped);
	}

	public void clearMyRoutes() {
		getWritableDatabase().delete(MyRoutesContract.MY_ROUTES, null, null);
	}*/

	public static class MyRoutesCursor extends CursorWrapper {
		public MyRoutesCursor(Cursor cursor) {
			super(cursor);
		}

		public String getRoute() {
			if (isBeforeFirst() || isAfterLast())
				return null;

			return getString(getColumnIndex(MyRoutesContract.ROUTE_NAME));

		}
	}
}
