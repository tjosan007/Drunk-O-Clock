package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Handles interactions with the class Preferences file.
 */
public class PreferencesHandler {

    private static Context context;
    // Static because there should be one instance of the SharedPreferences class
    // shared by both the watch face and configuration activity
    private static SharedPreferences preferences;
    public PreferencesHandler(Context context) {
        this.context = context;
    }

    /**
     * Initialize and return the SharedPreferences object of this app.
     * @return preferences object
     */
    public static SharedPreferences initializeSharedPreference() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences;
    }

    /**
     * Return the static SharedPreferences object, previously initialized.
     * Must have called initializeSharedPreference() method before!
     */
    public static SharedPreferences retrieve() {
        return preferences;
    }
}
