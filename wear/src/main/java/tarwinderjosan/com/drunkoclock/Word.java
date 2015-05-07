package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Watch face with time displayed as words and a phrase.
 * Change-log:
 * 5/1/2015
 */
public class Word extends Style {

    // Handles retrieving of dates and text
    private CustomDateFormatter dateFormatter;
    Typeface[] typeFace;

    // Paint objects hold specifications for the drawing
    private final Paint timePaint;
    // Word between the time
    private final Paint wordPaint;
    // Last saying, could be the weather temp
    private final Paint sayingPaint;
    private final Paint rulePaint;

    {

        // Init
        rulePaint = new Paint();
        sayingPaint = new Paint();
        timePaint = new Paint();
        wordPaint = new Paint();
    }

    /**
     * Main constructor
     */
    public Word(Context context) {
        super(context);
        initialize();

    }


    /**
     * Overridden
     * Called by the WatchFacePainter class to draw
     * @param canvas Canvas to draw on
     * @param rect Rect bounds
     */
    @Override
    protected void drawWatchFace(Canvas canvas, Rect rect) {
        // Class class draw method
        this.draw(canvas, rect, dateFormatter, timePaint, wordPaint, sayingPaint);
    }

    /**
     * Call the method to draw the watch face
     * @param canvas Canvas obj to draw on
     * @param bounds Rect obj linked to the canvas
     * @param dateFormatter CustomDateFormatter used to format dates
     * @param timePaint Used for drawing time
     */
    @Override
    protected void draw(Canvas canvas, Rect bounds, CustomDateFormatter dateFormatter, Paint timePaint, Paint word, Paint sayingPaint) {
        canvas.drawColor(Color.BLACK);
        // Get all the text needed to draw
        String[] textToDraw = dateFormatter.getTimeAccordingTo(this);
        // draw the hour
        float canvasStartX = canvas.getWidth() / 12;
        // ratio for every 320px/15 pixel down
        float canvasStartY = canvas.getHeight() / 3;

        // draw the hr
        canvas.drawText(textToDraw[0],canvasStartX,canvasStartY, timePaint);

        // draw the word, y value is height of all the previous text + orig  y height
        // also 10px padding

        canvas.drawText(textToDraw[1], canvasStartX, canvasStartY + computeYOffset(textToDraw[0], timePaint) + 10.0f, wordPaint);

        // draw the minute, see CustomDateFormatter

        canvas.drawText(textToDraw[2], canvasStartX, canvasStartY + computeYOffset(textToDraw[1], wordPaint) + computeYOffset(textToDraw[0], timePaint ) + 15.0f, timePaint);


        // draw second half if not null
        if(textToDraw[3] != null) {
            canvas.drawText(textToDraw[3], canvasStartX, canvasStartY + computeYOffset(textToDraw[0], timePaint) +
                    computeYOffset(textToDraw[1], wordPaint) + computeYOffset(textToDraw[2], timePaint) + 10.0f, timePaint);
        }
        // draw the last text
        if(textToDraw[4] != null) {
            canvas.drawText(textToDraw[4], canvasStartX, canvas.getHeight() - (canvas.getHeight() / 4), sayingPaint);
        }





    }

    /**
     * Compute the height of the previous text and draw the new text 10px plus
     * the height below
     * @param text
     * @param paint
     * @return
     */
    private float computeYOffset(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();

    }


    @Override
    public Typeface[] getTypeFace() {
        return super.getTypeFace();
    }

    /**
     * Set drawing characteristics dependent on watch state.
     * @param isAmbientMode Boolean holding ambient mode state
     */
    @Override
    protected void setCharacteristics(boolean isAmbientMode) {
        // Ambient mode, change setting to conserve battery
        if(isAmbientMode) {
            timePaint.setAntiAlias(false);
            wordPaint.setAntiAlias(false);
            sayingPaint.setAntiAlias(false);
            timePaint.setColor(Color.GRAY);
            wordPaint.setColor(Color.GRAY);
            sayingPaint.setColor(Color.GRAY);
        } else {
            timePaint.setAntiAlias(true);
            wordPaint.setAntiAlias(true);
            sayingPaint.setAntiAlias(true);
            timePaint.setColor(Color.WHITE);
            wordPaint.setColor(Color.WHITE);
            sayingPaint.setColor(Color.WHITE);
        }
    }


    private void initialize() {
        typeFace = super.getTypeFace();
        // Set the watch face style
        super.getDateFormatter().setWatchFaceLook(WatchFaceLook.WORD);
        // get the object for this class to use
        dateFormatter = super.getDateFormatter();
        // Set Paint object for drawing the time
        timePaint.setColor(Color.WHITE);
        timePaint.setAntiAlias(true);

        float size = getHeight() * (3.0f / 32.0f);
        timePaint.setTextSize(size);
        timePaint.setTypeface(getTypeFace()[0]);
        // Set the Paint object for the word paint
        wordPaint.setColor(Color.WHITE);
        wordPaint.setTextSize(size);
        wordPaint.setTypeface(getTypeFace()[1]);
        wordPaint.setAntiAlias(true);

        // Set the Paint object for the saying
        float size2 = getHeight() / 16;
        sayingPaint.setColor(Color.WHITE);
        sayingPaint.setAntiAlias(true);
        sayingPaint.setTextSize(getHeight() / 16.0f);
        sayingPaint.setTypeface(getTypeFace()[0]);
        // Set the Paint object for drawing hor. rule
        rulePaint.setColor(Color.WHITE);
        rulePaint.setAntiAlias(true);
        rulePaint.setStrokeWidth(5f);
    }

    public float getHeight() {
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return (float)metrics.heightPixels;
    }

    @Override
    public CustomDateFormatter getDateFormatter() {
        return super.getDateFormatter();
    }
}
