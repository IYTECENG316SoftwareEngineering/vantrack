package com.example.zanabucinca.vantrackv10.Presenters;

/**
 * Created by Yasin on 14.5.2015.
 */
public interface MainActivityPresenter extends ActivityPresenter {

    public boolean createUser();
    public boolean checkInternetCondition();
    public boolean setRouteOnUser(String route);
    public void toStartMapsActivity();
    public boolean deleteNewUser();
    public boolean isGpsOn();
	public void turnOnGps();

}
