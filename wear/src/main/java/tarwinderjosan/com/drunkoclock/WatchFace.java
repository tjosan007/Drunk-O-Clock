package tarwinderjosan.com.drunkoclock;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.concurrent.TimeUnit;

/**
 * Main WatchFaceService for DrunkOClock application
 */
public class WatchFace extends CanvasWatchFaceService {

    @Override
    public Engine onCreateEngine() {
        return new MainEngine();
    }

    /**
     * Class responsible for drawing the watch face on the canvas
     */
    private class MainEngine extends CanvasWatchFaceService.Engine {

        boolean lastVisible = true;

        private WatchFacePainter watchFace;
        // TimeUnit representing seconds used for conversions
        TimeUnit secondsUnit = TimeUnit.SECONDS;
        // Convert (1) duration (1 second) to milliseconds (1000 seconds)
        // The parameter represents the number of seconds to use for the conversion
        private final long TICK_PERIOD_MILLIS = secondsUnit.toMillis(1);

        // Handler for minute by minute notifications
        // Processes the runnable timeRunnable associated with the thread MessageQueue
        private Handler timeTickHandler;


        private Runnable timeRunnable= new Runnable() {
            @Override
            public void run() {
                // Call onSecondTick(), which calls another method to invalidate the screen
                // if two conditions are true, it is visible and not in ambient mode.
                onSecondTick();
                if(isVisible() && !isInAmbientMode())
                    // Run THIS runnable after TICK_PERIOD_MILLIS elapses
                    // This calls onSecondTick() mainly as seen above
                    timeTickHandler.postDelayed(this, TICK_PERIOD_MILLIS);
            }
        };


        /**
         * On each second, draw the interface
         */
        private void onSecondTick() {
            invalidateWhenNeeded();
        }

        /**
         * Draw if it is visible and is not in ambient mode
         */
        private void invalidateWhenNeeded() {
            if(isVisible() && !isInAmbientMode()) {
                Log.d("TAG", "INVALIDATING");
                invalidate();
            }
        }


        /**
         * Called when the app is destroyed.
         * Remove all the runnable callbacks and call the constructor
         * in the super class.
         */
        @Override
        public void onDestroy() {
            timeTickHandler.removeCallbacks(timeRunnable);
            super.onDestroy();
        }

        // SurfaceHolder is abstract interface to hold a display surface
        // Overridden from WatchFaceService.Engine
        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            // Build the WatchFaceStyle and set it back to a WatchFaceStyle
            WatchFaceStyle.Builder styler = new WatchFaceStyle.Builder(WatchFace.this);
            // How far into the screen the first card will peek while the watch face is displayed
            // (i.e set so it will have a small height)
            styler.setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT);
            // Set to NOT display the peak cards when the watch is in ambient mode
            styler.setAmbientPeekMode(WatchFaceStyle.AMBIENT_PEEK_MODE_HIDDEN);

            // Sets how to display background of the first peeking card
            // Only display briefly, and only if the peek card represents an interruptive
            // notification
            styler.setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE);
            // Make the system not draw the system-style time over the watch face
            styler.setShowSystemUiTime(false);

            // Inherited method from WatchFaceService.Engine
            // Requires WatchFaceStyle, the build() converts back to WatchFaceStyle
            setWatchFaceStyle(new WatchFaceStyle.Builder(WatchFace.this).build());

            timeTickHandler = new Handler(Looper.myLooper());

            startTimerIfNecessary();

            // Initialize the watchFace class, has methods used for drawing on the canvas
            // Pass in instance of the class and the Shape object representing device shape
            watchFace = WatchFacePainter.getMainInstance(WatchFace.this);
        } // end onCreate

        /**
         * Start the timer only if the watch is visible and not in ambient mode
         * Remove the previous posts of Runnable and add a new one.
         */
        private void startTimerIfNecessary() {
            // Remove previous posts of the runnable
            timeTickHandler.removeCallbacks(timeRunnable);
            if(isVisible() && !isInAmbientMode())
                // Add to the running queue, process the runnable
                timeTickHandler.post(timeRunnable);
        }

        /**
         * Called every time the watch face is invalidated.
         * Overridden the CanvasWatchFaceService.Engine class.
         * @param canvas Canvas to do the drawing
         * @param bounds Rect which defines the bounds of the interface
         */
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);
            watchFace.draw(canvas, bounds);
        }

        /**
         * Overridden from the WatchFaceService.Engine class
         * This method is called each minute only when in ambient mode.
         */
        @Override
        public void onTimeTick() {
            super.onTimeTick();
            // Invalidate the watch
            invalidate();
        }

        /**
         * Called when the watch becomes visible or not.
         * @param visible Is the watch visible?
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            startTimerIfNecessary();
            // back to visible after being non visible, update text
            if(!lastVisible) {
                // 25% chance of the word changing when back to visible
                if (watchFace.getCurrentStyle().getDateFormatter().getWatchFaceLook() == WatchFaceLook.WORD) {
                    if (watchFace.getCurrentStyle().getPhraseBook().allowUpdateWordRandom()) {
                        watchFace.getCurrentStyle().getPhraseBook().updateAppropriateSaying();
                        // redraw the word
                        invalidate();
                    }
                }
            }

            lastVisible = visible;
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
            // Call methods in the MainWatchFace class to modify the drawing
            // when in ambient mode
           watchFace.getCurrentStyle().setCharacteristics(inAmbientMode);
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
            // Implement burn-in protection
        }



    } // end class SimpleEngine
} // end class WatchFace
