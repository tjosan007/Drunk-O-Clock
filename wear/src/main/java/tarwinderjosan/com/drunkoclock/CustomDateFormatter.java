package tarwinderjosan.com.drunkoclock;

import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom date formatter for formatting the dates for the different types of watch faces.
 *
 * Change-log:
 * 5/1/2015
 * Added methods for the 2nd Word watch face
 * Included a PhraseBook object used internally by the class
 *
 */
public class CustomDateFormatter {

    // Digits used to format date
    String[] multiples = new String[]{"ten", "twenty", "thirty", "forty", "fifty", "sixty"};
    String[] digits = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"};
    // 10 - 19 inclusive
    String[] tens = new String[]{"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

   // Date format for main watch face look
   private final String DATE_FORMAT = "MMMM dd";
   private final String TIME_FORMAT = "k:mm";

  // Date object representing the current date
  private Date currentDate;

    // Holds the current Watch Face Type
    WatchFaceLook watchFaceLook;


    public CustomDateFormatter() {}

    /**
     * Get the current day
     * @return
     */
    public String getDay() {
        // Return the day in STRING format
        return new SimpleDateFormat("EEEE").format(currentDate);
    }

    /**
     * Get and return the date according to the watch face present
     * @return The date
     */
    public String getDateAccordingTo() {
        // Get and return the date
        if(watchFaceLook == WatchFaceLook.PLAIN) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
           return sdf.format(currentDate);
        }
        return "Error 9";
    }

    /**
     * Get the time according to the watch face style
     * @return The time according to the watch face style, contained in the array
     * Default watch face will have stored one element representing the time,
     * the other watch faces utilize the extra space to store converted forms into words
     * @param styler  The Style class in reference too
     */
    public String[] getTimeAccordingTo(Style styler) {
        // Return the time in 24 hr format if plain watch face look is used
        if(watchFaceLook == WatchFaceLook.PLAIN) {
            return new String[]{new SimpleDateFormat(TIME_FORMAT).format(currentDate)};
        } else if (watchFaceLook == WatchFaceLook.WORD) {
            // max 4 holds
            String[] textHolder = new String[5];
            textHolder[0] = getHourAsString();
            textHolder[1] = styler.getPhraseBook().getAppropriateSaying(styler);
            // Set the minute string in index 2 and 3
            // Set the minutes in the string
            textHolder = setMinuteString(textHolder);
            textHolder[4] = styler.getPhraseBook().getFinalSaying(styler.getDateFormatter());
            // add saying to the last index ---

            return textHolder;


        }

        return new String[]{"Error 10"};
    }

    /**
     * Set current watch face look
     * @param look
     */
    public void setWatchFaceLook(WatchFaceLook look) {
        this.watchFaceLook = look;
    }
    public WatchFaceLook getWatchFaceLook() {
        return watchFaceLook;
    }
    /**
     * Update the current Date object.
     */
    public void updateDate() {
        currentDate = new Date();

    }

    // The following methods are used exclusively for the WORD class

    /**
     * Get the hour in String format
     * @return The String with the hour
     */
    private String getHourAsString() {
        // Check the current hr and get a String
        String hour = new SimpleDateFormat("h").format(currentDate);
        Log.d("TAG", hour);
        return digits[Integer.parseInt(hour) - 1];
    }

    /**
     * check all possibilities for the minute and convert it to text form
     * Set the 2nd and 3rd indexes to the minute string
     * @param string
     */
    private String[] setMinuteString(String[] string) {
        String minute = new SimpleDateFormat("mm").format(currentDate);
        // Start of a new hour, no minutes avail
        if( (minute.charAt(0) == '0') && (minute.charAt(1) == '0') ) {
            // When an hour just started, get o, clock instead
            string[2] = "o, clock";
            return string;
            // Multiple of 10, like 20 minutes, 30 minutes
        } else if(minute.charAt(1) == '0') {
            string[2] = multiples[Character.getNumericValue(minute.charAt(0)) - 1];
        // Single digit for minute, like 1, 2 or 3
        } else if (minute.charAt(0) == '0') {
            string[2] = digits[Character.getNumericValue(minute.charAt(1)) - 1];
            return string;
        } else if (minute.charAt(0) == '1' && Character.getNumericValue(minute.charAt(1)) < 20){
                // when minute is 10 - 19 inclusive
                string[2] = tens[Character.getNumericValue(minute.charAt(1))];
                return string;
        } else {
            // final condition when the minute involves two strings
            string[2] = multiples[Character.getNumericValue(minute.charAt(0)) - 1];
            string[3] = digits[Character.getNumericValue(minute.charAt(1)) - 1];
            return string;
        }


        return string;
    } // end method

} // ~~! end main class
