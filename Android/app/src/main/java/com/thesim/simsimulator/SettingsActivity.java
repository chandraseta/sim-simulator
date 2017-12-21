package com.thesim.simsimulator;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

  private static final String LOG_TAG = SettingsActivity.class.getSimpleName();
  private SharedPreferences mSharedPreferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mSharedPreferences = this.getSharedPreferences("UserPreferences", MODE_PRIVATE);
    SharedPreferences.Editor editor = mSharedPreferences.edit();
    ListPreference listPreference = (ListPreference) findPreference("list_preferences");
    String currValue = listPreference.getValue();
    editor.putString("QuestionAmount", currValue);
    editor.apply();

    mSharedPreferences = this.getSharedPreferences("UserPreferences", MODE_PRIVATE);
    Log.d(LOG_TAG, "Question Amount: " + mSharedPreferences.getString("QuestionAmount", "Shared Prefs Not Set"));
  }
}
