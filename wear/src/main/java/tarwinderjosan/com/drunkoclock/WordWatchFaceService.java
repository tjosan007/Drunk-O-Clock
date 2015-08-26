package tarwinderjosan.com.drunkoclock;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import utils.DebugManager;


/**
 * Main WatchFaceService for Drunk O' Clock application.
 * This is the WatchFace service for the Word class. Upon installation
 * it will display the watch face in the users watch face directory.
 * See
 */
public class WordWatchFaceService extends CanvasWatchFaceService {

    @Override
    public Engine onCreateEngine() {
        return new MainEngine();
    }


    /**
     * Class responsible for drawing the watch face on the canvas and
     * listening for changes to the preferences
     */
    public class MainEngine extends Engine implements
            OnSharedPreferenceChangeListener {
        private boolean mIsRound;

        private VibrateHandler mVibrateHandler;
        // Interface for dealing with the preference file
        private PreferencesHandler mPreferenceHandler;
        // Actual link to the preferences file
        private SharedPreferences mPreferences;
        // Reference to the watch face
        private WordWatchFace mMainWatchFace;
        // Reference to the last visibility of the watch
        boolean lastVisible = true;

        long startTime = 0;

        /**
         * Called when the app is destroyed.
         * Remove all the runnable callbacks and call the constructor
         * in the super class.
         */
        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        // SurfaceHolder is abstract interface to hold a display surface
        // Overridden from WatchFaceService.Engine
        // Initializes the watch face and main objects upon start up
        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            startTime = System.currentTimeMillis();

            // Init vibrator
            mVibrateHandler = new VibrateHandler(getApplicationContext());
            // Init the pref manager
            mPreferenceHandler= new PreferencesHandler(getApplicationContext());
            // Init and retrieve the Shared Preference obj
            mPreferences = mPreferenceHandler.initializeSharedPreference();
            // Register a listener
            // All preference changes are then passed to the listener
            // Notice it is only ever modified in the ListenerService in respond
            // to mobile preference changes

            mPreferences.registerOnSharedPreferenceChangeListener(this);
            // Build the WatchFaceStyle and then set it back to a WatchFaceStyle
            // This is the static nested class in WatchFaceStyle

            // Inherited method from WatchFaceService.Engine
            // Set the watch face style
            setWatchFaceStyle(new WatchFaceStyle.Builder(WordWatchFaceService.this)
                    // How far into the screen the first card will peek while the watch face is displayed
                    // (i.e set so it will have a small height)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    // Set to display the peak cards when the watch is in ambient mode
                    .setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_VISIBLE)
                    // Sets how to display background of the first peeking card
                    .setBackgroundVisibility(WatchFaceStyle.PEEK_OPACITY_MODE_TRANSLUCENT)
                    .setStatusBarGravity(Gravity.RIGHT | Gravity.TOP)
                    // Set the location of "Ok Google" keyword to be aligned center in first Rect
                    .setHotwordIndicatorGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
                    .setShowUnreadCountIndicator(false)
                    // Do not draw the system-style time over the watch face
                    .setShowSystemUiTime(false).build());

        } // end onCreate

        /**
         * Called every time the watch face is invalidated.
         * Overridden the CanvasWatchFaceService.Engine class.
         * @param canvas Canvas to do the drawing
         * @param bounds Rect which defines the bounds of the interface
         */
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            mMainWatchFace.updateDate();
            mMainWatchFace.draw(canvas, bounds);
        }

        /**
         * Overridden from the WatchFaceService.Engine class
         * This method is called each minute when in ambient mode or when the user clicks
         * the screen. It is a substitute for the timeRunnable for when working each minute only.
         */
        @Override
        public void onTimeTick() {
            super.onTimeTick();
            DebugManager.print("TAG", "Invalidating onTimeTick()");
            mMainWatchFace.getPhraseBook().updateAppropriateSaying();
            mMainWatchFace.getPhraseBook().updateAppropriateSwear();
            invalidate();
        }

        /**
         * Called when the watch becomes visible or not.
         * @param visible Is the watch visible?
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
        }

        /**
         * Called when the device enters or exists ambient mode.
         * Provide black/white display and no animations when in ambient
         * mode to preserve battery life.
         * @param inAmbientMode
         */
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            mMainWatchFace.setCharacteristics(inAmbientMode);
            invalidate();
        }

        /**
         * Called when first peeking card positions itself on the screen.
         * @param rect Provides information about the position of the card
         */
        @Override
        public void onPeekCardPositionUpdate(Rect rect) {
            super.onPeekCardPositionUpdate(rect);
        }

        /**
         * Called when the properties of the device are determined.
         * @param properties
         */
        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
            // Implement burn-in protection someday

        }

        /**
         * Called when the preferences are changed to update the watch face.
         * Usually triggered after a call in ListenerService.
         * @param sharedPreferences SharedPreference obj associated with the preference file
         * @param key The key associated with the change
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            // Get the Phrase Choice, Sober as default
            switch(key){
                case "wordPackType":
                    String wordPackVal  = (sharedPreferences.getString("wordPackType", "1"));
                    // Internally calls postInvalidate() to reflect the change
                    mMainWatchFace.getPhraseBook().setWordPackTo(getWordPackFromInt(wordPackVal));
                    break;
                case "alignmentType":
                    boolean alignmentLeft = sharedPreferences.getBoolean("alignmentType", true);
                    mMainWatchFace.setAlignmentType(alignmentLeft);
            }
            mVibrateHandler.vibrateQuick();

        }

        /**
         * Get the WordPack from the preferences
         * @return A WordPack enum
         */
        public WordPack getWordPackFromPreferences(){
            // Pass in to the String representing the number choice
            return getWordPackFromInt(mPreferences.getString("wordPackType", "1"));

        }

        /**
         * Return a Word Pack from an integer supplied.
         * @return Word Pack
         */
        public WordPack getWordPackFromInt(String numAsString) {
            // Default init
            WordPack mWordPack = WordPack.SOBER;
            switch(Integer.parseInt(numAsString)) {
                case 1:
                    mWordPack = WordPack.SOBER;
                    break;
                case 2:
                    mWordPack = WordPack.TIPSY;
                    break;
                case 3:
                    mWordPack = WordPack.WASTED;
                    break;
                case 4:
                    mWordPack = WordPack.CUSTOM;
            }
            return mWordPack;
        }

        /**
         * Method called after onCreate(), used to determine screen type
         * @param insets The Window Insets
         */
        @Override
        public void onApplyWindowInsets(WindowInsets insets) {

            super.onApplyWindowInsets(insets);
            // Indicates if the device is a round device, and the chin size.
            mIsRound = insets.isRound();
            // Get the default word pack
            // Initialize the word watch face object
            WordPack pack = getWordPackFromPreferences();
            // Create new instance of the main Watch Face class
            mMainWatchFace = new WordWatchFace(pack, getApplicationContext(), this, mIsRound,
            getAlignTypeFromPreferences());
        }

        /**
         * Get the alignment type from the preferences
         * @return The alignment type, default "Left" if none available (new user)
         */
        public Paint.Align getAlignTypeFromPreferences() {
            // True for left, false for center
            boolean isAlignmentLeft = mPreferences.getBoolean("alignmentType", false);
            if(isAlignmentLeft)
                return Paint.Align.LEFT;
            else
                return Paint.Align.CENTER;
         }
    } // end class SimpleEngine

} // end class WatchFace
