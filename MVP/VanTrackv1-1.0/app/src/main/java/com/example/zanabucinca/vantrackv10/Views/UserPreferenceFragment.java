package com.example.zanabucinca.vantrackv10.Views;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.zanabucinca.vantrackv10.R;

public class UserPreferenceFragment extends PreferenceFragment {

	public static final String PREFERENCE_MODE = "PREF_MODE";
	public static final String PREFERENCE_USE_3G     = "PREF_USE_3G";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.user_preferences);
	}

}