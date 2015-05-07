package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * The plain style which displays the date in month/day format,
 * followed by HR and the time in 24-hr format.
 *
 * Change-log:
 * Font handling is done in Style class now
 * Date: 4/30/2015
 */
public class Plain extends Style {

    Typeface[] typeFace;
    CustomDateFormatter dateFormatter;

    // Paint objects hold specifications for the drawing
    private final Paint rulePaint;
    private final Paint datePaint;
    private final Paint timePaint;
    {
        rulePaint = new Paint();
        datePaint = new Paint();
        timePaint = new Paint();
    }

    // Initialize the Plain class and the Paint objects.
    public Plain(Context context) {
        super(context);
        // Get type face from super class
        typeFace = super.getTypeFace();
        super.getDateFormatter().setWatchFaceLook(WatchFaceLook.PLAIN);
        dateFormatter = super.getDateFormatter();
        // Set Paint object for drawing the date
        datePaint.setColor(Color.WHITE);
        datePaint.setAntiAlias(true);
        datePaint.setTextSize(25);
        datePaint.setTypeface(getTypeFace()[1]);
        // Set the Paint object for drawing hor. rule
        rulePaint.setColor(Color.WHITE);
        rulePaint.setAntiAlias(true);
        rulePaint.setStrokeWidth(2f);
        // Set the Paint object for drawing the time
        timePaint.setColor(Color.WHITE);
        timePaint.setAntiAlias(true);
        timePaint.setTextSize(50);
        timePaint.setTypeface(getTypeFace()[1]);
    }

    /**
     * Draw watch face method common to every style.
     *
     * @param canvas Canvas to draw on
     * @param rect Rect bounds
     */
    @Override
    protected void drawWatchFace(Canvas canvas, Rect rect) {
        this.draw(canvas, rect, dateFormatter, rulePaint, datePaint, timePaint);
    }

    /**
     * Represents drawing the watch style.
     * @param canvas Canvas obj to draw on
     * @param bounds Rect obj linked to the canvas
     */
    @Override
    protected void draw(Canvas canvas, Rect bounds, CustomDateFormatter dateFormatter, Paint rulePaint, Paint datePaint, Paint timePaint) {
        // Starting points
        int canvasStartX = canvas.getWidth() / 9;
        int canvasStartY = canvas.getHeight() - (canvas.getWidth() / 2);
        // Ending points
        int canvasStopX = canvas.getWidth() - canvas.getWidth() / 9;
        int canvasStopY = canvasStartY;

        // Where the magic happens
        // Fill with black the background
        canvas.drawColor(Color.BLACK);

        /* Draw the HR a bit below the center
        param 1 and 2
        The starting point for the line is somewhere near the left side of the screen (X val init)
        the starting point for the height is in the middle of the screen
        param 2 and 3
        the line stretches until near the end of the watch face border, same pixel padding kept between the starting point and ending
        the height remains the name to make it horizontal
         */
        canvas.drawLine(canvasStartX, canvasStartY, canvasStopX,
                canvasStartY, rulePaint);

        // Draw the text starting with the date above the horizontal rule
        canvas.drawText(dateFormatter.getDateAccordingTo(), (float)canvasStartX, (float)canvasStartY - 15, datePaint);
        // Draw the time below the HR in 24 hr format
        String[] string = dateFormatter.getTimeAccordingTo(this);

        /* Update to deal with the text height
        Notice: When Y is at canvasStartY, the text is drawn right on top of the line.
        30 pixels added would shift it down making the top of the text touch the bottom line,
        therefore 30 extra pixels must be added
         */

        if(string.length == 1)
            canvas.drawText(string[0], (float)canvasStartX, (float)canvasStartY + 55, timePaint);
      }

    /**
     * Get typeface from super class
     * @return
     */
    @Override
    public Typeface[] getTypeFace() {
        return super.getTypeFace();
    }

    public CustomDateFormatter getDateFormatter() {
        return super.getDateFormatter();
    }
    /**
     * Set drawing characteristics dependent on watch state.
     * @param isAmbientMode Boolean holding ambient mode state
     */
    @Override
    protected void setCharacteristics(boolean isAmbientMode) {
        super.setCharacteristics(isAmbientMode);
        // Ambient mode, change setting to conserve battery
        if(isAmbientMode) {
            timePaint.setAntiAlias(false);
            datePaint.setAntiAlias(false);
            timePaint.setColor(Color.GRAY);
            datePaint.setColor(Color.GRAY);
        } else {
            timePaint.setAntiAlias(true);
            datePaint.setAntiAlias(true);
            timePaint.setColor(Color.WHITE);
            datePaint.setColor(Color.WHITE);
        }
    }
 }

