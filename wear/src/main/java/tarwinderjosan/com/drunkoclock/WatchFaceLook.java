package tarwinderjosan.com.drunkoclock;

/**
 * Enum holds different watch face types.
 * The watch face types distinguish the types of format used for date/time.
 * Each watch face look and feel has a different date/time setup associated with it, as well
 * as a different layout pattern.
 */
public enum WatchFaceLook {
    // PLAIN - The default watch face look
    // WORD - Watch watch look which displays the time in words
    // followed by a phrase
    // WEATHER  - Displays time with weather and few hr rules
    PLAIN, WEATHER, WORD
}
