package tarwinderjosan.com.drunkoclock;

import android.os.Handler;
import android.util.Log;

/**
 * Handles fade transitions (and will be handling slide in the future)
 */
public class Animation {
    private boolean mAnimationFadeRunning;
    public long mTimeStartFadeAnimation;
    // Delay for each increment of the fade in from 0 alpha.
    // Too make it look like a smooth transition.
    private final int FADE_IN_DELAY_MS = 65;
    // The value to increment alpha by
    // Slow calls for smoother transition
    private final int FADE_INCREMENT_ALPHA = 5;
    // Times the loop must be called to fully fade in to 255 alpha value
    private final int TIMES_CALLED = 255 / FADE_INCREMENT_ALPHA;

    private WordWatchFace mMainWatchFace;
    public Animation(WordWatchFace watchFace) {mMainWatchFace = watchFace;}


    /**
     * Fade in the minute value, from a default alpha of 0 to 255.
     */
//    public void fadeInMinute() {
//        mMainWatchFace.getMinutePaintFadeIn().setAlpha(mMainWatchFace.getMinutePaintFadeIn().getAlpha() + FADE_INCREMENT_ALPHA);
//    }
//
//    public void fadeInWord() {
//        mMainWatchFace.getWordPaintFadeIn().setAlpha(mMainWatchFace.getWordPaintFadeIn().getAlpha() + FADE_INCREMENT_ALPHA);
//    }
//
//    public void setFadeInMinuteAlpha(int val) {
//        mMainWatchFace.getMinutePaintFadeIn().setAlpha(val);
//    }

    //public void setFadeInWordAlpha(int val) {mMainWatchFace.getWordPaintFadeIn().setAlpha(val);}

    public void setAnimationRunningFadeIn(boolean val) {
        mAnimationFadeRunning = val;
    }

    public boolean isAnimationRunningFadeIn() {return mAnimationFadeRunning;}

    public void setTimeStartFadeInAnimation(long millis) {
        mTimeStartFadeAnimation = millis;
    }

    public long getTimeStartFadeInAnimation() {
        return mTimeStartFadeAnimation;
    }


    /**
     * Post a specific runnable on the System handler.
     * @param handler The Handler to post on
     * @param runnable The runnable to post
     */
    public void animationFadePostDelay(Handler handler, Runnable runnable) {
        // The animation time is FADE DELAY MS * FADE INCREMENT ALPHA
        for (int i = 1; i < TIMES_CALLED; i++) {
            handler.postDelayed(runnable, i * FADE_IN_DELAY_MS);
        }

    }

    /**
     * Get the total time which the fade animation will take,
     */
    public int getAnimationFadeTimeElapse() {
        return TIMES_CALLED * FADE_IN_DELAY_MS;
    }

}
