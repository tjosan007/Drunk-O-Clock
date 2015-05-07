package tarwinderjosan.com.drunkoclock;

import android.app.ListActivity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Alternative way to use ListViews. This class is used for testing.
 * Use WatchFaceConfigActivity with CustomListAdapter instead.
 *
 * There are a few ways to implement lists:
 *
 * First way is to extend the ListActivity class as shown below.
 * Then calling the setListAdapter(arrayAdapter) will do the trick.
 * The ArrayAdapter takes the context, followed by the XML layout
 * it is using (basically the type of views it is holding and their
 * parameters) and the list to implement. To listen for clicks
 * requires overriding the method onItemClick()
 *
 * The second way is instead to extend the Activity class.
 * Creating a separate XML layout document which houses a ListView.
 * Create a link to the list view and set the list adapter on that
 * list view. To listen for clicks requires setting a listener to
 * AdapterView.OnItemClickListener.
 *
 * In both cases the parameters can be modified on the TextView which is used for each row.
 *
 */
public class WatchFaceConfigActivity2 extends ListActivity{



    private TextView mTextView;
    private Typeface mainFont;
    // Create the list of items
    String[] items= {"Look and Feel", "Date/Time", "Features", "Weather", "Advanced",
            "Information and Support"};
    String[] classNames = {"LookFeel", "DateTime", "Features", "Weather", "Advanced", "Support"};
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mainFont = Typeface.createFromAsset(this.getAssets(), "mainfont.otf");
        mTextView = (TextView)findViewById(R.id.textView);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.custom_list, items));
    }
}
