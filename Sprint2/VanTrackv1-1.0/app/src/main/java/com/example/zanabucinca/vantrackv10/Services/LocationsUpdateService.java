package com.example.zanabucinca.vantrackv10.Services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.zanabucinca.vantrackv10.Views.MapsActivity;

//alarm class
public class LocationsUpdateService extends IntentService {

	public static final String ACTION_UPDATE ="com.example.zanabucinca.vantrackv10.ACTION_UPDATE";
	public static final String ACTION_UPDATE_COMPLETED ="com.example.zanabucinca.vantrackv10.ACTION_UPDATE_COMPLETED";
	private static final String LOG_TAG = "LocationsUpdateService";

	public LocationsUpdateService(){
		super("LocationsUpdateService");
	}

	public static void startActionUpdate(Context context) {

		Intent intent = new Intent(context, LocationsUpdateService.class);
		intent.setAction(ACTION_UPDATE);
		context.startService(intent);
		Log.d(LOG_TAG, "Start service  "+context.startService(intent));
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(LOG_TAG, "Start Action");
		if (intent != null) {
			final String action = intent.getAction();
			if (ACTION_UPDATE.equals(action)) {
				handleActionUpdate();
			}
		}

	}
    // setting alarms
	public static void setServiceAlarm(Context context, int updateInterval) {
		Intent intent = new Intent(context, LocationsUpdateService.class);
		intent.setAction(ACTION_UPDATE);
		PendingIntent pi = PendingIntent.getService(context, 0, intent, 0);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Log.d(LOG_TAG, "Setting Service Alarm: " + updateInterval + " msec.");
		alarmManager.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + updateInterval, updateInterval, pi);
	}
    // checking alarm is on or of
	public static boolean isServiceAlarmOn(Context context) {
		Intent intent = new Intent(context, LocationsUpdateService.class);
		intent.setAction(ACTION_UPDATE);
		PendingIntent pi = PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		return pi != null;
	}
    // turn of alarm
    public static void unsetServiceAlarm(Context context) {
        Intent intent = new Intent(context, LocationsUpdateService.class);
        intent.setAction(ACTION_UPDATE);
        PendingIntent pi = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE);

        if (pi != null) {
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            Log.i(LOG_TAG, "Turn of Service Alarm");
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
    //if time is up, maps should be reload beacuse of this we take all users from db again and warn the mapsActivityPresenter
	private void handleActionUpdate() {
      //  Log.d(LOG_TAG,"handlerActionUpdate");
        if(MapsActivity.mapsActivityPresenter !=null)
            MapsActivity.mapsActivityPresenter.toStartGetAllUsersTask();

	}
}
