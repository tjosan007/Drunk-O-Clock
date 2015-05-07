package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

/**
 * Main Watch Face class. Handles the drawing of different watch faces.
 *
 * Change-log:
 * 4/29/2015
 * - Removed all reference to Date and SimpleDateFormat.
 * Delegation to the wrapper class CustomDateFormatter will handle all the tasks involved.
 * Cleaned up obsolete methods
 * 4/30/2015
 * - Tidying up the class a bit, creating parent class Style and separate children classes
 * to represent different styles.
 * Deprecated methods setFont and getHeightNeeded(String string)
 * 5/1/2015
 * Vacuumed pack the class some more.
 * Removed references to all fonts, this is handled by the classes themselves now
 * Removed context reference, cleaned up getInstance() method
 * Cleaned up constructor, removed WatchFaceLook pointer.
 *
 * Future changes to do:
 * ✓ Clean up this class and create a separate class to handle the drawing for each type of watch face look.
 * Some how write persistent data to avoid customizations being lost on a reset/crash
 *
 */
public class WatchFacePainter {


    // Debug string
    private final String TAG = "WATCH FACE PAINER LOG:";

    // ***************** DEBUGGING ABOVE ********************* //



    private Context context;
    boolean isAmbientMode;
    // Hold the current style object used for drawing
    Style currentStyle;
    // Shape representing the device type (square, round, moto-round)
    Shape shape;


    /**
     * Default watch face constructor.
     * @param style Current style to draw.
     */
    public WatchFacePainter(Style style, Context context) {
        this.context = context;
        // Set overall font of the watch face
        currentStyle = style;

    }

    /**
     * Method for drawing the watch face.
     * Called every second.
     * (On second thought, wouldn't it be better to call this every minute since the seconds are not being displayed at all?
     * Come back on that later)
     * @param canvas
     * @param bounds
     */
    public void draw(Canvas canvas, Rect bounds) {

        // Update the date each draw to reflect the current date/time
        currentStyle.getDateFormatter().updateDate();
        // Draw watch face w/ current style
        currentStyle.drawWatchFace(canvas, bounds);

    }

    /**
     * Initialize the default watch face.
     * Displays the month, day followed by a horizontal rule
     * and the time in 24 hr format.
     * @return MainWatchFace instance
     */
    public static WatchFacePainter getMainInstance(Context context) {
        Style styler = new Plain(context);
        // Set the context in the super class (used for fonts)
        return new WatchFacePainter(styler, context);

    }

    public Style getCurrentStyle() {
        return currentStyle;
    }
}
