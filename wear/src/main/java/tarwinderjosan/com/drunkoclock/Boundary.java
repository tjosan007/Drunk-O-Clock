package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Manages the boundary rectangles of the class and the main boundary rectangle parent.
 * The boundary rectangles in a device help manage alignment.
 * Square device and circular devices have different methods of
 * creating the boundary to house the text.
 *
 * This class also manages the bound creation for square and circular devices
 * Date: May 22, 2015
 */
public class Boundary {

    private boolean mIsRound;
    private Context mContext;
    private Rect mBounds;

    // Height of each subsection
    private float mRectHeight;
    // Width of each rect
    private float mRectWidth;
    // Subsection rectangles
    private static Rect[] mSubSections;
    /**
     * Main constructor.
     * Constructs a Boundary object used to manage the boundary rectangles of the device
     * @param isRound The type of the device, true if the device is round,
     *                   false otherwise.
     * @param context The context of the application, used for background data.
     * @param divisions The number of boundary rectangles to make, how many times the original
     *                  rectangle is split.
     */
    public Boundary(Context context, boolean isRound, int divisions) {
        mIsRound = isRound;
        mContext = context;
        // Set device bounds
        mBounds = setBounds(isRound);
        // Create the subsection rectangles
        mSubSections = getCanvasRects(divisions, mBounds);
        // All heights are equal, so just grab any subsection rects height
        mRectHeight = mSubSections[0].height();
        mRectWidth = mSubSections[0].width();
    }


    /**
     * Divide a Rectangle into n number of rectangles and return the Rect
     * array containing it.
     * @return Rect array containing all the sub rectangles
     * @param divisions The number of divisions to produce
     * @param rect The boundary Rect
     */
    public Rect[] getCanvasRects(int divisions, Rect rect) {
        float deviceHeight = rect.height();
        float deviceWidth = rect.width();
        // Get the height of each rectangle (5 rects total)
        float rectHeight = deviceHeight / divisions;
        // The starting point for the first subsection and the boundary
        // The y value > 0 for round devices and = 0 for square devices
        float startingPoint = (mContext.getResources().getDisplayMetrics().heightPixels - mBounds.height()) / 2;
        // Configure each section from top down

        Rect[] subSections = new Rect[divisions];
        for(int i = 0; i < divisions; i++) {
            if(mIsRound) {
                // The X value starts at the distance the bounds Rect is away from the left
                // Second parameter (top y value) starts at the Y value of the first sub section
                // This is the original rect height minus the new rect height then divided by 2
                subSections[i] = new Rect(mBounds.left, (int)(startingPoint + ( i * rectHeight) ), mBounds.right, (int) (startingPoint + rectHeight * (i + 1) ) );
            }
            else {
                // For square device the subsection Rects are always in reference to the main Rect (the device Canvas)
                // So the left X value will always be 0 for square/rect devices
                subSections[i] = new Rect(0, (int) (i * rectHeight), (int) deviceWidth, (int) rectHeight * (i + 1));
            }
        }
        return subSections;
    }

    public Rect getRectBounds() {return mBounds;}

    public float getRectWidth() {
        return mRectWidth;
    }

    /**
     * Draws the subsection rectangles.
     * Used solely for debugging and viewing the subsection Rects.
     * @param canvas The canvas to draw the sub sections on
     */
    public void drawSubSections(Canvas canvas) {
        Paint r1 = new Paint();
        r1.setColor(Color.RED);
        r1.setStyle(Paint.Style.STROKE);
        r1.setStrokeWidth(1);
        for(int i = 0; i < mSubSections.length; i++) {
            canvas.drawRect(mSubSections[i], r1);
            canvas.drawCircle(mSubSections[i].centerX(), mSubSections[i].centerY(), 1, r1);
        }
    }

    /**
     * Draw text in the rectangle with specified alignment.
     * @param text The actual text to draw
     * @param canvas The actual canvas to draw on
     * @param r The rect to draw in
     * @param textPaint The textPaint to use for drawing'
     * @param style The type of styling to apply (centered, left justified, etc.
     */
    public void drawRectText(String text, Canvas canvas, Rect r, Paint textPaint, Paint.Align style) {// Difference in rect height and text height
        float yOffset = (textPaint.ascent() + textPaint.descent()) / 2;
        // Left alignment?
        if(style == Paint.Align.LEFT) {
            textPaint.setTextAlign(Paint.Align.LEFT);
            // Square devices would start drawing at x = 0 for a left align, round devices at the
            // left X val of the rect
            if(!mIsRound) {
                canvas.drawText(text, r.left, r.centerY() - yOffset, textPaint);
            }
            else {
                canvas.drawText(text, r.left, r.centerY() - yOffset + 5 - (r.height() / 2),
                            textPaint);
                }
            }

        // Center alignment?
        else if (style == Paint.Align.CENTER) {
            textPaint.setTextAlign(Paint.Align.CENTER);
            if(!mIsRound)
                canvas.drawText(text, r.exactCenterX(), r.centerY() - yOffset, textPaint);
            else
                canvas.drawText(text, r.exactCenterX(), r.centerY() - yOffset- (r.height() / 2), textPaint );
        }
    }

    /**
     * Set the bounds of a watch.
     * On round devices, the bound is a square which fits inside the screen.
     * On rect devices, the bound is the screen itself.2
     * @param isRound True if the watch is circular, false if not
     * @return Rect with the appropriate bounds
     */
    public Rect setBounds(boolean isRound) {
        // Get the applications DisplayMetrics using the context
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();

        // If the device is round, the boundary is a square with maximum area enclosed on the device
        // screen.
        if (isRound) {
            if(metrics.heightPixels == 290) {
                metrics.heightPixels = 320;
            }
            float radius = metrics.heightPixels / 2;
            // Get the opposite length of the special triangle
            // This is the y-value of the rect coordinate in the 1st quad

            // Get the point of the first quad using trig using special triangles
            // This is the height of each 45-45-90 triangle which could be made
            // in each quadrant to maximize the Rectangle area.
            // To get the Y value, use the middle point as reference and subtract this value
            double lengthOppYQuad2 = metrics.heightPixels / 2 - radius * Math.sin(Math.PI / 4);
            double lengthOppYQuad3 = metrics.heightPixels / 2 + radius * Math.sin(Math.PI / 4);
            // Same as the above, to get the x coordinate
            // The X coordinate for 1st quad is the width / 2 plus the ACTUAL value for the Opposite
            double lengthOppXQuad1 = metrics.widthPixels / 2 + (radius * Math.sin(Math.PI / 4));
            double lengthOppXQuad2 = metrics.heightPixels / 2 - (radius * Math.sin(Math.PI / 4));

            // Left X (2nd quad), Top Y (2nd quad), Right X (1st quad), Bottom Y (3rd quad)
            // See the Javadoc for the Rect constructor
            Rect rect = new Rect((int) lengthOppXQuad2, (int) lengthOppYQuad2, (int) lengthOppXQuad1, (int) lengthOppYQuad3);
            // Return a square inside the round visible section
            return rect;
        } else {
            // Optimize for square devices

            // The bounds in a square device is just a rectangle the size of the device
            Rect rect = new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);
            return rect;
        }
    }

    public Rect[] getSubSections() {
        return mSubSections;
    }

    public static Rect getFirstRect() {
        return mSubSections[0];
    }
}
