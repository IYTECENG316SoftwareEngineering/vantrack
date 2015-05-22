package com.example.zanabucinca.vantrackv10.Views;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.zanabucinca.vantrackv10.R;

public class PrivacyPreferenceFragment extends PreferenceFragment {

	public static final String PREFERENCE_PUBLIC_LOCATION = "PREF_PUBLIC";


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.privacy_preferences);
	}

}