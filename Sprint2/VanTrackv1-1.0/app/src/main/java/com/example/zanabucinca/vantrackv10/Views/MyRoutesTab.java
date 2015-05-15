package com.example.zanabucinca.vantrackv10.Views;


import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zanabucinca.vantrackv10.Operators.MyRoutesContract;
import com.example.zanabucinca.vantrackv10.Operators.MyRoutesDatabaseHelper;
import com.example.zanabucinca.vantrackv10.Presenters.MyRoutesTapPresenter;
import com.example.zanabucinca.vantrackv10.Presenters.MyRoutesTapPresenterImpl;
import com.example.zanabucinca.vantrackv10.R;

import java.util.ArrayList;

public class MyRoutesTab extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{

	private static final String LOG_TAG = "MyRoutesTab";
	private static final boolean USE_LOADER = true;

	private MyRoutesCursorAdapter myRoutesCursorAdapter;

	private MyRoutesTapPresenter myRoutesTapPresenter;
	private ArrayList<String> itemUriList;

	private MainView listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view=inflater.inflate(R.layout.tab_1, container, false);
        myRoutesTapPresenter = new MyRoutesTapPresenterImpl(getActivity());
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        //  Log.i("zana:","onAct");
		if (USE_LOADER) {
			getLoaderManager().initLoader(0, null, this);
		} else {
            myRoutesTapPresenter.createCursor();
            extractItemUris(myRoutesTapPresenter.getCursor());
		}

		myRoutesCursorAdapter = new MyRoutesCursorAdapter(getActivity(), myRoutesTapPresenter.getCursor());
		this.getListView().setTextFilterEnabled(true);
		this.setListAdapter(myRoutesCursorAdapter);

	}

	private void extractItemUris(MyRoutesDatabaseHelper.MyRoutesCursor cursor) {
		itemUriList = new ArrayList<String>();

		if (cursor.moveToFirst()) {
			do {
				long id = cursor.getLong(cursor.getColumnIndex(MyRoutesContract.ID));
				Uri uri = ContentUris.withAppendedId(MyRoutesContract.CONTENT_URI, id);
				itemUriList.add(uri.toString());
			} while (cursor.moveToNext());
		}
	}

	public void setOnRouteSelectedListener(MainView listener) {
		this.listener = listener;
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		TextView routeView=(TextView) v;
		String route=routeView.getText().toString();
		onRouteSelected(route);
	}

	@Override
	public void onDestroy() {
		if (myRoutesTapPresenter.getCursor() != null)
            myRoutesTapPresenter.getCursor().close();
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(), MyRoutesContract.CONTENT_URI, MyRoutesContract.ALL,
				null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		MyRoutesDatabaseHelper.MyRoutesCursor rCursor = new MyRoutesDatabaseHelper.MyRoutesCursor(cursor);
		extractItemUris(rCursor);
		myRoutesCursorAdapter.swapCursor(rCursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		itemUriList = null;
		myRoutesCursorAdapter.swapCursor(null);
	}

	void onRouteSelected(String route) {
		if (listener != null){
			listener.onRouteSelected(route);
			}
	}

	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		listener=(MainView)activity;
	}
	@Override
	public void onDetach(){
		super.onDetach();
		listener=null;
	}



	private static class MyRoutesCursorAdapter extends CursorAdapter {
		public MyRoutesCursorAdapter(Context context, MyRoutesDatabaseHelper.MyRoutesCursor cursor) {
			super(context, cursor, 0);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater.from(context));
			return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			MyRoutesDatabaseHelper.MyRoutesCursor rc = (MyRoutesDatabaseHelper.MyRoutesCursor) cursor;
			String route = rc.getRoute();

			TextView textView = (TextView)view;
			textView.setText(route);
		}
	}

}