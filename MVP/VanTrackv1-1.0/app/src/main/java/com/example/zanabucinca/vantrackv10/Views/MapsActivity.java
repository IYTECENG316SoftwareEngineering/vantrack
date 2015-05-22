package com.example.zanabucinca.vantrackv10.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Services.ConnectionChangeReceiver;
import com.example.zanabucinca.vantrackv10.Presenters.MapsActivityPresenter;
import com.example.zanabucinca.vantrackv10.Presenters.MapsActivityPresenterImpl;
import com.example.zanabucinca.vantrackv10.R;
import com.example.zanabucinca.vantrackv10.Services.LocationsUpdateService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.widget.Toast;

import java.util.List;

public class MapsActivity extends FragmentActivity implements MapsView{
    public static final String DEFAULT_UPDATE_INTERVAL = "15000";
    private String LOG_TAG = "Maps Activity";
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Marker myMarker;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
   // private MapReloadReceiver reloadReceiver = new MapReloadReceiver();
    private InternetConnectionReceiver connectionReceiver = new InternetConnectionReceiver();
    private LatLng previousTarget ;
    private float previousZoomLevel =14;
    private long currentTimeLong;
    Boolean isConnected = true;

    public static MapsActivityPresenter mapsActivityPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mapsActivityPresenter = new MapsActivityPresenterImpl(this,getApplicationContext());

        // Get LocationManager object from System Service LOCATION_SERVICE
       // LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Bundle extras=getIntent().getExtras();
        mapsActivityPresenter.getExtras(extras);

       /* allInfo=(ArrayList<ArrayList<String>>) extras.getSerializable("info");

        newUser=(User) extras.getSerializable("newUser");
        apiConnector = new ApiConnector(getApplicationContext(),newUser);

        Log.d("info",allInfo.toString());
        Log.d("id", newUser.getId());*/

        currentTimeLong = System.currentTimeMillis();
        mapsActivityPresenter.setDefaultLocation();//default yerler dolmuslari icin gecti gecmedi olayi

        startInitGpsCall();
        //Log.d("first Location ","latitude"+newUser.getLatitude()+"longititude"+newUser.getLongitude());



        mapsActivityPresenter.startActionUpdateAlarm();

        previousTarget = new LatLng(mapsActivityPresenter.getNewUser().getLatitude(), mapsActivityPresenter.getNewUser().getLongitude());
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

    }
    @Override
    protected void onResume() {

        super.onResume();
        setUpMapIfNeeded();
        IntentFilter intentFilterForMap = new IntentFilter(LocationsUpdateService.ACTION_UPDATE_COMPLETED);
     //   this.registerReceiver(reloadReceiver, intentFilterForMap);
        IntentFilter intentFilterForInternet = new IntentFilter(ConnectionChangeReceiver.ACTION_INTERNET_CONNECTION);
        this.registerReceiver(connectionReceiver, intentFilterForInternet);
    }
    @Override
    public void onPause() {
        super.onPause();
       // this.unregisterReceiver(reloadReceiver);
        this.unregisterReceiver(connectionReceiver);
    }
    @Override
    protected void onStart() {
        super.onStart();
        stopGpsCall();
        startGpsCallBack();
        mapsActivityPresenter.setServiceAlarm();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapsActivityPresenter.turnOffAlarm();
        stopGpsCall();

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    @Override
    public void setUpMap() {

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.clear();
        Log.d(LOG_TAG,"setUpMap");
        for(List<String> row: mapsActivityPresenter.setUpMap()) {
            if(row.get(7).equals("missed van")) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(row.get(5)), Double.parseDouble(row.get(6))))
                        .title(row.get(2)).snippet("Missed")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            }

            else if(row.get(7).equals("comming van")) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(row.get(5)), Double.parseDouble(row.get(6))))
                        .title(row.get(2))
                        .snippet(mapsActivityPresenter.calculateTime(row.get(5),row.get(6),row.get(4)))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }
            else if(row.get(7).equals("public one")) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(row.get(5)), Double.parseDouble(row.get(6))))
                        .title(row.get(2)).snippet("Someone")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

            }
            else if(row.get(7).equals("van")) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(row.get(5)), Double.parseDouble(row.get(6))))
                        .title(row.get(2))
                        .snippet("")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            }
            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(previousTarget));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(previousZoomLevel));
            if(myMarker != null)
                myMarker.remove();
            myMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(mapsActivityPresenter.getNewUser().getLatitude(), mapsActivityPresenter.getNewUser().getLongitude()))
                    .title(//"You are here! \nyour speed : " + newUser.getSpeed() +
                            //  "latitude : " + mapsActivityPresenter.getNewUser().getLatitude() +
                          //  "longtitude : " + mapsActivityPresenter.getNewUser().getLongitude()
                            "You are here")
                    .snippet("your route is "+mapsActivityPresenter.getNewUser().getRoute()));
                    //.snippet("Route: " + mapsActivityPresenter.getNewUser().getRoute()+"   Speed: "+ mapsActivityPresenter.getNewUser().getSpeed()));

         //   myMarker.remove();
            mMap.setOnCameraChangeListener(getCameraChangeListener());
        }


    }

    private GoogleMap.OnCameraChangeListener getCameraChangeListener(){
        return new GoogleMap.OnCameraChangeListener()
        {
            @Override
            public void onCameraChange(CameraPosition position)
            {
                previousTarget = position.target;
                previousZoomLevel = position.zoom;
            }
        };
    }


    private class InternetConnectionReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, final Intent intent) {

            Bundle extras = intent.getExtras();
            isConnected = extras.getBoolean("isConnected");
            Log.d(LOG_TAG, "internet connection is : "+isConnected);

                if (isConnected) {
                    String type = extras.getString("type");
                    if(type!=null) {
                        Log.d(LOG_TAG, type);
                        //   type.contains("mobile")
                        if ((type.contains("mobile") && mapsActivityPresenter.getNewUser().getIsUse3g()) || type.equals("WIFI")) {
                            setUpMap();
                            LocationsUpdateService.startActionUpdate(MapsActivity.this);
                        } else if (type.equals("mobile") && !mapsActivityPresenter.getNewUser().getIsUse3g()) {
                            Toast.makeText(context, "Your 3g preference is disabled. Please enable it continue.", Toast.LENGTH_LONG).show();
                            mapsActivityPresenter.turnOffAlarm();
                            mMap.clear();
                        }
                        Log.d(LOG_TAG, type);
                    }
                } else {
                    Toast.makeText(context, "There is no internet connection.You can only see your own location.", Toast.LENGTH_LONG).show();
                    mapsActivityPresenter.turnOffAlarm();
                    mMap.clear();
                }


        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                long differenceTime = System.currentTimeMillis() - currentTimeLong ;
                currentTimeLong = System.currentTimeMillis();
                mapsActivityPresenter.setSpeed(location.getLatitude(), location.getLongitude(), differenceTime);
                mapsActivityPresenter.setNewUserLocation(location);

                //   previousTarget = new LatLng(mapsActivityPresenter.getNewUser().getLatitude(), mapsActivityPresenter.getNewUser().getLongitude());
               // Log.d("findik speeeed:",""+newUser.getSpeed());
                if(isConnected)
                   mapsActivityPresenter.updateNewUser();
                Log.d(LOG_TAG,"locationChanged");
                //Log.d("findik longtitude--latitude",""+location.getLongitude()+"--"+location.getLatitude());
                if(myMarker!=null&&mMap!=null) {
                  //  Log.d("remove","mymarker"+myMarker.getPosition().latitude+" "+myMarker.getPosition().longitude);
                    myMarker.remove();
                    myMarker=mMap.addMarker(new MarkerOptions().position(new LatLng(mapsActivityPresenter.getNewUser().getLatitude(), mapsActivityPresenter.getNewUser().getLongitude()))
                            .title(//"You are here! \nyour speed : " + newUser.getSpeed() +
                                    //  "latitude : " + mapsActivityPresenter.getNewUser().getLatitude() +
                                    //  "longtitude : " + mapsActivityPresenter.getNewUser().getLongitude()
                                    "You are here")
                            .snippet("your route is "+mapsActivityPresenter.getNewUser().getRoute()));
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
           /* Toast.makeText(MapsActivity.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_SHORT).show();*/

        }

        @Override
        public void onProviderEnabled(String provider) {
            /*Toast.makeText(MapsActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();*/

        }

        @Override
        public void onProviderDisabled(String provider) {
          /*  Toast.makeText(MapsActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();*/
            finish();
        }
    }

    private void startInitGpsCall(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
       // Log.d(LOG_TAG,"lc "+location);
        if (location != null) {
            mapsActivityPresenter.setNewUserLocation(location);
            locationListener.onLocationChanged(location);
        }
        else{
            Log.e(LOG_TAG,"There are some problems about gps");
            locationListener.onLocationChanged(location);
        }
    }

    private void stopGpsCall(){
        if (locationManager != null) {
            Log.d(LOG_TAG,"stopGps");
            locationManager.removeUpdates(locationListener);
        }
    }

    private void startGpsCallBack(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
      //  Log.d(LOG_TAG,"loccc "+location);
        if(location==null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            mapsActivityPresenter.setNewUserLocation(location);
            locationListener.onLocationChanged(location);
        }
        else{
            Log.e(LOG_TAG,"There are some problems about gps");
        }
    }


}

