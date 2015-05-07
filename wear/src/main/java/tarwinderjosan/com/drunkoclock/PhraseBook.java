package tarwinderjosan.com.drunkoclock;

import android.content.Context;

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
 */
public class PhraseBook {

    private String appropriateSaying = "";
    private String appropriateSwear = "";
    String[] soberWordPack;
    String[] swearsSober;

    /**
     * Load phrases
     * @param context
     */
    public PhraseBook(Context context) {
        // Go with the default sober for now
        // Displayed between hour and minute
        soberWordPack =  this.getPhraseArray(context, R.array.sober_wordpack);
        // Kinda Prefix for the line which displays the date
        swearsSober = this.getPhraseArray(context, R.array.swears_sober);
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

    public void updateAppropriateSwear() {
        appropriateSwear = swearsSober[(int)(swearsSober.length * Math.random())];
    }

    /**
     * Update with a random appropriate saying
     */
    public void updateAppropriateSaying() {
        appropriateSaying = soberWordPack[(int)(soberWordPack.length * Math.random())];
    }

    /**
     * Allow an update to the random word during visibility change
     * 25% change
     */
    public boolean allowUpdateWordRandom() {
        int num = (int)(100*Math.random() + 1);
        if(num <= 35) {
            return true;
        }
        return false;
    }
    /**
     * Get the first saying in the word pack.
     */
    public String getAppropriateSaying(Style styler) {
        return appropriateSaying;

    }

    /**
     * Get the final saying
     * @param formatter CustomDateFormatter object used for retrieving the day
     * @return final string
     */
    public String getFinalSaying(CustomDateFormatter formatter) {
        String s = appropriateSwear + ", it's " + formatter.getDay();
        return s;
    }
}
