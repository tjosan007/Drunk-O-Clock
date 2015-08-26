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

import utils.DebugManager;

/**
 * Watch face with time displayed as words and a phrase.
 */
public class WordWatchFace {

    // The alignment of the watch face
    Paint.Align mTextAlignment;

    // Member variables
    // Link to the phrase book
    private PhraseBook mPhraseBook;
    // Handles retrieving of dates and text
    private CustomWordDateFormatter mDateFormatter;
    // Context for font grabbing
    private Context mContext;
    // Create and load fonts
    private Typeface[] typeFaces;
    // Link to the main engine. used by the PhraseBook to invalidate
    private WordWatchFaceService.MainEngine mMainEngine;
    private WordPack mWordPack;

    private float mMainTextSize;
    private float mDeviceDensity;

    // Holds true if the device is a round device
    private boolean mIsRound;
    Rect[] mSubSections;


    // Paint objects hold specifications for the drawing
    private Paint mHourPaint;
    // Word between the time
    private Paint mWordPaint;
    // Last saying, could be the weather temp
    private Paint mSayingPaint;
    private Paint mMinutePaint;

    private String mBackgroundColor;
    private Boundary boundary;

    {
        mMinutePaint = new Paint();
        mSayingPaint = new Paint();
        mHourPaint = new Paint();
        mWordPaint = new Paint();
    }


    /**
     * Main constructor
     * @param wordPack Enum holding word pack
     * @param context The context of the application
     * @param mainEngine A reference to the main engine class
     * @param isRound Boolean indicating if the device is round
     */
    public WordWatchFace(WordPack wordPack, Context context, WordWatchFaceService.MainEngine mainEngine, boolean isRound,
                         Paint.Align alignment) {
        mTextAlignment = alignment;
        mMainEngine = mainEngine;
        mWordPack = wordPack;
        mContext = context;
        mIsRound = isRound;
        boundary = new Boundary(context, isRound, 5);
        mSubSections =  boundary.getSubSections();

        // Initialize the useful variables such as fonts
        initialize();

    }

    /**
     * Call the method to draw the watch face
     * @param canvas Canvas obj to draw on
     * @param bounds Rect obj linked to the canvas
     */
    protected void draw(Canvas canvas, Rect bounds) {

        if(mIsRound) {
            // Device is round, draw with bounds
            drawRound(canvas, bounds);
        } else {
            // Device is square, draw with bounds
            //drawSquare(canvas, bounds);
            drawSquare(canvas, bounds);
        }
    }

    /**
     * Set drawing characteristics dependent on watch state.
     * @param isAmbientMode Boolean holding ambient mode state
     */
    protected void setCharacteristics(boolean isAmbientMode) {
        // Ambient mode, change setting to conserve battery
        if(isAmbientMode) {
            mHourPaint.setAntiAlias(false);
            mWordPaint.setAntiAlias(false);
            mSayingPaint.setAntiAlias(false);
            mMinutePaint.setAntiAlias(false);

            mHourPaint.setColor(Color.GRAY);
            mWordPaint.setColor(Color.GRAY);
            mSayingPaint.setColor(Color.GRAY);
            mMinutePaint.setColor(Color.GRAY);
            mBackgroundColor="#000000";
        } else {
            mHourPaint.setAntiAlias(true);
            mWordPaint.setAntiAlias(true);
            mSayingPaint.setAntiAlias(true);
            mMinutePaint.setAntiAlias(true);

            mHourPaint.setColor(Color.WHITE);
            mWordPaint.setColor(Color.WHITE);
            mSayingPaint.setColor(Color.WHITE);
            mMinutePaint.setColor(Color.WHITE);
            mBackgroundColor="#000000";


        }
    }


    /**
     * Just default initialization, such as font size
     * and bounds rect and the phrase book.
     */
    private void initialize() {

        mBackgroundColor="#000000";
        mDeviceDensity = getDisplayMetrics().density;
        // The main text size to use
        mMainTextSize = mDeviceDensity * 35 ;

        mDateFormatter = new CustomWordDateFormatter(this);
        // Initialize the Phrases. Pass in the appropriate word pack retrieved from the preferences in the main class
        // Initialize the typefaces
        mPhraseBook = new PhraseBook(mContext, this, mMainEngine, mWordPack);

        typeFaces = new Typeface[]{Typeface.createFromAsset(mContext.getAssets(), "gotham_light_34.ttf"),
                Typeface.createFromAsset(mContext.getAssets(), "gotham_bold_36.ttf")};
        // Set Paint object for drawing the time
        mHourPaint.setColor(Color.WHITE);
        mHourPaint.setAntiAlias(true);
        mHourPaint.setTextSize(mMainTextSize);
        mHourPaint.setTypeface(getTypeFace()[0]);

        mMinutePaint.setColor(Color.WHITE);
        mMinutePaint.setAntiAlias(true);
        mMinutePaint.setTextSize(mMainTextSize);
        mMinutePaint.setTypeface(getTypeFace()[0]);

        mWordPaint.setColor(Color.WHITE);
        mWordPaint.setFakeBoldText(true);
        mWordPaint.setTextSize(mMainTextSize);
        mWordPaint.setTypeface(getTypeFace()[1]);
        mWordPaint.setAntiAlias(true);

        mSayingPaint.setColor(Color.WHITE);
        mSayingPaint.setAntiAlias(true);
        mSayingPaint.setTextSize(mDeviceDensity*45);
        mSayingPaint.setTypeface(getTypeFace()[0]);

    }


    /**
     * A separate option for round devices
     * Draw within the bounds of a square/rectangular device
     * @param canvas The canvas to dr0aw on
     * @param rect The width and height of the Rectangle to remain in
     */
    public void drawRound(Canvas canvas, Rect rect) {
        // Reset to reg values, they are changed in their adjustSaying or adjustMinute size methods
        mSayingPaint.setTextSize(mDeviceDensity * 45);
        mMinutePaint.setTextSize(mMainTextSize);

        // Get text to draw. Pass in the text to drawRectText method which draws it in the
        // specified rectangle with respect to the specified alignment.
        String[] textToDraw = mDateFormatter.getTimeAccordingTo();
        for(String x : textToDraw) {
            DebugManager.print("TAG", "Value: " + x);
        }
        // Static background color
        canvas.drawColor(Color.parseColor(mBackgroundColor));

        // The main canvas Rect is split into 4 Rect objects to
        // hold the different styles of text. Each Rect object
        // holds its own text.
        boundary.drawRectText(textToDraw[0], canvas, mSubSections[1], mHourPaint, mTextAlignment);
        boundary.drawRectText(textToDraw[1], canvas, mSubSections[2], mWordPaint, mTextAlignment);

        if (textToDraw[3] == null)
            boundary.drawRectText(textToDraw[2], canvas, mSubSections[3], mMinutePaint, mTextAlignment);
        else {
            adjustMinuteTextRound(mMinutePaint, textToDraw[2] + "-" + textToDraw[3]);
            boundary.drawRectText(textToDraw[2] + "-" +  textToDraw[3], canvas, mSubSections[3], mMinutePaint, mTextAlignment);
            }

        adjustSayingTextRound(mSayingPaint, textToDraw[4]);
        boundary.drawRectText(textToDraw[4], canvas, mSubSections[4], mSayingPaint, mTextAlignment);
    }

    /**s
     * Draw on the canvas. The canvas is split up into 5 different subsections used for drawing.
     * This is used for square devices
     * @param canvas The canvas
     * @param rect The rect
     */
    public void drawSquare(Canvas canvas, Rect rect) {
        mSayingPaint.setTextSize(mDeviceDensity * 45);
        mMinutePaint.setTextSize(mMainTextSize);

        String[] textToDraw = mDateFormatter.getTimeAccordingTo();

        // Static background color
        canvas.drawColor(Color.parseColor(mBackgroundColor));
        // The main canvas Rect is split into 5 Rect objects to
        // hold the different styles of text. Each Rect object
        // holds its own text.
        boundary.drawRectText(textToDraw[0], canvas, mSubSections[1], mHourPaint, mTextAlignment);
        boundary.drawRectText(textToDraw[1], canvas, mSubSections[2], mWordPaint, mTextAlignment);
        if (textToDraw[3] == null)
            boundary.drawRectText(textToDraw[2], canvas, mSubSections[3], mMinutePaint, mTextAlignment);
        else {
            adjustMinuteText(mMinutePaint, textToDraw[2] + "-" + textToDraw[3]);
            boundary.drawRectText(textToDraw[2] + "-" +  textToDraw[3], canvas, mSubSections[3], mMinutePaint, mTextAlignment);
        }

        adjustSayingText(mSayingPaint, textToDraw[4]);
        boundary.drawRectText(textToDraw[4], canvas, mSubSections[4], mSayingPaint, mTextAlignment);
    }


    private void adjustMinuteText(Paint mMinutePaint, String textToMeasure) {
        if(mMinutePaint.measureText(textToMeasure, 0, textToMeasure.length()) <= getDisplayMetrics().widthPixels) {
            return;
        } else {
            mMinutePaint.setTextSize(mMinutePaint.getTextSize() - 0.1f);
            adjustMinuteText(mMinutePaint, textToMeasure);
        }
    }

    private void adjustMinuteTextRound(Paint mMinutePaint, String textToMeasure) {
        if(mMinutePaint.measureText(textToMeasure, 0, textToMeasure.length()) <= boundary.getRectWidth()) {
            return;
        } else {
            mMinutePaint.setTextSize(mMinutePaint.getTextSize() - 0.1f);
            adjustMinuteTextRound(mMinutePaint, textToMeasure);
        }
    }

    private void adjustSayingText(Paint mSayingPaint, String textToMeasure) {
        if(mSayingPaint.measureText(textToMeasure, 0, textToMeasure.length()) <= getDisplayMetrics().widthPixels) {
            return;
        } else {
            mSayingPaint.setTextSize(mSayingPaint.getTextSize() - 0.1f);
            adjustSayingText(mSayingPaint, textToMeasure);
        }
    }

    private void adjustSayingTextRound(Paint mSayingPaint, String textToMeasure) {
        if(mSayingPaint.measureText(textToMeasure, 0, textToMeasure.length()) <= boundary.getRectWidth()) {
            return;
        } else {
            mSayingPaint.setTextSize(mSayingPaint.getTextSize() - 0.1f);
            adjustSayingTextRound(mSayingPaint, textToMeasure);
        }
    }

    /**
     * Get the applications display metrics.
     * @return The apps display metrics
     */
    public DisplayMetrics getDisplayMetrics() {
        return mContext.getResources().getDisplayMetrics();
    }

    /**
     * Return the CustomDateFormatter used to format the text
     * @return The CustomDateFormatter used to format the text
     */
    public CustomWordDateFormatter getDateFormatter() {
        return mDateFormatter;
    }

    public PhraseBook getPhraseBook() {
        return mPhraseBook;
    }

    /**
     * Get the main Typeface array.
     * @return Array of Typeface class
     */
    public Typeface[] getTypeFace() {
        return typeFaces;
    }

    public void updateDate() {
        mDateFormatter.updateDate();
    }

    /**
     * Set alignment type
     * @param alignmentType The alignment type, true for Left and false for Center
     */
    public void setAlignmentType(boolean alignmentType) {
        if(alignmentType) {
            mTextAlignment = Paint.Align.LEFT;
        } else
            mTextAlignment = Paint.Align.CENTER;
    }
}
