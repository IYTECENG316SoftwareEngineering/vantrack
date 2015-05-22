package com.example.zanabucinca.vantrackv10.Views;

import android.preference.PreferenceActivity;

import com.example.zanabucinca.vantrackv10.R;

import java.util.List;

/**
 * Created by zanabucinca on 10/05/15.
 */
public class FragmentPreferencesActivity extends PreferenceActivity {
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@Override
	protected boolean isValidFragment (String fragmentName) {
		if(UserPreferenceFragment.class.getName().equals(fragmentName)
				|| PrivacyPreferenceFragment.class.getName().equals(fragmentName))
			return true;
		else
			return false;
	}
}
