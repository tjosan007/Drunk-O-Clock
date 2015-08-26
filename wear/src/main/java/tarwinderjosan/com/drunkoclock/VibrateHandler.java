package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.os.Vibrator;

/**
 * Just used for vibrations
 */
public class VibrateHandler {
    Vibrator mVibrator;
    Context mContext;
    long mCurrentTime;
    public VibrateHandler(Context context) {
        mContext = context;
        mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrateQuick() {
        // Start without a delay
        // Vibrate for 100 milliseconds
        // Sleep for 1000 milliseconds
        long[] pattern = {0, 100, 100, 100, 100};
        mVibrator.vibrate(pattern, -1);

    }
}
