package tarwinderjosan.com.drunkoclock;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Custom date formatter for formatting the date and time for the main watch face/
 * Date: May 15, 2015
 */
public class CustomWordDateFormatter {

    // Digits used to format date
    String[] multiples = new String[]{"ten", "twenty", "thirty", "forty", "fifty", "sixty"};
    String[] digits = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"};
    // 10 - 19 inclusive
    String[] tens = new String[]{"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

    // Date object representing the current date

    private Date currentDate;
    private WordWatchFace word;

    /**
     * Main constructor.
     * Initialize reference to Word class.
     * @param word Word class instance
     */
    public CustomWordDateFormatter(WordWatchFace word ) {
        this.word = word;
    }

    /**
     * Get the current day
     * @return The current day in String format
     */
    public String getDay() {
        // Return the day in STRING format
        // When the letters are 4 characters or more, full version is used of the name
        return new SimpleDateFormat("EEEE").format(currentDate);
    }

    /**
     * Get the time for the watch face.
     * @return The time according to the watch face style, contained in the array
     */
    public String[] getTimeAccordingTo() {
            String[] textHolder = new String[5];
            textHolder[0] = getHourAsString();
            textHolder[1] = word.getPhraseBook().getAppropriateSaying();
            // Set the minute string in index 2 and 3 (not always index 3, index 3 is null in instance
            // Such as when minute is divisible by 10

            // Set the minutes in the string
            textHolder = setMinuteString(textHolder);
            textHolder[4] = word.getPhraseBook().getFinalSaying(word.getDateFormatter());
            // add saying to the last index ---

            return textHolder;
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

    /**
     * Check to see if the time has changed and needs to be reflected
     * @return True if the time has changed and needs to be reflected
     */
    public boolean isDateChanged() {
        // Calendar for getting current time
        Calendar calendar = Calendar.getInstance();
        // Old Date object, without being updated
        Date oldDate = currentDate;
        // Formatter for the hour and minutes
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("h:mm");

        // Split string according to delimiter :
        String[] time = (currentTimeFormat.format(calendar.getTime()).split(":"));
        String[] oldTime = (currentTimeFormat.format(oldDate.getTime()).split(":"));

        // Minute has been changed!
        if(! (time[1].equals(oldTime[1])) ) {
            return true;
        } else {
            return false;
        }


    }

} // ~~! end main class
