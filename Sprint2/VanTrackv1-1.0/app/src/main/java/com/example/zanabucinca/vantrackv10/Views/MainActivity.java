package com.example.zanabucinca.vantrackv10.Views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.example.zanabucinca.vantrackv10.Model.User;
import com.example.zanabucinca.vantrackv10.Operators.ApiConnector;
import com.example.zanabucinca.vantrackv10.Presenters.MainActivityPresenter;
import com.example.zanabucinca.vantrackv10.Presenters.MainActivityPresenterImpl;
import com.example.zanabucinca.vantrackv10.R;


public class MainActivity extends ActionBarActivity implements MainView {
    private String LOG_TAG = "MainActivity";
	public static final String DEFAULT_MODE = "User";
	public static final boolean DEFAULT_USE_3G = false;
	public static final boolean DEFAULT_PUBLIC_LOCATION=false ;
    private MainActivityPresenter mainActivityPresenter;
	// Declaring Your View and Variables


    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[]={"My Routes","New Route"};
    private int Numboftabs =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


		toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);


		// Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
		adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

		// Assigning ViewPager View and setting the adapter
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		// Assiging the Sliding Tab Layout View
		tabs = (SlidingTabLayout) findViewById(R.id.tabs);
		tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

		// Setting Custom Color for the Scroll bar indicator of the Tab View
		tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});

		// Setting the ViewPager For the SlidingTabsLayout
		tabs.setViewPager(pager);

        mainActivityPresenter = new MainActivityPresenterImpl(this);


    }
	@Override
	protected void onStart() {
		super.onStart();
		mainActivityPresenter.createUser();

	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent intent = new Intent(this, FragmentPreferencesActivity.class);
			startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
    @Override
    protected void onResume() {
         super.onResume();
         mainActivityPresenter.turnOnGps();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         switch (requestCode){
            case 1 :
                if (!mainActivityPresenter.isGpsOn()) {
                    super.finish();
                }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.turnOffAlarm();
        if(mainActivityPresenter.deleteNewUser()){
            Log.d(LOG_TAG,"Deleted user");
        }


    }
    @Override
    public void onRouteSelected(String route) {
	//	Log.i(LOG_TAG,"Public, mode"+ newUser.getIsPublic() + newUser.getMode());
        mainActivityPresenter.setRouteOnUser(route);
        if(mainActivityPresenter.checkInternetCondition()) {
            mainActivityPresenter.toStartMapsActivity();
            /*apiConnector.insertNewUserOnMap();
            isNewUserInserted = true;*/

        }
    }


}
