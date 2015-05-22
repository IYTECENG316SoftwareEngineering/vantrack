package com.example.zanabucinca.vantrackv10.Operators;

import android.net.Uri;

public class MyRoutesContract {
	public static final String MY_ROUTES = "myroutes";
	public static final String ID = "_id";
	public static final String ROUTE_NAME = "routename";

	static public String[] ALL = {
			ID, ROUTE_NAME
	};

	static public final String AUTHORITY = "com.example.zanabucinca.vantrackv10.myroutesdata.provider";
	static public final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MY_ROUTES);
}
