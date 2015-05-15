package com.example.zanabucinca.vantrackv10;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements RouteFragment.OnRouteSelectedListener {
    private User newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
		//getting the last known location to avoid wrong locations
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                    }
                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                    @Override
                    public void onLocationChanged(final Location location) {
                    }
                });

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        TextView modeselect=(TextView) findViewById(R.id.modeselect);
        modeselect.setVisibility(View.INVISIBLE);
        RadioGroup modeoption=(RadioGroup) findViewById(R.id.mode_options);
        modeoption.setVisibility(View.INVISIBLE);
        RouteFragment routeFragment = (RouteFragment) getFragmentManager().findFragmentById(R.id.route_container);
        if(routeFragment==null){
            FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.route_container, new RouteFragment());
            String tag=null;
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        FrameLayout routeframe=(FrameLayout) findViewById(R.id.route_container);
        routeframe.setVisibility(View.VISIBLE);


        switch(view.getId()) {
            case R.id.user:
                if (checked){
                    newUser= new RegularUser();
                    newUser.setLocation(location);

                }
                    break;
            case R.id.driver:
                if (checked){
                    newUser=new DriverUser();
                    newUser.setLocation(location);

                }

                    break;
        }
    }



    @Override
    public void routeSelected(String route) {
        newUser.setRoute(route);
        new InsertUserTask().execute(new ApiConnector());
        new GetAllUsersTask().execute(new ApiConnector());
    }




    public class InsertUserTask extends AsyncTask<ApiConnector, Void, Void>
    {
        @Override
        protected Void doInBackground(ApiConnector... params) {
            params[0].InsertUser(newUser);
            return null;
        }

    }

    public class GetAllUsersTask extends AsyncTask<ApiConnector,Long, JSONArray>

    {
        @Override
        protected JSONArray doInBackground(ApiConnector... params) {

            return params[0].GetAllUsers();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

           Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
           intent.putExtra("locations", setLocations(jsonArray));
           startActivity(intent);

        }
    }
    public ArrayList<ArrayList<Double>> setLocations(JSONArray jsonArray){
        ArrayList<ArrayList<Double>> allLocations = new ArrayList<ArrayList<Double>>();

        for(int i=0; i<jsonArray.length(); i++){
            JSONObject json=null;
            try{
                json=jsonArray.getJSONObject(i);
                ArrayList<Double> loc=new ArrayList<Double>();
                loc.add(Double.parseDouble(json.getString("latitude")));
                loc.add(Double.parseDouble(json.getString("longitude")));
                allLocations.add(i, loc);

            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        return allLocations;
    }

}
