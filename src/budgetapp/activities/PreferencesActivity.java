package budgetapp.activities;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;
import budgetapp.main.R;
import budgetapp.models.BudgetModel;

public class PreferencesActivity
    extends PreferenceActivity /* implements OnSharedPreferenceChangeListener */{

    private BudgetModel model;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        model = new BudgetModel(this);
        setUpListeners();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressWarnings("deprecation")
    private void setUpListeners() {

        PreferenceScreen currencies = (PreferenceScreen) findPreference("currencies");
        currencies.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(PreferencesActivity.this, CurrenciesActivity.class);
                startActivity(intent);
                return true;
            }
        });

        PreferenceScreen categories = (PreferenceScreen) findPreference("manageCategories");
        categories.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference arg0) {
                Intent intent = new Intent(PreferencesActivity.this, CategoriesActivity.class);
                startActivity(intent);
                return true;
            }
        });

        PreferenceScreen clearAutocomplete = (PreferenceScreen) findPreference("clearAutocompleteValues");
        clearAutocomplete.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference arg0) {
                model = new BudgetModel(getBaseContext());
                model.clearAutocompleteValues();
                Toast.makeText(getBaseContext(), "Auto-complete values cleared", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        PreferenceScreen feedback = (PreferenceScreen) findPreference("sendFeedback");
        feedback.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference arg0) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "alest849@gmail.com" });
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "MrCashManager feedback");

                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(emailIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not find any email application to use.",
                        Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });

        PreferenceScreen rateApp = (PreferenceScreen) findPreference("rateApp");
        rateApp.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference arg0) {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return true;
            }
        });

        PreferenceScreen about = (PreferenceScreen) findPreference("about");
        about.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference arg0) {
                Intent intent = new Intent(PreferencesActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            }
        });
        /*
         * PreferenceScreen backup = (PreferenceScreen) findPreference("manageBackup");
         * 
         * backup.setOnPreferenceClickListener(new OnPreferenceClickListener() {
         * 
         * @Override public boolean onPreferenceClick(Preference arg0) { Intent intent = new
         * Intent(PreferencesActivity.this,BackupActivity.class); startActivity(intent); return true; }
         * 
         * });
         * 
         * getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
         */
    }
    /*
     * public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
     * 
     * if(key.equalsIgnoreCase("backupFolder")) { EditTextPreference backupFolder = (EditTextPreference)
     * findPreference("backupFolder"); backupFolder.setSummary(preferences.getString("backupFolder",
     * Environment.getExternalStorageDirectory().getPath()));
     * 
     * }
     * 
     * }
     */

}
