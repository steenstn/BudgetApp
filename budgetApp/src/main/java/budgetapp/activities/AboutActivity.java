package budgetapp.activities;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import budgetapp.fragments.VersionNameExplanationFragment;
import budgetapp.main.R;

public class AboutActivity
    extends FragmentActivity {

    private String version = "";
    private String versionName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        try {
            version = "Version " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            version = "Unknown version";
        }

        versionName = getString(R.string.versionName);

        TextView versionTextView = (TextView) findViewById(R.id.textViewVersion);
        versionTextView.setText(version + " (" + versionName + ")");
    }

    public void showExplanation(View v) {
        DialogFragment newFragment = new VersionNameExplanationFragment();
        newFragment.show(getSupportFragmentManager(), "show_explanation");
    }
}
