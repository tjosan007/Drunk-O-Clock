package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * This class represents the types of styles  watch face.
 * It is the superclass to all look and feels.
 * has methods and fields which are similar to all classes.
 * Ir also holds descriptors familiar to all watch face looks.
 *
 * Change-log:
 * Date: 4/30/2015
 * Fonts implemented
 * Removed all instances of CustomDateFormatter out of other classes and placed it here
 * Classes can access the public object by using the getter associated
 * aka encapsulation Mr. Wilhelm.
 *
 */
public class Style {


    // Link to the phrase book
    private PhraseBook phraseBook;
    // Handles retrieving of dates and text
    private CustomDateFormatter dateFormatter;
    // Context for font grabbing
    private Context context;
    // Create and load fonts
    private Typeface[] typeFaces;


    public Style(Context context) {
        this.context = context;
        dateFormatter = new CustomDateFormatter();
        phraseBook = new PhraseBook(context);
        typeFaces = new Typeface[]{Typeface.createFromAsset(context.getAssets(), "gotham_light_34.ttf"),
        Typeface.createFromAsset(context.getAssets(), "gotham_bold_36.ttf")};
    }

    /**
     * Method calls the specific watch face classes draw method
     * @param canvas Canvas to draw on
     * @param rect Rect bounds
     */
    protected void drawWatchFace(Canvas canvas, Rect rect) {}



    /**
     * Method inherited by every subclass.
     * Demonstrates method overriding aka static polymorphism
     * @param canvas Canvas obj to draw on
     * @param bounds Rect obj linked to the canvas
     * @param dateFormatter CustomDateFormatter used to format dates
     * @param paint1 Paint object
     * @param paint1 Paint object
     * @param paint1 Paint object
     */
    protected void draw(Canvas canvas, Rect bounds, CustomDateFormatter dateFormatter, Paint paint1, Paint paint2, Paint paint3) {}


    /**
     * Get Typeface array used for drawing the fonts.
     * @return typeFace array
     */
    public Typeface[] getTypeFace() { return typeFaces; }

    /**
     * Set watch face characteristics depending on the current state.
     * @param isAmbientMode Boolean holding ambient mode state     */
    protected void setCharacteristics(boolean isAmbientMode) {}

    /**
     * Getting the date formatter
     */
    public CustomDateFormatter getDateFormatter() {
        return dateFormatter;
    }

    /**
     * Set the context
     * @param context The context
     */
    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {return context;}
    public PhraseBook getPhraseBook() {return phraseBook;}

}
