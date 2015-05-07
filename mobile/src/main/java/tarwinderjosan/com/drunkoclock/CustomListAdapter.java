package tarwinderjosan.com.drunkoclock;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Represents a custom list adapter where the values like font/color can be
 * modified. The views in the lists are simply TextViews with modified
 * parameters.
 *
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private final String TAG = "CustomListAdapter";
    private Context context;

    public CustomListAdapter(Context context, int textViewResourceID, String[] list) {
        super(context, textViewResourceID, list);
        this.context = context;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        LayoutInflater vi = LayoutInflater.from(getContext());
        View customView = vi.inflate(R.layout.custom_list, parent, false);
        Log.d(TAG, "Position: " + position);
        String firstItem = getItem(position);
        TextView textView = (TextView)customView.findViewById(R.id.textView);
        textView.setText(firstItem);
        /*
        Dynamic adjusting of font size below.
        Use the properties on the Display instead of DisplayMetrics on pre honey comb devices (<= api 12)
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Uses display metrics class
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDisplayMetrics().widthPixels * 0.05f);
        } else {
            Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            // uses deprecated display getWidth() method
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, display.getWidth() * 0.05f);
        }
        textView.setTextColor(Color.WHITE);
        // Apply padding
        textView.setPadding(0, 50, 0, 50);
        textView.setGravity(Gravity.CENTER);
        // Apply fonts
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf"));

        return customView;
    }
}
