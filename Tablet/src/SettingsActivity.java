//  Copyright 2010 Jonathan Steele
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.itnoles.shared.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;
//import android.widget.Button;

import java.util.List;

public class SettingsActivity extends PreferenceActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Add a button to the header list.
		/*if (hasHeaders()) {
			Button button = new Button(this);
			button.setText("Some action");
			setListFooter(button);
        }*/
	}
	
	protected String getVersion()
	{
		String version = "N/A";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
			version = pInfo.versionName;
		} catch (NameNotFoundException e1) {
			Log.e(this.getClass().getSimpleName(), "Name not found", e1);
		}
		return version;
	}
	
	/**
	 * Populate the activity with the top-level headers.
	 */
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
	
	/**
	 * This fragment shows the preferences for the first header.
	 */
	public static class GeneralFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
		private SharedPreferences sharedPref;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.general);
			
			sharedPref = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
		}
		
		@Override
		public void onResume()
		{
			super.onResume();
			// Set up a listener whenever a key changes
			getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onPause()
		{
			super.onPause();
			// Unregister the listener whenever a key changes
			getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		}
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			Preference pref = findPreference(key);
			SharedPreferences.Editor editor = sharedPref.edit();
			if (key.equals("news"))
			{
				ListPreference newsPref = (ListPreference)pref;
				int index = newsPref.findIndexOfValue(newsPref.getValue());
				if (index != -1)
				{
					editor.putString("newstitle", newsPref.getEntries()[index].toString());
					editor.putString("newsurl", newsPref.getEntryValues()[index].toString());
					// Don't forget to apply your edits!!!
					editor.apply();
				}
				getActivity().setResult(RESULT_OK);
			}
		}
	}
	
	/**
	 * This fragment shows the preferences for the second header.
	 */
	public static class AboutFragment extends PreferenceFragment {
		private static final String NON_FOUND = "N/A";
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.about_settings);
			
			setStringSummary("app_version", ((SettingsActivity)getActivity()).getVersion());
			setStringSummary("author_name", "Jonathan Steele");
			setStringSummary("author_email", "xfsunoles@gmail.com");
			setStringSummary("author_website", NON_FOUND);

			findPreference("author_email").setEnabled(true);
			findPreference("author_website").setEnabled(true);
		}
		
		private void setStringSummary(String preference, String value)
		{
			try {
				findPreference(preference).setSummary(value);
			} catch (RuntimeException e) {
				findPreference(preference).setSummary(NON_FOUND);
			}
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			String key = preference.getKey();
			String summary = preference.getSummary().toString();
			if (key.equals("author_email")) {
				final Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[] {summary});
				i.putExtra(Intent.EXTRA_SUBJECT, "App Feedback for " + getResources().getString(R.string.app_name));
				startActivity(Intent.createChooser(i, "Select email application."));
			} else if (key.equals("author_website")) {
				if (!NON_FOUND.equals(summary)) {
					// Take string from url and parse it to the default browsers
					final Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(summary));
					startActivity(viewIntent);
				}
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
	}
}