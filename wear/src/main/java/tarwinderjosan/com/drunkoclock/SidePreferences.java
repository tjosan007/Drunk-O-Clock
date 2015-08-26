package tarwinderjosan.com.drunkoclock;

import android.os.Bundle;

import preference.WearPreferenceActivity;

/**
 * The wearable side preference
 */
public class SidePreferences extends WearPreferenceActivity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        addPreferencesFromResource(R.layout.preferences);
    }
}
