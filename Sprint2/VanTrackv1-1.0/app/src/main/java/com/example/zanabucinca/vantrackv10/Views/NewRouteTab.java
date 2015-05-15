package com.example.zanabucinca.vantrackv10.Views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.example.zanabucinca.vantrackv10.Presenters.NewRouteTabPresenter;
import com.example.zanabucinca.vantrackv10.Presenters.NewRouteTabPresenterImpl;
import com.example.zanabucinca.vantrackv10.R;

public class NewRouteTab extends ListFragment implements OnItemClickListener {
	private ArrayAdapter<CharSequence> adapter;
	private MainView listener;
    private NewRouteTabPresenter newRouteTabPresenter;
    private final String Log_TAG = "NewRouteTabPresenter";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab_2, container, false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		adapter = ArrayAdapter.createFromResource(getActivity(), R.array.routes_array, android.R.layout.simple_list_item_1);
        newRouteTabPresenter = new NewRouteTabPresenterImpl(getActivity());
		setListAdapter(adapter);
		getListView().setOnItemClickListener(this);

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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		String route=adapter.getItem(position).toString();
		onRouteSelected(route);
        Log.d(Log_TAG, "" + newRouteTabPresenter.insertDb(route));

	}
	void onRouteSelected(String route){
		if(listener!=null)
		listener.onRouteSelected(route);
	}
}
