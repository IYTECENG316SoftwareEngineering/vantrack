package com.example.zanabucinca.vantrackv10.Operators;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


public class MyRoutesDataProvider extends ContentProvider {
	private static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final int ROUTE_DIR_CODE = 10;
	private static final int ROUTE_ID_CODE = 20;



	private MyRoutesDatabaseHelper dbHelper;

	static {
		matcher.addURI(MyRoutesContract.AUTHORITY, MyRoutesContract.MY_ROUTES, ROUTE_DIR_CODE);
		matcher.addURI(MyRoutesContract.AUTHORITY, MyRoutesContract.MY_ROUTES + "/#", ROUTE_ID_CODE);
	}

	public MyRoutesDataProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String selectionParam = selection;
		switch (matcher.match(uri)) {
			case ROUTE_DIR_CODE:
				break;
			case ROUTE_ID_CODE:
				selection = MyRoutesContract.ID + " = " + uri.getLastPathSegment();
				if (!TextUtils.isEmpty(selectionParam)) {
					selection = selection + " and " + selectionParam;
				}
				break;
			default:
				throw new IllegalArgumentException("Invalid uri to query: " + uri);
		}
		int nDeleted = db.delete(MyRoutesContract.MY_ROUTES, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return nDeleted;
	}
	@Override
	public String getType(Uri uri) {
		switch (matcher.match(uri)) {
			case ROUTE_DIR_CODE:
				return "vnd.android.cursor.dir/com.example.zanabucinca.route";
			case ROUTE_ID_CODE:
				return "vnd.android.cursor.item/com.example.zanabucinca.route";
			default:
				throw new IllegalArgumentException("Not a valid uri " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (matcher.match(uri) != ROUTE_DIR_CODE)
			throw new IllegalArgumentException("Cannot insert to uri: " + uri);

		long id = dbHelper.insertRoute(values);
		Uri elem_uri = ContentUris.withAppendedId(MyRoutesContract.CONTENT_URI, id);
		getContext().getContentResolver().notifyChange(elem_uri, null);
		return elem_uri;
	}

	@Override
	public boolean onCreate() {
		dbHelper = new MyRoutesDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
						String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(MyRoutesContract.MY_ROUTES);
		switch (matcher.match(uri)) {
			case ROUTE_DIR_CODE:
				break;
			case ROUTE_ID_CODE:
				qb.appendWhere(MyRoutesContract.ID + " = " + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Invalid uri to query: " + uri);
		}

		SQLiteDatabase db;
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			db = dbHelper.getReadableDatabase();
		}

		//Log.i(LOG_TAG, qb.buildQuery(projection, selection, null, null, sortOrder, null));

		Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
					  String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String selectionParam = selection;
		switch (matcher.match(uri)) {
			case ROUTE_DIR_CODE:
				break;
			case ROUTE_ID_CODE:
				selection = MyRoutesContract.ID + " = " + uri.getLastPathSegment();
				if (!TextUtils.isEmpty(selectionParam))
					selection = selection + " and " + selectionParam;
				break;
			default:
				throw new IllegalArgumentException("Invalid uri to query: " + uri);
		}
		int nUpdated = db.update(MyRoutesContract.MY_ROUTES, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return nUpdated;
	}
}

