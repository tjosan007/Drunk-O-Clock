package tarwinderjosan.com.drunkoclock;

import android.content.Context;

import java.util.Calendar;

import utils.DebugManager;

/**
 * Handles the phrases, retrieving the phrases and returning phrases.\
 * Eventually will use a preference file for the user editable settings like
 * watch face style and phrases to decide upon each load up.
 *
 * To do
 * Implement user preference file to handle word pack selection as well as watch type selections.
 * Settings -> modifies preference file
 *
 * Change-log:
 * Updated with method to generate random second word
 * Changed generation percentage to 85%
 * This should be updatable using SharedPreferences class
 */
public class PhraseBook {

    WordPack mWordPack;
    private WordWatchFaceService.MainEngine mMainServiceEngine;

    private String appropriateSaying = "";
    private String appropriateSwear = "";

    // The following are Arrays holding the word packs
    String[] soberWordPack;
    String[] swearsSober;

    String[] tipsyWordPack;
    String[] swearsTipsy;

    String[] wastedWordPack;
    String[] swearsWasted;

    WordWatchFace wordWatchFace;

    /**
     * Load phrases
     * @param context Application context
     * @param word The WordWatchFace class reference
     * @param engine The Engine reference
     * @param mPack Initial word pack
     */
    public PhraseBook(Context context, WordWatchFace word, WordWatchFaceService.MainEngine engine, WordPack mPack ) {


        // Ref to word class
        wordWatchFace = word;
        // Ref to main engine
        mMainServiceEngine = engine;
        // The word pack to use
        mWordPack = mPack;

        // Initialize all the Word Packs
        soberWordPack = this.getPhraseArray(context, R.array.sober_wordpack);
        swearsSober = this.getPhraseArray(context, R.array.swears_sober);

        swearsTipsy = this.getPhraseArray(context, R.array.swears_tipsy);
        tipsyWordPack = this.getPhraseArray(context, R.array.tipsy_wordpack);

        swearsWasted = this.getPhraseArray(context, R.array.swears_wasted);
        wastedWordPack = this.getPhraseArray(context, R.array.wasted_wordpack);

        updateAppropriateSaying();
        updateAppropriateSwear();
    }


    /**
     * Get a phrase array.
     * @param context main class context
     * @param id integer val of the array
     * @return String containing all the array values
     */
    public String[] getPhraseArray(Context context, int id) {
        return context.getResources().getStringArray(id);
    }

    /**
     * Used for the last drawing, eg "Ohh, it's monday"
     */
    public void updateAppropriateSwear() {
        switch(mWordPack) {
            case SOBER:
                appropriateSwear = swearsSober[(int) (swearsSober.length * Math.random())];
                break;
            case TIPSY:
                appropriateSwear = swearsTipsy[(int) (swearsTipsy.length * Math.random())];
                break;
            case WASTED:
                appropriateSwear = swearsWasted[(int) (swearsWasted.length * Math.random())];
                break;
            default:
                appropriateSwear = swearsSober[(int) (swearsSober.length * Math.random())];
        }
    }

    /**
     * Swear is the second draw from the top, eg "flippin"
     */
    public void updateAppropriateSaying() {
        switch(mWordPack) {
            case SOBER:
                appropriateSaying = soberWordPack[(int) (soberWordPack.length * Math.random())];
                break;
            case TIPSY:
                appropriateSaying = tipsyWordPack[(int) (tipsyWordPack.length * Math.random())];
                break;
            case WASTED:
                appropriateSaying = wastedWordPack[(int) (wastedWordPack.length * Math.random())];
                break;
            default:
                appropriateSaying = soberWordPack[(int) (soberWordPack.length * Math.random())];
        }
    }

    /**
     * Allow an update to both words during visibility change
     * 85% random chance
     */
    public boolean allowUpdateWordRandom() {
        int num = (int)(100*Math.random() + 1);
        if(num <= 85) {
            return true;
        }
        return false;
    }
    /**
     * Get the first saying in the word pack.
     */
    public String getAppropriateSaying() {
        DebugManager.print("TAG", "App saying: " + appropriateSaying);
        return appropriateSaying;

    }

    /**
     * Get the final saying
     * @param formatter CustomDateFormatter object used for retrieving the day
     * @return final string
     */
    public String getFinalSaying(CustomWordDateFormatter formatter) {
        String s = appropriateSwear + ", it's " + formatter.getDay();
        return s;
    }

    /**
     * Update the phrases to reflect on the watch.
     * Also invalidate the watch to reflect.
     */
    public void updatePhrases() {
        updateAppropriateSaying();
        updateAppropriateSwear();
        // Post invalidate for calling outside of the UI class
        mMainServiceEngine.postInvalidate();
    }
    /**
     * Set the word pack to draw on
     * @param wordPack The Word pack in String format
     */
    public void setWordPackTo(WordPack wordPack) {
        mWordPack = wordPack;
        updatePhrases();
    }
}
